package org.jboss.tools.smooks.xml;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class XmlActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.smooks.xml";

	public static final String TYPE_ID_XSD = "org.jboss.tools.smooks.xml.viewerInitor.xsd";

	public static final String TYPE_ID_XML = "org.jboss.tools.smooks.xml.viewerInitor.xml";

	// The shared instance
	private static XmlActivator plugin;

	/**
	 * The constructor
	 */
	public XmlActivator() {
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
		reg.put(XMLImageConstants.IMAGE_XML_ATTRIBUTE,
				imageDescriptorFromPlugin(PLUGIN_ID,
						"icons/obj16/attribute_obj.gif"));
		reg.put(XMLImageConstants.IMAGE_XML_ELEMENT,
				imageDescriptorFromPlugin(PLUGIN_ID,
						"icons/obj16/element_obj.gif"));
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static XmlActivator getDefault() {
		return plugin;
	}

}
