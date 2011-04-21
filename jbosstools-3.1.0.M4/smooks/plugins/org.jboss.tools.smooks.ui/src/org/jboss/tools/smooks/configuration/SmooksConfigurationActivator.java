package org.jboss.tools.smooks.configuration;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class SmooksConfigurationActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.smooks.ui";

	// The shared instance
	private static SmooksConfigurationActivator plugin;

	/**
	 * The constructor
	 */
	public SmooksConfigurationActivator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
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
	public static SmooksConfigurationActivator getDefault() {
		return plugin;
	}

	public void log(Throwable e) {
		getLog().log(new Status(Status.ERROR, PLUGIN_ID, Status.ERROR, "Error", e));
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
		reg.put(GraphicsConstants.JAVA_PROPERTY_ICON, getImageDescriptor(GraphicsConstants.JAVA_PROPERTY_ICON_PATH));

		reg.put(GraphicsConstants.IMAGE_UNKNOWN_OBJ, imageDescriptorFromPlugin(PLUGIN_ID,
				GraphicsConstants.IMAGE_UNKNOWN_OBJ));

		reg.put(GraphicsConstants.IMAGE_INPUT_DATA_HEADER, imageDescriptorFromPlugin(PLUGIN_ID,
				GraphicsConstants.IMAGE_INPUT_DATA_HEADER));

		// regist ovr images
		reg.put(GraphicsConstants.IMAGE_OVR_ERROR, getImageDescriptor(GraphicsConstants.IMAGE_OVR_ERROR));
		reg.put(GraphicsConstants.IMAGE_OVR_WARING, getImageDescriptor(GraphicsConstants.IMAGE_OVR_WARING));

		// regist
		reg.put(GraphicsConstants.IMAGE_EMPTY, getImageDescriptor(GraphicsConstants.IMAGE_PATH_BLANK));
		reg.put(GraphicsConstants.IMAGE_ERROR, getImageDescriptor(GraphicsConstants.IMAGE_PATH_ERROR));
		reg.put(GraphicsConstants.IMAGE_WARNING, getImageDescriptor(GraphicsConstants.IMAGE_PATH_WARNING));

		// regist java images
		reg.put(GraphicsConstants.IMAGE_JAVA_BEAN, imageDescriptorFromPlugin(PLUGIN_ID,
				GraphicsConstants.IMAGE_JAVA_BEAN));
		reg.put(GraphicsConstants.IMAGE_JAVA_INTERFACE, imageDescriptorFromPlugin(PLUGIN_ID,
				GraphicsConstants.IMAGE_JAVA_INTERFACE));
		reg.put(GraphicsConstants.IMAGE_JAVA_ATTRIBUTE, imageDescriptorFromPlugin(PLUGIN_ID,
				GraphicsConstants.IMAGE_JAVA_ATTRIBUTE));
		reg.put(GraphicsConstants.IMAGE_CHECKBOX_CHECK, imageDescriptorFromPlugin(PLUGIN_ID,
				GraphicsConstants.IMAGE_CHECKBOX_CHECK));
		reg.put(GraphicsConstants.IMAGE_CHECKBOX_UNCHECK, imageDescriptorFromPlugin(PLUGIN_ID,
				GraphicsConstants.IMAGE_CHECKBOX_UNCHECK));
		reg.put(GraphicsConstants.IMAGE_JAVA_OBJECT, imageDescriptorFromPlugin(PLUGIN_ID,
				GraphicsConstants.IMAGE_JAVA_OBJECT));
		reg.put(GraphicsConstants.IMAGE_PACKAGE_OBJECT, imageDescriptorFromPlugin(PLUGIN_ID,
				GraphicsConstants.IMAGE_PACKAGE_OBJECT));
		reg.put(GraphicsConstants.IMAGE_JAVA_ARRAY, imageDescriptorFromPlugin(PLUGIN_ID,
				GraphicsConstants.IMAGE_JAVA_ARRAY));
		reg.put(GraphicsConstants.IMAGE_JAVA_COLLECTION, imageDescriptorFromPlugin(PLUGIN_ID,
				GraphicsConstants.IMAGE_JAVA_COLLECTION));
		reg.put(GraphicsConstants.IMAGE_JAVA_FILE, imageDescriptorFromPlugin(PLUGIN_ID,
				GraphicsConstants.IMAGE_JAVA_FILE));

		// for the xml2xml line
		reg.put(GraphicsConstants.IMAGE_XML_FILE,
				imageDescriptorFromPlugin(PLUGIN_ID, GraphicsConstants.IMAGE_XML_FILE));

		reg.put(GraphicsConstants.IMAGE_XSD_FILE,
				imageDescriptorFromPlugin(PLUGIN_ID, GraphicsConstants.IMAGE_XSD_FILE));

		reg.put(GraphicsConstants.IMAGE_BINDING_LINE, imageDescriptorFromPlugin(PLUGIN_ID,
				GraphicsConstants.IMAGE_BINDING_LINE));

		reg.put(GraphicsConstants.IMAGE_MAPPING_LINE, imageDescriptorFromPlugin(PLUGIN_ID,
				GraphicsConstants.IMAGE_MAPPING_LINE));
		reg.put(GraphicsConstants.IMAGE_XML_ATTRIBUTE, imageDescriptorFromPlugin(PLUGIN_ID,
				"icons/full/obj16/attribute_obj.gif"));
		reg.put(GraphicsConstants.IMAGE_XML_ELEMENT, imageDescriptorFromPlugin(PLUGIN_ID,
				"icons/full/obj16/element_obj.gif"));
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
