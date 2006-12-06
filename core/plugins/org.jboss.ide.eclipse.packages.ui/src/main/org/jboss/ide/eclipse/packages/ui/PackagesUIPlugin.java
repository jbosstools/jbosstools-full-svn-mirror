package org.jboss.ide.eclipse.packages.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class PackagesUIPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.ide.eclipse.packages.ui";
	
	// image ids
	public static final String IMG_PACKAGE = "icons/jar_obj.gif";
	public static final String IMG_PACKAGE_EXPLODED = "icons/jar_exploded_obj.gif";
	public static final String IMG_EXTERNAL_FILE = "icons/ext_file_obj.gif";
	public static final String IMG_EXTERNAL_FOLDER = "icons/ext_folder_obj.gif";
	public static final String IMG_INCLUDES = "icons/includes.gif";
	public static final String IMG_EXCLUDES = "icons/excludes.gif";
	public static final String IMG_NEW_PACKAGE = "icons/new_package.gif";
	public static final String IMG_NEW_JAR_WIZARD = "icons/new_jar_wiz.png";
	public static final String IMG_SINGLE_FILE = "icons/single_file.gif";
	public static final String IMG_MULTIPLE_FILES = "icons/multiple_files.gif";
	public static final String IMG_COLLAPSE_ALL = "icons/collapseall.gif";
	public static final String IMG_PACKAGE_EDIT = "icons/jar_src_obj.gif";
	public static final String IMG_WAR = "icons/war.gif";
	public static final String IMG_EAR = "icons/ear.gif";
	public static final String IMG_EJB_JAR = "icons/EJBJar.gif";
	public static final String IMG_NEW_WAR_WIZARD="icons/new_war_wiz.png";
	public static final String IMG_NEW_EAR_WIZARD="icons/ear-wiz-banner.gif";
	public static final String IMG_BUILD_PACKAGES = "icons/build_packages.gif";
	
	// The shared instance
	private static PackagesUIPlugin plugin;
	
	/**
	 * The constructor
	 */
	public PackagesUIPlugin() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
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
	public static PackagesUIPlugin getDefault() {
		return plugin;
	}

	protected void initializeImageRegistry(ImageRegistry registry) {
		registerImages(registry, new String[] {
			IMG_PACKAGE, IMG_PACKAGE_EXPLODED, IMG_EXTERNAL_FILE,
			IMG_EXTERNAL_FOLDER, IMG_INCLUDES, IMG_EXCLUDES,
			IMG_NEW_PACKAGE, IMG_NEW_JAR_WIZARD, IMG_SINGLE_FILE,
			IMG_MULTIPLE_FILES, IMG_COLLAPSE_ALL, IMG_PACKAGE_EDIT,
			IMG_EAR, IMG_EJB_JAR, IMG_WAR, IMG_NEW_EAR_WIZARD,
			IMG_NEW_WAR_WIZARD, IMG_BUILD_PACKAGES
		});
	}
	
	private void registerImages (ImageRegistry reg, String ids[])
	{
		for (int i = 0; i < ids.length; i++)
			reg.put(ids[i], imageDescriptorFromPlugin(PLUGIN_ID, ids[i]));
	}
	
	// helper methods
	public static Image getImage (String id)
	{
		return getDefault().getImageRegistry().get(id);
	}
	
	public static ImageDescriptor getImageDescriptor (String id)
	{
		return getDefault().getImageRegistry().getDescriptor(id);
	}
}
