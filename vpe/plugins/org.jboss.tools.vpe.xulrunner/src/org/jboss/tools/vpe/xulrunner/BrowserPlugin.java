package org.jboss.tools.vpe.xulrunner;

import java.text.MessageFormat;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.common.log.BaseUIPlugin;
import org.jboss.tools.common.log.IPluginLog;
import org.jboss.tools.vpe.xulrunner.browser.XulRunnerBrowser;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class BrowserPlugin extends BaseUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.vpe.xulrunner"; //$NON-NLS-1$
	
	private static final String TRUE_STRING = "true";  //$NON-NLS-1$
	
	public static final boolean DEBUG_BROWSERSTART;
	public static final boolean PRINT_ELEMENT_BOUNDS;
	// The shared instance
	private static BrowserPlugin plugin;
	
	static {
		DEBUG_BROWSERSTART = "true".equals(Platform.getDebugOption(PLUGIN_ID + "/debug/browser_start"));  //$NON-NLS-1$  //$NON-NLS-2$
		PRINT_ELEMENT_BOUNDS = TRUE_STRING.equals(
				Platform.getDebugOption(BrowserPlugin.PLUGIN_ID + "/debug/PrintElementBounds")); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * The constructor
	 */
	public BrowserPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		// required to be here to fix tycho test execution errors
		earlyStartup();
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
	public static BrowserPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public static IPluginLog getPluginLog() {
		return getDefault();
	}
	
	public void earlyStartup() {
		try {
			/* init xulrunner path  */ 
			XulRunnerBrowser.getXulRunnerPath();
//			String xulRunnerPath = XulRunnerBrowser.getXulRunnerPath();
//			if ("true".equals(Platform.getDebugOption(PLUGIN_ID + "/debug/earlyStartup"))) { //$NON-NLS-1$ //$NON-NLS-2$
//				logInfo(MessageFormat.format("earlyStartup: XULRunner path is: {0}",xulRunnerPath)); //$NON-NLS-1$
//			}
		} catch (XulRunnerException e) {
			logError(e);
		}
	}
}