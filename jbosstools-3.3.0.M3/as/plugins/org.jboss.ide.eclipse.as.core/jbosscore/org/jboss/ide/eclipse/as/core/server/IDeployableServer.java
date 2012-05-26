/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 

package org.jboss.ide.eclipse.as.core.server;

import org.eclipse.wst.server.core.IServer;
import org.jboss.ide.eclipse.as.core.server.internal.ServerAttributeHelper;

/**
 *
 * @author rob.stryker@jboss.com
 */
public interface IDeployableServer {
	public static final String SERVER_MODE = "org.jboss.ide.eclipse.as.core.server.serverMode"; //$NON-NLS-1$
	public static final String DEPLOY_DIRECTORY = "org.jboss.ide.eclipse.as.core.server.deployDirectory"; //$NON-NLS-1$
	public static final String TEMP_DEPLOY_DIRECTORY = "org.jboss.ide.eclipse.as.core.server.tempDeployDirectory"; //$NON-NLS-1$
	public static final String DEPLOY_DIRECTORY_TYPE = "org.jboss.ide.eclipse.as.core.server.deployDirectoryType"; //$NON-NLS-1$
	public static final String ZIP_DEPLOYMENTS_PREF = "org.jboss.ide.eclipse.as.core.server.zipDeploymentsPreference"; //$NON-NLS-1$

	public static final String DEPLOY_METADATA = "metadata"; //$NON-NLS-1$
	public static final String DEPLOY_CUSTOM = "custom"; //$NON-NLS-1$
	public static final String DEPLOY_SERVER = "server"; //$NON-NLS-1$
	
	public String getDeployFolder();
	public void setDeployFolder(String folder);
	public String getTempDeployFolder();
	public void setTempDeployFolder(String folder);
	public String getDeployLocationType();
	public void setDeployLocationType(String type);
	public boolean zipsWTPDeployments();
	public void setZipWTPDeployments(boolean val);
	
	
	public String getConfigDirectory();
	public ServerAttributeHelper getAttributeHelper();
	public IServer getServer();
	
	public boolean hasJMXProvider();
}
