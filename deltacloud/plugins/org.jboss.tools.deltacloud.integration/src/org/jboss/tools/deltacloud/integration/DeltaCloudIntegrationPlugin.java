package org.jboss.tools.deltacloud.integration;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class DeltaCloudIntegrationPlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.deltacloud.integration";

	// The shared instance
	private static DeltaCloudIntegrationPlugin plugin;
	
	/**
	 * The constructor
	 */
	public DeltaCloudIntegrationPlugin() {
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
	public static DeltaCloudIntegrationPlugin getDefault() {
		return plugin;
	}

}
