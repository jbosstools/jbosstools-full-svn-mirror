/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.common.model.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

public class ModelUIImages {
//	private URL imageBaseURL = null;
//	private static String PREFIX_ICON_ENABLED  = "";
//	private static String PREFIX_ICON_DISABLED = "d";
//	private static String PREFIX_ICON_HOVER    = "h";
	
	private static String ACTIONS_PATH         = "wizards/"; //$NON-NLS-1$
	
	public static String ACT_CREATE_PROJECT    = ACTIONS_PATH + "new_project.gif"; //$NON-NLS-1$
	public static String ACT_ADOPT_PROJECT     = ACTIONS_PATH + "adopt_project.gif"; //$NON-NLS-1$
	public static String ACT_IMPORT_PROJECT    = ACTIONS_PATH + "import_project.gif"; //$NON-NLS-1$
	
	public static String WIZARD_NEW_PROJECT    = ACTIONS_PATH + "EclipseCreateNewProject.png"; //$NON-NLS-1$
	public static String WIZARD_DEFAULT        = ACTIONS_PATH + "EclipseDefault.png"; //$NON-NLS-1$
	public static String WIZARD_IMPORT_PROJECT = ACTIONS_PATH + "EclipseImport.png"; //$NON-NLS-1$
	public static String WIZARD_MODULES_CONFIG = ACTIONS_PATH + "EclipseModulesConfiguration.gif"; //$NON-NLS-1$
	
	// JAVA
	public static String JAVA_CLASS 			= "java/class.gif"; //$NON-NLS-1$
	public static String JAVA_INTERFACE 		= "java/interface.gif"; //$NON-NLS-1$
	public static String JAVA_PACKAGE 			= "java/package.gif"; //$NON-NLS-1$
	
	public static String TAGLIB_FILE 			= "editors/taglibs_file.gif"; //$NON-NLS-1$
	
	public static String TAGLIB_ATTRIBUTE 			= "editors/taglibs_attribute.gif"; //$NON-NLS-1$
	
	// this blok staye witout changes for compatibility
	private static ModelUIImages INSTANCE;
	
	static {
		try {
			INSTANCE = new ModelUIImages(new URL(ModelUIPlugin.getDefault().getBundle().getEntry("/"), "images/xstudio/")); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (MalformedURLException e) {
			// do nothing
			ModelUIPlugin.getPluginLog().logError(e);
		}
	}
	
	public static Image getImage(String key) {
		getImageDescriptor(key); // provide image in the registry
		ImageRegistry registry = ModelUIPlugin.getDefault().getImageRegistry();
		synchronized(registry) {
			return registry.get(key);
		}
	}

	public static ImageDescriptor getImageDescriptor(String key) {
		ImageDescriptor result = null;
		ImageRegistry registry = ModelUIPlugin.getDefault().getImageRegistry();
		synchronized(registry) {
			result = registry.getDescriptor(key);
		}
		if(result == null) {
			result = INSTANCE.createImageDescriptor(key);
			if(result != null) {
				synchronized (registry) {
					registry.remove(key);
					registry.put(key, result);
				}
			}
		}
		return result;
	}

	public static void setImageDescriptors(IAction action, String iconName)	{
		action.setImageDescriptor(INSTANCE.createImageDescriptor(iconName));
	}
	
	public static ModelUIImages getInstance() {
		return INSTANCE;
	}

	// for reusable purposes
	
	private URL baseUrl;
	private ModelUIImages parentRegistry;
	
	protected ModelUIImages(URL registryUrl, ModelUIImages parent){
		if(ModelUIPlugin.isDebugEnabled()) {
			ModelUIPlugin.getPluginLog().logInfo("Create ModelUIImages class."); //$NON-NLS-1$
			ModelUIPlugin.getPluginLog().logInfo("RegistryUrl = " + registryUrl); //$NON-NLS-1$
			ModelUIPlugin.getPluginLog().logInfo("parent = " + (parent==null?"null":parent.getClass().getName())); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if(registryUrl == null) throw new IllegalArgumentException("Base url for image registry cannot be null."); //$NON-NLS-1$
		baseUrl = registryUrl;
		parentRegistry = parent;
	}
	
	protected ModelUIImages(URL url){
		this(url,null);		
	}

	public ImageDescriptor createImageDescriptor(String key) {
		try {
			return ImageDescriptor.createFromURL(makeIconFileURL(key));
		} catch (MalformedURLException e) {
			if(parentRegistry == null) {
				return ImageDescriptor.getMissingImageDescriptor();
			} else {
				return parentRegistry.createImageDescriptor(key);
			}
			
		}		
	}

	private URL makeIconFileURL(String name) throws MalformedURLException {
		if (name == null) throw new MalformedURLException("Image name cannot be null.");
		return new URL(baseUrl, name);
	}	

}
