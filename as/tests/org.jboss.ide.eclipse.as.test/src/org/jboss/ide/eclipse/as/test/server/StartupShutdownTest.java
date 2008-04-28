/**
 * JBoss, a Division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
package org.jboss.ide.eclipse.as.test.server;

import java.util.ArrayList;
import java.util.Date;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.wst.server.core.IRuntimeWorkingCopy;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerListener;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerEvent;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.server.core.IServer.IOperationListener;
import org.eclipse.wst.server.core.internal.RuntimeWorkingCopy;
import org.jboss.ide.eclipse.as.core.server.IJBossServerRuntime;
import org.jboss.ide.eclipse.as.core.server.internal.JBossServerBehavior;
import org.jboss.ide.eclipse.as.test.ASTest;

/**
 * These tests are for a simple startup / shutdown of a basic, 
 * default configuration. The deploy folder should be normal, 
 * with only required deployments that should not fail at all. 
 * 
 * @author Rob Stryker
 *
 */
public class StartupShutdownTest extends TestCase {

	protected static final IVMInstall VM_INSTALL = JavaRuntime.getDefaultVMInstall();
	protected static final String DEFAULT_CONFIG = "default";
	
	protected static final int DEFAULT_STARTUP_TIME = 150000;
	protected static final int DEFAULT_SHUTDOWN_TIME = 90000;

	protected IRuntime currentRuntime;
	protected IServer currentServer;
	protected ServerStateListener stateListener;
	
	public void setUp() {
		try {
			if( getName().equals("test32"))
				createServer(ASTest.JBOSS_RUNTIME_32, ASTest.JBOSS_SERVER_32, ASTest.JBOSS_AS_32_HOME, DEFAULT_CONFIG);
			else if( getName().equals("test40"))
				createServer(ASTest.JBOSS_RUNTIME_40, ASTest.JBOSS_SERVER_40, ASTest.JBOSS_AS_40_HOME, DEFAULT_CONFIG);
			else if( getName().equals("test42"))
				createServer(ASTest.JBOSS_RUNTIME_42, ASTest.JBOSS_SERVER_42, ASTest.JBOSS_AS_42_HOME, DEFAULT_CONFIG);
			
			// first thing's first. Let's add a server state listener
			stateListener = new ServerStateListener();
			currentServer.addServerListener(stateListener);

		} catch( CoreException ce ) {
			fail("Failed during setUp for " + getName() + ": " + ce.getMessage());
		}
	}
	
	public void tearDown() {
		System.out.println("tearing down " + getName());
		try {
			currentServer.removeServerListener(stateListener);
			currentServer.delete();
		} catch( CoreException ce ) {
			// report
		}
	}
	


	protected void createServer(String runtimeID, String serverID, 
					String location, String configuration) throws CoreException {
		// if file doesnt exist, abort immediately.
		assertTrue(new Path(location).toFile().exists());
		
		currentRuntime = createRuntime(runtimeID, location, configuration);
		IServerType serverType = ServerCore.findServerType(serverID);
		IServerWorkingCopy serverWC = serverType.createServer(null, null, new NullProgressMonitor());
		serverWC.setRuntime(currentRuntime);
		serverWC.setName(serverID);
		serverWC.setServerConfiguration(null);
		currentServer = serverWC.save(true, new NullProgressMonitor());
	}
	
	
	private IRuntime createRuntime(String runtimeId, String homeDir, String config) throws CoreException {
		IRuntimeType[] runtimeTypes = ServerUtil.getRuntimeTypes(null,null, runtimeId);
		assertEquals("expects only one runtime type", runtimeTypes.length, 1);
		IRuntimeType runtimeType = runtimeTypes[0];
		IRuntimeWorkingCopy runtimeWC = runtimeType.createRuntime(null, new NullProgressMonitor());
		runtimeWC.setName(runtimeId);
		runtimeWC.setLocation(new Path(homeDir));
		((RuntimeWorkingCopy) runtimeWC).setAttribute(
				IJBossServerRuntime.PROPERTY_VM_ID, VM_INSTALL.getId());
		((RuntimeWorkingCopy) runtimeWC).setAttribute(
				IJBossServerRuntime.PROPERTY_VM_TYPE_ID, VM_INSTALL
						.getVMInstallType().getId());
		((RuntimeWorkingCopy) runtimeWC).setAttribute(
				IJBossServerRuntime.PROPERTY_CONFIGURATION_NAME, config);

		IRuntime savedRuntime = runtimeWC.save(true, new NullProgressMonitor());
		return savedRuntime;
	}
	
	public class StatusWrapper {
		protected IStatus status;
		public IStatus getStatus() { return this.status; }
		public void setStatus(IStatus s) { this.status = s; }
	}
	
	public void test32() {
		startup();
		shutdown();
	}
	
	public void test40() {
		startup();
		shutdown();
	}
	
	public void test42() {
		startup();
		shutdown();
	}

	protected class ServerStateListener implements IServerListener {
		private ArrayList stateChanges;
		public ServerStateListener() {
			this.stateChanges = new ArrayList();
		}
		public ArrayList getStateChanges() {
			return stateChanges;
		}
		public void serverChanged(ServerEvent event) {
			if((event.getKind() & ServerEvent.SERVER_CHANGE) != 0)  {
				if((event.getKind() & ServerEvent.STATE_CHANGE) != 0) {
					if( event.getState() != IServer.STATE_STOPPED)
						stateChanges.add(new Integer(event.getState()));
				}
			}
		}
	}
	
	protected class ErrorStreamListener implements IStreamListener {
		protected boolean errorFound = false;
		String entireLog = "";
		public void streamAppended(String text, IStreamMonitor monitor) {
			entireLog += text;
		} 
		
		// will need to be fixed or decided how to figure out errors
		public boolean hasError() {
			return errorFound;
		}
	}

	protected void startup() { startup(DEFAULT_STARTUP_TIME); }
	protected void startup(int maxWait) {
		long finishTime = new Date().getTime() + maxWait;
		
		// operation listener, which is only alerted when the startup is *done*
		final StatusWrapper opWrapper = new StatusWrapper();
		final IOperationListener listener = new IOperationListener() {
			public void done(IStatus result) {
				opWrapper.setStatus(result);
			} };
			
			
		// a stream listener to listen for errors
		ErrorStreamListener streamListener = new ErrorStreamListener();
		
		// the thread to actually start the server
		Thread startThread = new Thread() { 
			public void run() {
				currentServer.start(ILaunchManager.RUN_MODE, listener);
			}
		};
		
		startThread.start();
		
		boolean addedStream = false;
		while( finishTime > new Date().getTime() && opWrapper.getStatus() == null) {
			// we're waiting for startup to finish
			if( !addedStream ) {
				IStreamMonitor mon = getStreamMonitor();
				if( mon != null ) {
					mon.addListener(streamListener);
					addedStream = true;
				}
			}
			try {
				Display.getDefault().readAndDispatch();
			} catch( SWTException swte ) {}
		}
		
		try {
			assertTrue("Startup has taken longer than what is expected for a default startup", finishTime >= new Date().getTime());
			assertNotNull("Startup never finished", opWrapper.getStatus());
			assertFalse("Startup had System.error output", streamListener.hasError());
		} catch( AssertionFailedError afe ) {
			// cleanup
			currentServer.stop(true);
			// rethrow
			throw afe;
		}
		getStreamMonitor().removeListener(streamListener);
	}

	
	protected void shutdown() { shutdown(DEFAULT_SHUTDOWN_TIME); }
	protected void shutdown(int maxWait) {
		long finishTime = new Date().getTime() + maxWait;
		
		// operation listener, which is only alerted when the startup is *done*
		final StatusWrapper opWrapper = new StatusWrapper();
		final IOperationListener listener = new IOperationListener() {
			public void done(IStatus result) {
				opWrapper.setStatus(result);
			} };
			
			
		// a stream listener to listen for errors
		ErrorStreamListener streamListener = new ErrorStreamListener();
		if( getStreamMonitor() != null ) 
			getStreamMonitor().addListener(streamListener);
		
		// the thread to actually start the server
		Thread stopThread = new Thread() { 
			public void run() {
				currentServer.stop(false, listener);
			}
		};
		
		stopThread.start();
		
		while( finishTime > new Date().getTime() && opWrapper.getStatus() == null) {
			// we're waiting for startup to finish
			try {
				Display.getDefault().readAndDispatch();
			} catch( SWTException swte ) {}
		}
		
		try {
			assertTrue("Startup has taken longer than what is expected for a default startup", finishTime >= new Date().getTime());
			assertNotNull("Startup never finished", opWrapper.getStatus());
			assertFalse("Startup had System.error output", streamListener.hasError());
		} catch( AssertionFailedError afe ) {
			// cleanup
			currentServer.stop(true);
			// rethrow
			throw afe;
		}
	}
		
	protected IStreamMonitor getStreamMonitor() {
		JBossServerBehavior behavior = 
			(JBossServerBehavior)currentServer.loadAdapter(JBossServerBehavior.class, null);
		if( behavior != null ) {
			if( behavior.getProcess() != null ) {
				return behavior.getProcess().getStreamsProxy().getOutputStreamMonitor();
			}
		}
		return null;
	}
}
