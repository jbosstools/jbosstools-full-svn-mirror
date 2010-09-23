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
	public static final String IMG_CLOUD= NAME_PREFIX + "cloud.gif"; //$NON-NLS-1$
	public static final String IMG_FOLDER= NAME_PREFIX + "folder.gif"; //$NON-NLS-1$
	public static final String IMG_INSTANCE= NAME_PREFIX + "instance.gif"; //$NON-NLS-1$
	public static final String IMG_IMAGE= NAME_PREFIX + "image.gif"; //$NON-NLS-1$
	public static final String IMG_COLLAPSE_ALL= NAME_PREFIX + "collapseall.gif"; //$NON-NLS-1$
	public static final String IMG_DELTA= NAME_PREFIX + "delta.gif"; //$NON-NLS-1$
	public static final String IMG_DELTA_LARGE= NAME_PREFIX + "delta-large.gif"; //$NON-NLS-1$
	public static final ImageDescriptor DESC_CLOUD= createManaged("", IMG_CLOUD);
	public static final ImageDescriptor DESC_FOLDER= createManaged("", IMG_FOLDER);
	public static final ImageDescriptor DESC_INSTANCE= createManaged("", IMG_INSTANCE);
	public static final ImageDescriptor DESC_IMAGE= createManaged("", IMG_IMAGE);
	public static final ImageDescriptor DESC_COLLAPSE_ALL= createManaged("", IMG_COLLAPSE_ALL);
	public static final ImageDescriptor DESC_DELTA= createManaged("", IMG_DELTA);
	public static final ImageDescriptor DESC_DELTA_LARGE= createManaged("", IMG_DELTA_LARGE);

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
