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
package org.jboss.ide.eclipse.as.internal.management.as7;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.as.management.as7.IJBoss7DeploymentResult;
import org.jboss.ide.eclipse.as.management.as7.IJBoss7ManagerService;
import org.jboss.ide.eclipse.as.management.as7.JBoss7DeploymentState;
import org.jboss.ide.eclipse.as.management.as7.JBoss7ServerState;

/**
 * @author Rob Stryker
 */
public class JBoss7ManagerService implements IJBoss7ManagerService {

	public IJBoss7DeploymentResult deployAsync(String host, int port, String deploymentName,
			File file, IProgressMonitor monitor) throws Exception {
		AS7Manager manager = new AS7Manager(host, port);
		return manager.deploy(deploymentName, file);
	}

	public IJBoss7DeploymentResult deploySync(String host, int port, String deploymentName,
			File file, IProgressMonitor monitor) throws Exception {
		AS7Manager manager = new AS7Manager(host, port);
		return manager.deploySync(deploymentName, file, monitor);
	}

	public IJBoss7DeploymentResult undeployAsync(String host, int port, String deploymentName,
			boolean removeFile, IProgressMonitor monitor) throws Exception {
		AS7Manager manager = new AS7Manager(host, port);
		return manager.undeploy(deploymentName);
	}

	public IJBoss7DeploymentResult syncUndeploy(String host, int port, String deploymentName,
			boolean removeFile, IProgressMonitor monitor) throws Exception {
		AS7Manager manager = new AS7Manager(host, port);
		return manager.undeploySync(deploymentName, monitor);
	}

	public JBoss7DeploymentState getDeploymentState(String host, int port, String deploymentName) throws Exception {
		AS7Manager manager = new AS7Manager(host, port);
		return manager.getDeploymentState(deploymentName);
	}
	
	@Deprecated
	public JBoss7ServerState getServerState(String host) throws Exception {
		return getServerState(host, AS7Manager.MGMT_PORT);
	}

	public JBoss7ServerState getServerState(String host, int port) throws Exception {
		AS7Manager manager = new AS7Manager(host, port);
		return manager.getServerState();
	}

	public boolean isRunning(String host, int port) throws Exception {
		return new AS7Manager(host, port).isRunning();
	}

	@Deprecated
	public void stop(String host) throws Exception {
		stop(host, AS7Manager.MGMT_PORT);
	}
	
	public void stop(String host, int port) throws Exception {
		new AS7Manager(host, port).stopServer();
	}

	@Override
	public void dispose() {
	}
}
