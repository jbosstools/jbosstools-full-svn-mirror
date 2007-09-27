package org.jboss.ide.eclipse.ejb3.wizards.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class EJB3WizardsUIPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static EJB3WizardsUIPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	/**
	 * The constructor.
	 */
	public EJB3WizardsUIPlugin() {
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
	public static EJB3WizardsUIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = EJB3WizardsUIPlugin.getDefault().getResourceBundle();
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
				resourceBundle = ResourceBundle.getBundle("org.jboss.ide.eclipse.ejb3.wizards.ui.EJB3WizardsUIPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}
	
	public static void alert(String string)
	{
		MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(),
				"EJB3 Tools - Alert", Display.getDefault().getSystemImage(SWT.ICON_INFORMATION),
				string, MessageDialog.INFORMATION,new String[] { "OK", }, 0);

		dialog.setBlockOnOpen(true);
		
		dialog.open();
	}

	public static void error(String string) {
		MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(),
				"EJB3 Tools - Error", Display.getDefault().getSystemImage(SWT.ICON_ERROR),
				string, MessageDialog.ERROR,new String[] { "OK", }, 0);

		dialog.setBlockOnOpen(true);
		
		dialog.open();
	}
	
	public static void warn(String string) {
		MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(),
				"EJB3 Tools - Warning", Display.getDefault().getSystemImage(SWT.ICON_WARNING),
				string, MessageDialog.WARNING,new String[] { "OK", }, 0);

		dialog.setBlockOnOpen(true);
		
		dialog.open();
	}
	
}
