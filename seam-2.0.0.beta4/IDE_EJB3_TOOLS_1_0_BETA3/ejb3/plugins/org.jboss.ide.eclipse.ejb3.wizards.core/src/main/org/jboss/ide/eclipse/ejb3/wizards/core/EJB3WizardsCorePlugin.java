package org.jboss.ide.eclipse.ejb3.wizards.core;

import java.io.IOException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class EJB3WizardsCorePlugin extends Plugin {
	//The shared instance.
	private static EJB3WizardsCorePlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	private ILaunchConfiguration selectedLaunchConfiguration;
	
	/**
	 * The constructor.
	 */
	public EJB3WizardsCorePlugin() {
		super();
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
		resourceBundle = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static EJB3WizardsCorePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = EJB3WizardsCorePlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		try {
			if (resourceBundle == null)
				resourceBundle = ResourceBundle.getBundle("org.jboss.ide.eclipse.ejb3.wizards.core.EJB3WizardsCorePluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}
	
	/**
	 * Get the base directory of this plugin
	 */
	public String getBaseDir()
	{
		try
		{
			URL installURL = Platform.asLocalURL(this.getBundle().getEntry("/"));//$NON-NLS-1$
			return installURL.getFile().toString();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		return null;
	}
	
	
	public ILaunchConfiguration getSelectedLaunchConfiguration() {
		return selectedLaunchConfiguration;
	}
	
	public void setSelectedLaunchConfiguration(
			ILaunchConfiguration selectedLaunchConfiguration) {
		this.selectedLaunchConfiguration = selectedLaunchConfiguration;
	}
}
