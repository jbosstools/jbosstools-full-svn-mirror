package org.jboss.tools.ws.creation.core;

import org.jboss.tools.common.log.BaseUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class JBossWSCreationCorePlugin extends BaseUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.ws.creation.core"; //$NON-NLS-1$

	// The shared instance
	private static JBossWSCreationCorePlugin plugin;
	
	private long generateTime;
	
	/**
	 * The constructor
	 */
	public JBossWSCreationCorePlugin() {
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
	public static JBossWSCreationCorePlugin getDefault() {
		return plugin;
	}
	
	public long getGenerateTime(){
		return generateTime;
	}

	public void setGenerateTime(long generateTime) {
		this.generateTime = generateTime;
	}

}
