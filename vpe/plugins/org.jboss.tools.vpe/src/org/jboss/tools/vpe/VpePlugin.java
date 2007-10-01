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

import org.eclipse.core.runtime.Platform;
import org.jboss.tools.common.log.BaseUIPlugin;
import org.jboss.tools.common.log.IPluginLog;
import org.jboss.tools.common.reporting.ProblemReportingHelper;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class VpePlugin extends BaseUIPlugin {
	public final static String PLUGIN_ID = "org.jboss.tools.vpe";
	//The shared instance.
	private static VpePlugin plugin;
	//Resource bundle.
	
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
		if (plugin == null) {
			// plugin will be initialized in constructor
			Platform.getBundle(PLUGIN_ID);
		}
		
		return plugin;
	}

//	/**
//	 * Returns the string from the plugin's resource bundle,
//	 * or 'key' if not found.
//	 */
//	public static String getResourceString(String key) {
//		ResourceBundle bundle = VpePlugin.getDefault().getResourceBundle();
//		try {
//			return (bundle != null) ? bundle.getString(key) : key;
//		} catch (MissingResourceException e) {
//			VpePlugin.getPluginLog()
//					.logError("Resource " + key + " is missing.", e);
//			return key;
//		}
//	}

//	/**
//	 * Returns the plugin's resource bundle,
//	 */
//	public ResourceBundle getResourceBundle() {
//		return resourceBundle;
//	}

	public static void reportProblem(Exception throwable) {
		if (VpeDebug.usePrintStackTrace) {
			throwable.printStackTrace();
		} else {
			ProblemReportingHelper.reportProblem(PLUGIN_ID, throwable);
		}
	}
	
	public static IPluginLog getPluginLog() {
		return getDefault();
	}
}
