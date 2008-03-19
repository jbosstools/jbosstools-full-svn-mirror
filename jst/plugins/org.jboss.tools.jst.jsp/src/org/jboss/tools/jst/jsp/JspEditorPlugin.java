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
package org.jboss.tools.jst.jsp;

import java.io.File;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jst.jsp.ui.internal.JSPUIPlugin;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.jboss.tools.common.log.BaseUIPlugin;
import org.jboss.tools.common.log.IPluginLog;
import org.jboss.tools.common.text.xml.XmlEditorPlugin;
import org.jboss.tools.jst.jsp.preferences.JSPOccurrencePreferenceConstants;

/**
 * The main plugin class to be used in the desktop.
 */
public class JspEditorPlugin extends BaseUIPlugin {
	//The shared instance.
	private static JspEditorPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	public static final String PLUGIN_ID = "org.jboss.tools.jst.jsp"; 


	/**
	 * The constructor.
	 */
	public JspEditorPlugin() {
		plugin = this;
		try {
			resourceBundle= ResourceBundle.getBundle("org.jboss.tools.jst.jsp.JspEditorPluginResources");
		} catch (MissingResourceException x) {
			getPluginLog().logError(x);
			resourceBundle = null;
		}
	}


	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle= JspEditorPlugin.getDefault().getResourceBundle();
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			getPluginLog().logError(e);
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	
	public void startup() throws CoreException {
		super.startup();
        // Bug-fix for showing breakpoint icons at startup
        Platform.getPlugin("org.jboss.tools.jst.web.debug.ui");
	}
	
	protected void initializeDefaultPluginPreferences() {
		IPreferenceStore store = getPreferenceStore();
		JSPOccurrencePreferenceConstants.initializeDefaultValues(store);

	}

	public void initDefaultPluginPreferences() {
		IPreferenceStore store = JSPUIPlugin.getDefault().getPreferenceStore();

		JSPOccurrencePreferenceConstants.initializeDefaultValues(store);

	}

	public synchronized JavaTextTools getJavaTextTools() {
		return XmlEditorPlugin.getDefault().getJavaTextTools();
	}

	/* New Text Editors */	

	public static Path getInstallLocation()
	{
//		Object obj = null;
		try
		{
			URL url = getDefault().getDescriptor().getInstallURL();
			String s1 = FileLocator.resolve(url).getFile();
			if(s1.startsWith("/"))
				s1 = s1.substring(1);
			s1 = (new Path(s1)).toOSString();
			String s;
			if(s1.endsWith(File.separator))
				s = s1;
			else
				s = s1 + File.separator;
			return new Path(s);
		}	catch(Exception exception)	{
			getDefault().logError(exception);
		}
		return null;
	}

	public static Shell getActiveShell()
	{
		if(plugin == null)
			return null;
		IWorkbench workBench = plugin.getWorkbench();
		if(workBench == null)
			return null;
		IWorkbenchWindow workBenchWindow = workBench.getActiveWorkbenchWindow();
		if(workBenchWindow == null)
			return null;
		else
			return workBenchWindow.getShell();
	}

	public static JspEditorPlugin getDefault() {
		return plugin;
	}

	public static boolean isDebugEnabled() {
		return JspEditorPlugin.getDefault().isDebugging();
	}

	/**
	 * @return IPluginLog object
	 */
	public static IPluginLog getPluginLog() {
		return getDefault();
	}
}
