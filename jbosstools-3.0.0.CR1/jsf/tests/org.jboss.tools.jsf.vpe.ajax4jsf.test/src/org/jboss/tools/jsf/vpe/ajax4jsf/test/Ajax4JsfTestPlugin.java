package org.jboss.tools.jsf.vpe.ajax4jsf.test;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Ajax4JsfTestPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.jsf.vpe.ajax4jsf.test";

	// The shared instance
	private static Ajax4JsfTestPlugin plugin;
	
	/**
	 * The constructor
	 */
	public Ajax4JsfTestPlugin() {
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
	public static Ajax4JsfTestPlugin getDefault() {
		return plugin;
	}

	public static String getPluginResourcePath() {
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		URL url = null;
		try {
			url = bundle == null ? null : FileLocator.resolve(bundle
					.getEntry("/resources")); // $NON-NLS-1$
		} catch (Exception e) {
			url = bundle.getEntry("/resources"); // $NON-NLS-1$
		}
		return (url == null) ? null : url.getPath();
	}
}
