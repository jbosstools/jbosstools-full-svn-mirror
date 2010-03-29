/*******************************************************************************
  * Copyright (c) 2008 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/

package org.hibernate.mediator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class MediatorPlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.hibernate.mediator"; //$NON-NLS-1$

	// The shared instance
	private static MediatorPlugin plugin;
	
	/**
	 * The constructor
	 */
	public MediatorPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		setPlugin(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		setPlugin(null);
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static MediatorPlugin getDefault() {
		return plugin;
	}

	private static void setPlugin(MediatorPlugin plugin) {
		MediatorPlugin.plugin = plugin;
	}
	
	/**
	 * Log message
	 *
	 */
	private static void log(int severity, String message, Throwable e) {
		getDefault().getLog().log(new Status(severity, PLUGIN_ID, message, e));
	}
	
	/**
	 * Short exception log
	 *
	 */
	public static void logException(Throwable e) {
		log(IStatus.ERROR, e.getMessage(),  e);
	}
	
	/**
	 * Short error log call
	 *
	 */
	public static void logError(String message) {
		log(IStatus.ERROR, message, null);
	}
	
	/**
	 * Short warning log call
	 *
	 */
	public static void logWarning(String message) {
		log(IStatus.WARNING, message, null);
	}
	
	/**
	 * Short information log call
	 *
	 */
	public static void logInfo(String message) {
		log(IStatus.INFO, message, null);
	}
	
	

}
