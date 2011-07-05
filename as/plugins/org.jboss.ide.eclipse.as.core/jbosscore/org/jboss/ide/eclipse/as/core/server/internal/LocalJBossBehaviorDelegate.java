/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.ide.eclipse.as.core.server.internal;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.jboss.ide.eclipse.as.core.ExtensionManager;
import org.jboss.ide.eclipse.as.core.ExtensionManager.IServerJMXRunnable;
import org.jboss.ide.eclipse.as.core.JBossServerCorePlugin;
import org.jboss.ide.eclipse.as.core.Messages;
import org.jboss.ide.eclipse.as.core.extensions.events.IEventCodes;
import org.jboss.ide.eclipse.as.core.extensions.events.ServerLogger;
import org.jboss.ide.eclipse.as.core.extensions.polling.ProcessTerminatedPoller.IProcessProvider;
import org.jboss.ide.eclipse.as.core.publishers.LocalPublishMethod;
import org.jboss.ide.eclipse.as.core.server.IJBossServerRuntime;
import org.jboss.ide.eclipse.as.core.server.IServerStatePoller;
import org.jboss.ide.eclipse.as.core.server.internal.launch.configuration.LocalStopLaunchConfigurator;
import org.jboss.ide.eclipse.as.core.util.IJBossRuntimeConstants;
import org.jboss.ide.eclipse.as.core.util.LaunchConfigUtils;
import org.jboss.ide.eclipse.as.core.util.PollThreadUtils;
import org.jboss.ide.eclipse.as.core.util.RuntimeUtils;

/**
 * 
 * @author Rob Stryker
 *
 */
public class LocalJBossBehaviorDelegate extends AbstractJBossBehaviourDelegate implements IProcessProvider {
	
	private static final String STOP_LAUNCH_TYPE = "org.jboss.ide.eclipse.as.core.server.stopLaunchConfiguration"; //$NON-NLS-1$

	protected PollThread pollThread = null;
	protected IProcess process;
	protected boolean nextStopRequiresForce = false;
	public LocalJBossBehaviorDelegate() {
		super();
	}
	public String getBehaviourTypeId() {
		return LocalPublishMethod.LOCAL_PUBLISH_METHOD;
	}
	public void stop(boolean force) {
		int state = getServer().getServerState();
		if( force 
				|| !isProcessRunning() 
				|| state == IServer.STATE_STOPPED 
				|| nextStopRequiresForce) {
			forceStop();
			return;
		}
		
		// if we're starting up or shutting down and they've tried again, 
		// then force it to stop. 
		if( state == IServer.STATE_STARTING || state == IServer.STATE_STOPPING ) {
			stopPolling();
			forceStop();
			return;
		}
		
		serverStopping();
		gracefullStop();
	}
	
	@Override
	protected IStatus gracefullStop() {
		new Thread() {
			
			@Override
			public void run() {
				try {
					ILaunchConfigurationWorkingCopy wc = 
							LaunchConfigUtils.createLaunchConfigurationWorkingCopy("Stop JBoss Server", STOP_LAUNCH_TYPE);  //$NON-NLS-1$
					new LocalStopLaunchConfigurator(getServer()).configure(wc);
					ILaunch launch = wc.launch(ILaunchManager.RUN_MODE, new NullProgressMonitor());
					IProcess stopProcess = launch.getProcesses()[0];
					waitFor(stopProcess);
					if (stopProcess.getExitValue() == 0) {
						// TODO: correct concurrent access to process, pollThread and nextStopRequiresForce
						if( isProcessRunning() ) { 
							getActualBehavior().setServerStarted();
							cancelPolling(Messages.STOP_FAILED_MESSAGE);
							nextStopRequiresForce = true;
						}
					}
				} catch( CoreException ce ) {
					JBossServerCorePlugin.getDefault().getLog().log(ce.getStatus());
				}
				
			}

			private void waitFor(IProcess process) {
				while( !process.isTerminated()) {
					try {
						Thread.yield();
						Thread.sleep(100);
					} catch(InterruptedException ie) {
					}
				}
			}
		}.start();
		// TODO: find out if this is ok. My current guess is that we should 
		// not thread here and return the real outcome
		return Status.OK_STATUS;
	}
	
	@Override
	protected synchronized void forceStop() {
		// just terminate the process.
		if( isProcessRunning()) {
			try {
				process.terminate();
				addForceStopEvent();
			} catch( DebugException e ) {
				addForceStopFailedEvent(e);
			}
		}
		process = null;
		getActualBehavior().setServerStopped();
	}
	
	protected void addForceStopFailedEvent(DebugException e) {
		IStatus status = new Status(IStatus.ERROR,
				JBossServerCorePlugin.PLUGIN_ID, IEventCodes.BEHAVIOR_FORCE_STOP_FAILED, 
				Messages.FORCE_TERMINATE_FAILED, e);
		ServerLogger.getDefault().log(getServer(), status);
	}
	protected void addForceStopEvent() {
		IStatus status = new Status(IStatus.ERROR,
				JBossServerCorePlugin.PLUGIN_ID, IEventCodes.BEHAVIOR_FORCE_STOP, 
				Messages.FORCE_TERMINATED, null);
		ServerLogger.getDefault().log(getServer(), status);
	}
	
	protected void addProcessTerminatedEvent() {
		IStatus status = new Status(IStatus.INFO,
				JBossServerCorePlugin.PLUGIN_ID, IEventCodes.BEHAVIOR_PROCESS_TERMINATED, 
				Messages.TERMINATED, null);
		ServerLogger.getDefault().log(getServer(), status);
	}
	
	protected transient IDebugEventSetListener processListener;
	
	public synchronized void setProcess(final IProcess newProcess) {
		if (process != null) { 
			return;
		}
		process = newProcess;
		if (processListener != null)
			DebugPlugin.getDefault().removeDebugEventListener(processListener);
		if (newProcess == null)
			return;
		
		processListener = new IDebugEventSetListener() {
			public void handleDebugEvents(DebugEvent[] events) {
				if (events != null) {
					int size = events.length;
					for (int i = 0; i < size; i++) {
						if (process != null && process.equals(events[i].getSource()) && events[i].getKind() == DebugEvent.TERMINATE) {
							DebugPlugin.getDefault().removeDebugEventListener(this);
							stopPolling();
							forceStop();
							addProcessTerminatedEvent();
						}
					}
				}
			}
		};
		DebugPlugin.getDefault().addDebugEventListener(processListener);
	}
	
	private boolean isProcessRunning() {
		return process != null 
				&& !process.isTerminated();
	}
	
	public void serverStarting() {
		nextStopRequiresForce = false;
		pollServer(IServerStatePoller.SERVER_UP);
	}
	
	public void serverStopping() {
		getActualBehavior().setServerStopping();
		pollServer(IServerStatePoller.SERVER_DOWN);
	}
	
	public synchronized IProcess getProcess() {
		return process;
	}
	
	protected void pollServer(final boolean expectedState) {
		IServerStatePoller poller = PollThreadUtils.getPoller(expectedState, getServer());
		PollThreadUtils.pollServer(expectedState, poller , pollThread, getActualBehavior());
	}
	
	protected void pollServer(boolean expectedState, IServerStatePoller poller) {
		this.pollThread = PollThreadUtils.pollServer(expectedState, poller, pollThread, getActualBehavior());
	}
	

	protected void stopPolling() {
		cancelPolling(null);
	}

	protected void cancelPolling(String message) {
		PollThreadUtils.cancelPolling(message, this.pollThread);
		this.pollThread = null;
	}
	
	public void publishStart(final IProgressMonitor monitor) throws CoreException {
		if( shouldSuspendScanner() ) {
			ExtensionManager.getDefault().getJMXRunner().beginTransaction(getServer(), this);
			IServerJMXRunnable r = new IServerJMXRunnable() {
				public void run(MBeanServerConnection connection) throws Exception {
					suspendDeployment(connection, monitor);
				}
			};
			try {
				ExtensionManager.getDefault().getJMXRunner().run(getServer(), r);
			} catch( CoreException jmxe ) {
				IStatus status = new Status(IStatus.WARNING, JBossServerCorePlugin.PLUGIN_ID, IEventCodes.SUSPEND_DEPLOYMENT_SCANNER, Messages.JMXPauseScannerError, jmxe);
				ServerLogger.getDefault().log(getServer(), status);
			}
		}
	}
	
	public void publishFinish(final IProgressMonitor monitor) throws CoreException {
		if( shouldSuspendScanner()) {
			IServerJMXRunnable r = new IServerJMXRunnable() {
				public void run(MBeanServerConnection connection) throws Exception {
					resumeDeployment(connection, monitor);
				}
			};
			try {
				ExtensionManager.getDefault().getJMXRunner().run(getServer(), r);
			} catch( CoreException jmxe ) {
				IStatus status = new Status(IStatus.WARNING, JBossServerCorePlugin.PLUGIN_ID, IEventCodes.RESUME_DEPLOYMENT_SCANNER, Messages.JMXResumeScannerError, jmxe);
				ServerLogger.getDefault().log(getServer(), status);
			} finally {
				ExtensionManager.getDefault().getJMXRunner().endTransaction(getServer(), this);
			}
		}
	}

	protected boolean shouldSuspendScanner() {
		return getActualBehavior().shouldSuspendScanner() && 
				ExtensionManager.getDefault().getJMXRunner() != null;
	}
	
	protected void suspendDeployment(final MBeanServerConnection connection, IProgressMonitor monitor) throws Exception {
		ObjectName name = new ObjectName(IJBossRuntimeConstants.DEPLOYMENT_SCANNER_MBEAN_NAME);
		launchDeployCommand(connection, name, IJBossRuntimeConstants.STOP, monitor);
	}
	

	
	protected void resumeDeployment(final MBeanServerConnection connection, IProgressMonitor monitor) throws Exception {
		ObjectName name = new ObjectName(IJBossRuntimeConstants.DEPLOYMENT_SCANNER_MBEAN_NAME);
		launchDeployCommand(connection, name, IJBossRuntimeConstants.START, monitor);
	}
	
	protected void launchDeployCommand(final MBeanServerConnection connection, final ObjectName objectName, 
			final String methodName, IProgressMonitor monitor) throws Exception {
		final Exception[] e = new Exception[1];
		final Object waitObject = new Object();
		final Boolean[] subtaskComplete = new Boolean[1];
		subtaskComplete[0] = new Boolean(false);
		Thread t = new Thread() {
			public void run() {
				Exception exception = null;
				try {
					executeDeploymentCommand(connection, objectName, methodName);
				} catch( Exception ex ) {
					exception = ex;
				}
				synchronized(waitObject) {
					e[0] = exception;
					subtaskComplete[0] = new Boolean(true);
					waitObject.notifyAll();
				}
			}
		};
		t.start();
		int count = 0;
		while(t.isAlive() && !monitor.isCanceled() && count <= 4000) {
			count+= 1000;
			synchronized(waitObject) {
				if( subtaskComplete[0].booleanValue() )
					break;
				waitObject.wait(1000);
			}
		}
		synchronized(waitObject) {
			if( !subtaskComplete[0].booleanValue()) {
				t.interrupt();
				IStatus status = new Status(IStatus.WARNING, JBossServerCorePlugin.PLUGIN_ID, IEventCodes.DEPLOYMENT_SCANNER_TRANSITION_CANCELED, Messages.JMXScannerCanceled, null);
				ServerLogger.getDefault().log(getServer(), status);
			} else if( e[0] != null ) {
				String error = methodName.equals(IJBossRuntimeConstants.START) ? Messages.JMXResumeScannerError : Messages.JMXPauseScannerError;
				IStatus status = new Status(IStatus.ERROR, JBossServerCorePlugin.PLUGIN_ID, IEventCodes.DEPLOYMENT_SCANNER_TRANSITION_FAILED, error, e[0]);
				ServerLogger.getDefault().log(getServer(), status);
			}
		}
	}
		
	protected void executeDeploymentCommand(MBeanServerConnection connection, ObjectName objectName, String methodName) throws Exception {
		connection.invoke(objectName, methodName, new Object[] {  }, new String[] {});
	}

	
	// Can start / stop / restart etc
	public IStatus canStart(String launchMode) {
		return canChangeState(launchMode);
	}
	public IStatus canRestart(String launchMode) {
		return canChangeState(launchMode);
	}
	public IStatus canStop() {
		return canChangeState(null);
	}
	public IStatus canStop(String launchMode) {
		return canChangeState(launchMode);
	}

	public IStatus canChangeState(String launchMode) {
		try {
		if( getServer() != null 
				&& getServer().getRuntime() != null 
				&& RuntimeUtils.checkedGetJBossServerRuntime(getServer()).getVM() != null )
			return Status.OK_STATUS;
		} catch(Exception e) {
			// ignore
		}
		return new Status(IStatus.ERROR, JBossServerCorePlugin.PLUGIN_ID, 
				"This server does not have a valid runtime environment"); //$NON-NLS-1$
	}
}
