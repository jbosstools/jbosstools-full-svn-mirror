package org.jboss.ide.eclipse.as.wtp.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class WTPOveridePlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.ide.eclipse.as.wtp.ui"; //$NON-NLS-1$
	public static final String[] ICON_DIRS = new String[]{"icons/full/obj16", //$NON-NLS-1$
		"icons/full/cview16", //$NON-NLS-1$
		"icons/full/ctool16", //$NON-NLS-1$
		"icons/full/clcl16", //$NON-NLS-1$
		"icons/full/ovr16", //$NON-NLS-1$
		"icons/full/extra", //$NON-NLS-1$
		"icons/full/wizban", //$NON-NLS-1$
		"icons", //$NON-NLS-1$
		""}; //$NON-NLS-1$
	
	// The shared instance
	private static WTPOveridePlugin plugin;

	/**
	 * The constructor
	 */
	public WTPOveridePlugin() {
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
	 * Get a .gif from the image registry.
	 */
	public Image getImage(String key) {
		ImageRegistry imageRegistry = getImageRegistry();
		Image image = imageRegistry.get(key);
		if (image == null || image.isDisposed()) {
			ImageDescriptor descriptor = getImageDescriptor(key);
			if (descriptor != null) {
				image = descriptor.createImage();
				imageRegistry.put(key, image);
			}
		}
		return image;
	}

	/**
	 * This gets a .gif from the icons folder.
	 */
	public ImageDescriptor getImageDescriptor(String key) {
		ImageDescriptor imageDescriptor = null;
		URL gifImageURL = getImageURL(key,getBundle());
		if (gifImageURL != null)
			imageDescriptor = ImageDescriptor.createFromURL(gifImageURL);
		return imageDescriptor;
	}

	/**
	 * This gets a .gif from the icons folder.
	 */
	public URL getImageURL(String key, Bundle bundle) {
		String gif = "/" + key + ".gif"; //$NON-NLS-1$ //$NON-NLS-2$
		IPath path = null;
		for (int i = 0; i < ICON_DIRS.length; i++) {
			path = new Path(ICON_DIRS[i]).append(gif);
			if (Platform.find(bundle,path) == null)
				continue;
			try {
				return new URL( bundle.getEntry("/"), path.toString()); //$NON-NLS-1$ 
			} catch (MalformedURLException exception) {
				logError(exception);
				continue;
			}
		}
		return null;
	}
	
	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static WTPOveridePlugin getInstance() {
		return plugin;
	}

	public static void log(Exception e) {
		log(e.getMessage(), e);
	}
	
	public static void log(String message, Exception e) {
		IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, message, e);
		getInstance().getLog().log(status);
	}
	public static IStatus createStatus(int severity, int aCode,
			String aMessage, Throwable exception) {
		return new Status(severity, PLUGIN_ID, aCode,
				aMessage != null ? aMessage : "No message.", exception); //$NON-NLS-1$
	}
	public static IStatus createErrorStatus(String message) {
		return createErrorStatus(message, null);
	}
	public static IStatus createWarningStatus(String message) {
		return createWarningStatus(message, null);
	}
	public static IStatus createErrorStatus(String message, Throwable exception) {
		return new Status(IStatus.ERROR, PLUGIN_ID, -1, message, exception);
	}
	public static IStatus createWarningStatus(String message, Throwable exception) {
		return new Status(IStatus.WARNING, PLUGIN_ID, -1, message, exception);
	}
	public static IStatus createErrorStatus(int aCode, String aMessage,
			Throwable exception) {
		return createStatus(IStatus.ERROR, aCode, aMessage, exception);
	}

	public static IStatus createStatus(int severity, String message, Throwable exception) {
		return new Status(severity, PLUGIN_ID, message, exception);
	}

	public static IStatus createStatus(int severity, String message) {
		return createStatus(severity, message, null);
	}
	public static void logError(Throwable exception) {
		Platform.getLog(Platform.getBundle(PLUGIN_ID)).log( createStatus(IStatus.ERROR, exception.getMessage(), exception));
	}

	public static void logError(CoreException exception) {
		Platform.getLog(Platform.getBundle(PLUGIN_ID)).log( exception.getStatus() );
	}

}
