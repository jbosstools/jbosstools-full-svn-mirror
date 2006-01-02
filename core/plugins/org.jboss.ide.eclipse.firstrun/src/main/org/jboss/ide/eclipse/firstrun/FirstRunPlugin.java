package org.jboss.ide.eclipse.firstrun;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class FirstRunPlugin extends AbstractUIPlugin {

	//The shared instance.
	private static FirstRunPlugin plugin;
	
	public static final String FIRST_RUN_PROPERTY = "org.jboss.ide.eclipse.firstrun";
	public static final String ICON_JBOSSIDE_LOGO = "icons/jbosside-logo.png";
	
	/**
	 * The constructor.
	 */
	public FirstRunPlugin() {
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
	public static FirstRunPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("org.jboss.ide.eclipse.firstrun", path);
	}
}
