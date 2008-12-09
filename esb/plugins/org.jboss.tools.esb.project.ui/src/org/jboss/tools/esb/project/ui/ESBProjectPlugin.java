package org.jboss.tools.esb.project.ui;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ESBProjectPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.esb.project.ui";

	// The shared instance
	private static ESBProjectPlugin plugin;
	
	/**
	 * The constructor
	 */
	public ESBProjectPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		Image img = imageDescriptorFromPlugin(PLUGIN_ID, "icons/wizards/esb_runtime.gif").createImage();
		getImageRegistry().put("esb_runtime", img);
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
	public static ESBProjectPlugin getDefault() {
		return plugin;
	}

}
