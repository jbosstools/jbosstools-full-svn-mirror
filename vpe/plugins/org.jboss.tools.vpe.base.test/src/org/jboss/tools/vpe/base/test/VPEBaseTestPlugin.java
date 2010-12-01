/******************************************************************************* 
* Copyright (c) 2007 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*     Red Hat, Inc. - initial API and implementation
******************************************************************************/
package org.jboss.tools.vpe.base.test;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.common.log.BaseUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class VPEBaseTestPlugin extends BaseUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.vpe.base.test";
	
	public static final String VPE_TEST_EXTENTION_POINT_ID = "org.jboss.tools.vpe.ui.test"; //$NON-NLS-1$

	// The shared instance
	private static VPEBaseTestPlugin plugin;
	
	/**
	 * The constructor
	 */
	public VPEBaseTestPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static VPEBaseTestPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns all extensions of {@value #VPE_TEST_EXTENTION_POINT_ID}
	 */
	public IExtension[] getVpeTestExtensions() {
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extensionRegistry
				.getExtensionPoint(VPE_TEST_EXTENTION_POINT_ID);
		IExtension[] extensions = extensionPoint.getExtensions();
		return extensions;
	}
}
