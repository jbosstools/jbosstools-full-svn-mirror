/*
 * Created on 16.04.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.jboss.tools.jsf.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;

import org.jboss.tools.common.model.ui.ModelUIImages;

/**
 * @author Eskimo
 *
 */
public class JsfUiImages extends ModelUIImages {
	
	public static String JSF_IMPORT_PROJECT_ACTION = "wizards/import_jsf_project.gif";
	public static String JSF_CREATE_PROJECT_ACTION = "wizards/new_jsf_project.gif";
	
	private static JsfUiImages INSTANCE;
	
	static {
		try {
			if(JsfUiPlugin.isDebugEnabled()) {
				JsfUiPlugin.getPluginLog().logError("Trying activate plugin images.");
			}
			INSTANCE = 
				new JsfUiImages(
					new URL(JsfUiPlugin.getDefault().getBundle().getEntry("/"), "images/xstudio/"),
					ModelUIImages.getInstance()
				);
			
		} catch (MalformedURLException e) {
			
		}
	}

	public static ImageDescriptor getImageDescriptor(String key) {
		if(JsfUiPlugin.isDebugEnabled()) {
			JsfUiPlugin.getPluginLog().logInfo("Create imageDescriptor for key '" + key + "'.");
		}
		return INSTANCE.createImageDescriptor(key);
	}

	/**
	 * @param registryUrl
	 * @param parent
	 */
	public JsfUiImages(URL registryUrl, ModelUIImages parent) {
		super(registryUrl, parent);
		if(JsfUiPlugin.isDebugEnabled()) {
			JsfUiPlugin.getPluginLog().logInfo("Create JsfUiImages class.");
			JsfUiPlugin.getPluginLog().logInfo("RegistryUrl = " + registryUrl);
			JsfUiPlugin.getPluginLog().logInfo("parent = " + parent.getClass().getName());
		}
	}

}
