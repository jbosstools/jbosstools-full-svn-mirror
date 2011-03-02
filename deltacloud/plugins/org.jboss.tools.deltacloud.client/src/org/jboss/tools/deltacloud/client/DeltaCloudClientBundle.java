package org.jboss.tools.deltacloud.client;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class DeltaCloudClientBundle extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.deltacloud.client"; //$NON-NLS-1$

	// The shared instance
	private static DeltaCloudClientBundle plugin;
	
	/**
	 * The constructor
	 */
	public DeltaCloudClientBundle() {
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
	public static DeltaCloudClientBundle getDefault() {
		return plugin;
	}

}
