package org.jboss.tools.smooks.javabean;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jboss.tools.smooks.javabean.ui.JavaImageConstants;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class JavaBeanActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.smooks.javabean";

	public static final String DATA_TYPE_ID_JAVABEAN = "org.jboss.tools.smooks.ui.viewerInitor.javabean";

	// The shared instance
	private static JavaBeanActivator plugin;

	/**
	 * The constructor
	 */
	public JavaBeanActivator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
		reg.put(JavaImageConstants.IMAGE_JAVA_ATTRIBUTE,
				imageDescriptorFromPlugin(PLUGIN_ID, "icons/obj16/att_obj.gif"));
		reg.put(JavaImageConstants.IMAGE_JAVA_OBJECT,
				imageDescriptorFromPlugin(PLUGIN_ID, "icons/obj16/class_obj.gif"));
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static JavaBeanActivator getDefault() {
		return plugin;
	}

}
