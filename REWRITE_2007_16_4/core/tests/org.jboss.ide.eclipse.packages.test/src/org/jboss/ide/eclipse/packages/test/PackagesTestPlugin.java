package org.jboss.ide.eclipse.packages.test;

import org.jboss.ide.eclipse.core.test.CoreTestPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class PackagesTestPlugin extends CoreTestPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.ide.eclipse.packages.test";

	// The shared instance
	private static PackagesTestPlugin plugin;
	
	/**
	 * The constructor
	 */
	public PackagesTestPlugin() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
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
	public static PackagesTestPlugin getDefault() {
		return plugin;
	}

}
