/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.as.core.server;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.wst.server.core.IServer;
import org.jboss.ide.eclipse.as.core.model.EventLogModel;
import org.jboss.ide.eclipse.as.core.model.ServerProcessModel;
import org.jboss.ide.eclipse.as.core.model.EventLogModel.EventLogTreeItem;
import org.jboss.ide.eclipse.as.core.runtime.server.IServerStatePoller;
import org.jboss.ide.eclipse.as.core.runtime.server.polling.PollThread;
import org.jboss.ide.eclipse.as.core.runtime.server.polling.TwiddlePoller;
import org.jboss.ide.eclipse.as.core.server.stripped.DeployableServerBehavior;

public class JBossServerBehavior extends DeployableServerBehavior {
	public static final String LAUNCH_CONFIG_DEFAULT_CLASSPATH = "__JBOSS_SERVER_BEHAVIOR_LAUNCH_CONFIG_DEFAULT_CLASSPATH__";
	
	
	private PollThread pollThread = null;
	public JBossServerBehavior() {
		super();
	}

	public void stop(boolean force) {
		if( force ) {
			forceStop();
			return;
		}
		
		// If the server's already terminated via processes, just abort
		IProcess[] startProcesses = 
			ServerProcessModel.getDefault().getModel(getServer().getId()).getProcesses(JBossServerLaunchConfiguration.START);
		if( ServerProcessModel.allProcessesTerminated(startProcesses)) {
			forceStop();
			return;
		}
			
		// if we're starting up or shutting down and they've tried again, 
		// then force it to stop. 
		int state = getServer().getServerState();
		if( state == IServer.STATE_STARTING || state == IServer.STATE_STOPPING || state == IServer.STATE_STOPPED) {
			pollThread.cancel();
			forceStop();
			return;
		}
		
		Thread t = new Thread() {
			public void run() {
				// Otherwise execute a shutdown attempt
				try {
					// Set up our launch configuration for a STOP call (to shutdown.jar)
					ILaunchConfiguration wc = JBossServerLaunchConfiguration.setupLaunchConfiguration(getServer(), JBossServerLaunchConfiguration.STOP);
					wc.launch(ILaunchManager.RUN_MODE, new NullProgressMonitor());
				} catch( Exception e ) {
				}
			}
		};
		t.start();
	}
	
	protected void forceStop() {
		// just terminate the processes. All of them
		try {
			ServerProcessModel.getDefault().getModel(getServer().getId()).clearAll();
			setServerStopped();
			EventLogTreeItem tpe = new ForceShutdownEvent();
			EventLogModel.markChanged(tpe.getEventRoot());

		} catch( Throwable t ) {
			t.printStackTrace();
		}
	}

	public static final String FORCE_SHUTDOWN_EVENT_KEY = "org.jboss.ide.eclipse.as.core.server.JBossServerBehavior.forceShutdown";
	public class ForceShutdownEvent extends EventLogTreeItem {
		public ForceShutdownEvent() {
			super(EventLogModel.getModel(getServer()).getRoot(), null, FORCE_SHUTDOWN_EVENT_KEY);
		}
	}
	
	public void setupLaunchConfiguration(ILaunchConfigurationWorkingCopy workingCopy, IProgressMonitor monitor) throws CoreException {
		JBossServerLaunchConfiguration.setupLaunchConfiguration(workingCopy, getServer(), JBossServerLaunchConfiguration.START);
	}

	
	
	
	
	public void serverStarting() {
		setServerStarting();
		pollServer(IServerStatePoller.SERVER_UP);
	}
	
	public void serverStopping() {
		setServerStopping();
		pollServer(IServerStatePoller.SERVER_DOWN);
	}
	
	

	protected void pollServer(final boolean expectedState) {
		if( this.pollThread != null ) {
			pollThread.cancel();
		}
		this.pollThread = new PollThread("Server Poller", new TwiddlePoller(), expectedState, this);
		pollThread.start();
	}
	
}
