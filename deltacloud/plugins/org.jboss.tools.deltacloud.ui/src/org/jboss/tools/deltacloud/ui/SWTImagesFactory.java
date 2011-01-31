/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

public class SWTImagesFactory {
	// The plugin registry
	private static ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();

	// Subdirectory (under the package containing this class) where 16 color images are
	private static URL fgIconBaseURL;

	static {
		try {
			fgIconBaseURL= new URL(Activator.getDefault().getBundle().getEntry("/"), "icons/" ); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (MalformedURLException e) {
			Activator.log(e);
		}
	}	
	private static final String NAME_PREFIX= Activator.PLUGIN_ID + '.';
	private static final int NAME_PREFIX_LENGTH= NAME_PREFIX.length();
	public static final String IMG_CLOUD= NAME_PREFIX + "delta.png"; //$NON-NLS-1$
//	public static final String IMG_CLOUD= NAME_PREFIX + "cloud.gif"; //$NON-NLS-1$
	public static final String IMG_COLLAPSE_ALL= NAME_PREFIX + "collapseall.gif"; //$NON-NLS-1$
	public static final String IMG_DELTA= NAME_PREFIX + "delta.gif"; //$NON-NLS-1$
	public static final String IMG_DELTA_LARGE= NAME_PREFIX + "delta-large.gif"; //$NON-NLS-1$
	public static final String IMG_DESTROY = NAME_PREFIX + "destroy.gif"; //$NON-NLS-1$
	public static final String IMG_DESTROY_D = NAME_PREFIX + "destroyd.gif"; //$NON-NLS-1$
	public static final String IMG_FOLDER= NAME_PREFIX + "folder.gif"; //$NON-NLS-1$
	public static final String IMG_IMAGE= NAME_PREFIX + "image.gif"; //$NON-NLS-1$
	public static final String IMG_INSTANCE= NAME_PREFIX + "instance.gif"; //$NON-NLS-1$
	public static final String IMG_NEW_DELTA= NAME_PREFIX + "newdelta.gif"; //$NON-NLS-1$
	public static final String IMG_REBOOT = NAME_PREFIX + "reboot.gif"; //$NON-NLS-1$
	public static final String IMG_REBOOTD = NAME_PREFIX + "rebootd.gif"; //$NON-NLS-1$
	public static final String IMG_START= NAME_PREFIX + "run.gif"; //$NON-NLS-1$
	public static final String IMG_STARTD = NAME_PREFIX + "rund.gif"; //$NON-NLS-1$
	public static final String IMG_RUNNING= NAME_PREFIX + "running.gif"; //$NON-NLS-1$
	public static final String IMG_STOP = NAME_PREFIX + "stop.gif"; //$NON-NLS-1$
	public static final String IMG_STOPD = NAME_PREFIX + "stopd.gif"; //$NON-NLS-1$
	public static final String IMG_STOPPED = NAME_PREFIX + "stopped.gif"; //$NON-NLS-1$
	public static final String SYSTEM_VIEW = NAME_PREFIX + "system_view.gif"; //$NON-NLS-1$
	public static final String SYSTEM_VIEWD = NAME_PREFIX + "system_viewd.gif"; //$NON-NLS-1$
	public static final ImageDescriptor DESC_CLOUD= createManaged("", IMG_CLOUD);
	public static final ImageDescriptor DESC_FOLDER= createManaged("", IMG_FOLDER);
	public static final ImageDescriptor DESC_INSTANCE= createManaged("", IMG_INSTANCE);
	public static final ImageDescriptor DESC_IMAGE= createManaged("", IMG_IMAGE);
	public static final ImageDescriptor DESC_COLLAPSE_ALL= createManaged("", IMG_COLLAPSE_ALL);
	public static final ImageDescriptor DESC_DELTA= createManaged("", IMG_DELTA);
	public static final ImageDescriptor DESC_DELTA_LARGE= createManaged("", IMG_DELTA_LARGE);
	public static final ImageDescriptor DESC_NEW_DELTA= createManaged("", IMG_NEW_DELTA);
	public static final ImageDescriptor DESC_DESTROY = createManaged("", IMG_DESTROY);
	public static final ImageDescriptor DESC_DESTROYD= createManaged("", IMG_DESTROY_D);
	public static final ImageDescriptor DESC_START= createManaged("", IMG_START);
	public static final ImageDescriptor DESC_STARTD= createManaged("", IMG_STARTD);
	public static final ImageDescriptor DESC_RUNNING= createManaged("", IMG_RUNNING);
	public static final ImageDescriptor DESC_STOP= createManaged("", IMG_STOP);
	public static final ImageDescriptor DESC_STOPD= createManaged("", IMG_STOPD);
	public static final ImageDescriptor DESC_STOPPED= createManaged("", IMG_STOPPED);
	public static final ImageDescriptor DESC_REBOOT= createManaged("", IMG_REBOOT);
	public static final ImageDescriptor DESC_REBOOTD= createManaged("", IMG_REBOOTD);

	private static ImageDescriptor createManaged(String prefix, String name) {
		return createManaged(imageRegistry, prefix, name);
	}
	
	private static ImageDescriptor createManaged(ImageRegistry registry, String prefix, String name) {
		ImageDescriptor result= ImageDescriptor.createFromURL(makeIconFileURL(prefix, name.substring(NAME_PREFIX_LENGTH)));
		registry.put(name, result);
		return result;
	}
	
	public static Image get(String key) {
		return imageRegistry.get(key);
	}
	
	private static ImageDescriptor create(String prefix, String name) {
		return ImageDescriptor.createFromURL(makeIconFileURL(prefix, name));
	}
	
	private static URL makeIconFileURL(String prefix, String name) {
		StringBuffer buffer= new StringBuffer(prefix);
		buffer.append(name);
		try {
			return new URL(fgIconBaseURL, buffer.toString());
		} catch (MalformedURLException e) {
			Activator.log(e);
			return null;
		}
	}
	
	/**
	 * Sets all available image descriptors for the given action.
	 */	
	public static void setImageDescriptors(IAction action, String type, String relPath) {
	    if (relPath.startsWith(NAME_PREFIX))
	        relPath= relPath.substring(NAME_PREFIX_LENGTH);
		action.setDisabledImageDescriptor(create("d" + type, relPath)); //$NON-NLS-1$
		action.setImageDescriptor(create("e" + type, relPath)); //$NON-NLS-1$

	}
	
	/**
	 * Helper method to access the image registry from the CUIPlugin class.
	 */
	static ImageRegistry getImageRegistry() {
		return imageRegistry;
	}

}
