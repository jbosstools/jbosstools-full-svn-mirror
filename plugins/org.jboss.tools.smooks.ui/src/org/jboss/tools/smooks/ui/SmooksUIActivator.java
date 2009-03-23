package org.jboss.tools.smooks.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jboss.tools.smooks.javabean.ui.JavaImageConstants;
import org.jboss.tools.smooks.utils.SmooksGraphConstants;
import org.jboss.tools.smooks.xml.XMLImageConstants;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class SmooksUIActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.smooks.ui";
	

	public static final String TYPE_ID_XSD = "org.jboss.tools.smooks.xml.viewerInitor.xsd";

	public static final String TYPE_ID_XML = "org.jboss.tools.smooks.xml.viewerInitor.xml";

	public static final String DATA_TYPE_ID_JAVABEAN = "org.jboss.tools.smooks.ui.viewerInitor.javabean";


	// The shared instance
	private static SmooksUIActivator plugin;
	
	/**
	 * The constructor
	 */
	public SmooksUIActivator() {
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

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
		reg.put(SmooksGraphConstants.IMAGE_EMPTY, getImageDescriptor(SmooksGraphConstants.IMAGE_PATH_BLANK));
		reg.put(SmooksGraphConstants.IMAGE_ERROR, getImageDescriptor(SmooksGraphConstants.IMAGE_PATH_ERROR));
		reg.put(SmooksGraphConstants.IMAGE_WARNING, getImageDescriptor(SmooksGraphConstants.IMAGE_PATH_WARNING));
		reg.put(XMLImageConstants.IMAGE_XML_ATTRIBUTE,
				imageDescriptorFromPlugin(PLUGIN_ID,
						"icons/full/obj16/attribute_obj.gif"));
		reg.put(XMLImageConstants.IMAGE_XML_ELEMENT,
				imageDescriptorFromPlugin(PLUGIN_ID,
						"icons/full/obj16/element_obj.gif"));
		
		// regist java images
		reg.put(JavaImageConstants.IMAGE_JAVA_INTERFACE,
				imageDescriptorFromPlugin(PLUGIN_ID, JavaImageConstants.IMAGE_JAVA_INTERFACE));
		reg.put(JavaImageConstants.IMAGE_JAVA_ATTRIBUTE,
				imageDescriptorFromPlugin(PLUGIN_ID, JavaImageConstants.IMAGE_JAVA_ATTRIBUTE));
		reg.put(JavaImageConstants.IMAGE_CHECKBOX_CHECK,
				imageDescriptorFromPlugin(PLUGIN_ID, JavaImageConstants.IMAGE_CHECKBOX_CHECK));
		reg.put(JavaImageConstants.IMAGE_CHECKBOX_UNCHECK,
				imageDescriptorFromPlugin(PLUGIN_ID, JavaImageConstants.IMAGE_CHECKBOX_UNCHECK));
		reg.put(JavaImageConstants.IMAGE_JAVA_OBJECT,
				imageDescriptorFromPlugin(PLUGIN_ID, "icons/full/obj16/class_obj.gif"));
		reg.put(JavaImageConstants.IMAGE_JAVA_ARRAY,
				imageDescriptorFromPlugin(PLUGIN_ID, JavaImageConstants.IMAGE_JAVA_ARRAY));
		reg.put(JavaImageConstants.IMAGE_JAVA_COLLECTION,
				imageDescriptorFromPlugin(PLUGIN_ID, JavaImageConstants.IMAGE_JAVA_COLLECTION));
		
		// for the xml2xml line
		reg.put(XMLImageConstants.IMAGE_BINDING_LINE,
				imageDescriptorFromPlugin(PLUGIN_ID,XMLImageConstants.IMAGE_BINDING_LINE));
		
		reg.put(XMLImageConstants.IMAGE_MAPPING_LINE,
				imageDescriptorFromPlugin(PLUGIN_ID, XMLImageConstants.IMAGE_MAPPING_LINE));
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static SmooksUIActivator getDefault() {
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
}
