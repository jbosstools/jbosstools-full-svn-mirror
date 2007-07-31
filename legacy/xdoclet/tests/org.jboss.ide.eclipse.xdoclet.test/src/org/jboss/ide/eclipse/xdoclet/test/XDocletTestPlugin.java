package org.jboss.ide.eclipse.xdoclet.test;

import org.jboss.ide.eclipse.core.test.CoreTestPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class XDocletTestPlugin extends CoreTestPlugin {

	//The shared instance.
	private static XDocletTestPlugin plugin;
	
	/**
	 * The constructor.
	 */
	public XDocletTestPlugin() {
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
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static XDocletTestPlugin getDefault() {
		return plugin;
	}

}
