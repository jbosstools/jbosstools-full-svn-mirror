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

import java.util.HashMap;

import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.wst.server.core.IRuntime;

/**
 * 
 * @author Rob Stryker
 */
public interface IJBossServerRuntime {
	public static String PROPERTY_VM_ID = "PROPERTY_VM_ID"; //$NON-NLS-1$
	public static String PROPERTY_VM_TYPE_ID = "PROPERTY_VM_TYPE_ID"; //$NON-NLS-1$
	
	public static String PROPERTY_CONFIGURATION_NAME = "org.jboss.ide.eclipse.as.core.runtime.configurationName"; //$NON-NLS-1$

	public IRuntime getRuntime();
	public IVMInstall getVM();
	public void setVM(IVMInstall install);
	public String getJBossConfiguration();
	public void setJBossConfiguration(String config);
	
	// for startup
	public String getDefaultRunArgs();
	public String getDefaultRunVMArgs();
	public HashMap<String, String> getDefaultRunEnvVars();
	public boolean isUsingDefaultJRE();
}
