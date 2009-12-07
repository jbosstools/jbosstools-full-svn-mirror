/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IStartup;
import org.jboss.tools.common.log.BaseUIPlugin;
import org.jboss.tools.common.log.IPluginLog;
import org.jboss.tools.common.reporting.ProblemReportingHelper;
import org.jboss.tools.vpe.xulrunner.browser.XulRunnerBrowser;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class VpePlugin extends BaseUIPlugin implements IStartup {
	public static final String PLUGIN_ID = "org.jboss.tools.vpe"; //$NON-NLS-1$
	
	public static final String EXTESION_POINT_VPE_TEMPLATES = "org.jboss.tools.vpe.templates"; //$NON-NLS-1$
	public static final String EXTESION_POINT_LOCALE_PROVIDER = "org.jboss.tools.vpe.localeProvider"; //$NON-NLS-1$
	
	//The shared instance.
	private static VpePlugin plugin;

	
	/**
	 * The constructor.
	 */
	public VpePlugin() {
		super();
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static VpePlugin getDefault() {
		return plugin;
	}

	public static void reportProblem(Exception throwable) {
		if (VpeDebug.USE_PRINT_STACK_TRACE) {
			throwable.printStackTrace();
		} 
		ProblemReportingHelper.reportProblem(PLUGIN_ID, throwable);
	}
	
	public String getResourcePath(String resourceName) {
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		
		if (bundle != null) {
			URL url = bundle.getEntry(resourceName);
			
			if (url != null) {
				try {
					return FileLocator.resolve(url).getPath();
				} catch (IOException ioe) {
					logError(ioe);
				}
			}
		}
		
		return null;
	}
	
	public static IPluginLog getPluginLog() {
		return getDefault();
	}

	public void earlyStartup() {
		/* init xulrunner path for */ 
		try {
			String xulRunnerPath = XulRunnerBrowser.getXulRunnerPath();
			if ("true".equals(Platform.getDebugOption(PLUGIN_ID + "/debug/earlyStartup"))) { //$NON-NLS-1$ //$NON-NLS-2$
				logInfo("earlyStartup: XULRunner path is: " + xulRunnerPath); //$NON-NLS-1$
			}
		} catch (Throwable t) {
			// Ignore this. Will catch it when use Visual Editor 
		}
	}
	
	public static String getPluginResourcePath() {
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		URL url = null;
		try {
			url = bundle == null ? null : FileLocator.resolve(bundle.getEntry("/ve")); //$NON-NLS-1$
		} catch (IOException e) {
			VpePlugin.getPluginLog().logError(e);
		}
		return (url == null) ? null : url.getPath();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.log.BaseUIPlugin#getId()
	 */
	@Override
	public String getId() {
		return PLUGIN_ID;
	}
}