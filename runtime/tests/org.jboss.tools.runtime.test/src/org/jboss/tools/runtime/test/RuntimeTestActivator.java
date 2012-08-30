package org.jboss.tools.runtime.test;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class RuntimeTestActivator extends Plugin {

	// The shared instance
	private static RuntimeTestActivator plugin;
	
	
	/**
	 * The constructor
	 */
	public RuntimeTestActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
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
	public static RuntimeTestActivator getDefault() {
		return plugin;
	}

}
