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
package org.jboss.ide.eclipse.as.core.server.internal.v7;

import org.eclipse.wst.server.core.IServer;
import org.jboss.ide.eclipse.as.core.JBossServerCorePlugin;
import org.jboss.ide.eclipse.as.core.server.IJBoss7ManagerService;
import org.osgi.framework.BundleContext;

public class JBoss7ManagerUtil {
	
	public static final String SERVICE_VERSION_70 = "org.jboss.ide.eclipse.as.management.as7.service"; //$NON-NLS-1$

	public static IJBoss7ManagerService findManagementService(IServer server) throws Exception {
		BundleContext context = JBossServerCorePlugin.getContext();
		JBoss7ManagerServiceProxy proxy = new JBoss7ManagerServiceProxy(context, getRequiredVersion(server));
		proxy.open();
		return proxy;
	}

	private static String getRequiredVersion(IServer server) {
		return IJBoss7ManagerService.AS_VERSION_700;
	}
}
