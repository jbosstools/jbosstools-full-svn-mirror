/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 

package org.jboss.tools.seam.internal.core.project.facet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.jboss.tools.seam.core.SeamCorePlugin;
import org.jboss.tools.seam.core.SeamPreferences;
import org.jboss.tools.seam.core.project.facet.SeamFacetPreference;
import org.jboss.tools.seam.core.project.facet.SeamRuntime;
import org.jboss.tools.seam.core.project.facet.SeamRuntimeListConverter1;
import org.jboss.tools.seam.core.project.facet.SeamVersion;

/**
 * @author eskimo
 *
 */
public class SeamFacetPreferenceInitializer extends
		AbstractPreferenceInitializer {

	public static String RUNTIME_CONFIG_FORMAT_VERSION = "1.0";
	
	/**
	 * 
	 */
	public SeamFacetPreferenceInitializer() {}

	@Override
	public void initializeDefaultPreferences() {
		IScopeContext context = new DefaultScope();
		IEclipsePreferences node = context.getNode(SeamCorePlugin.PLUGIN_ID);
		node.put(SeamFacetPreference.RUNTIME_CONFIG_FORMAT_VERSION, RUNTIME_CONFIG_FORMAT_VERSION);
		node.put(SeamFacetPreference.SEAM_DEFAULT_CONNECTION_PROFILE, "DefaultDS");
		node.put(SeamFacetPreference.JBOSS_AS_DEFAULT_DEPLOY_AS, "war");
		node.put(SeamFacetPreference.HIBERNATE_DEFAULT_DB_TYPE, "HSQL");
		initializeDefault(node,getSeamGenBuildPath());
	}

	public static final String SEAM_GEN_HOME = "../../../../jboss-eap/seam"; 
	
	public String getSeamGenBuildPath() {
		String pluginLocation=null;
		try {
			pluginLocation = FileLocator.resolve(SeamCorePlugin.getDefault().getBundle().getEntry("/")).getFile();
		} catch (IOException e) {
			SeamCorePlugin.getPluginLog().logError(e);
		};
		File seamGenDir = new File(pluginLocation, SEAM_GEN_HOME);
		Path  p = new Path(seamGenDir.getPath());
		p.makeAbsolute();
		if(p.toFile().exists()) {
			return p.toOSString();
		} else {
			return "";
		}
	}
	
	/**
	 * @param node 
	 * @param seamGenBuildPath
	 * @return
	 */
	public void initializeDefault(IEclipsePreferences node, String seamGenBuildPath) {
		Map<String, SeamRuntime> map = new HashMap<String,SeamRuntime>();
		File seamFolder = new File(seamGenBuildPath);
		if(seamFolder.exists() && seamFolder.isDirectory()) {
			SeamRuntime rt = new SeamRuntime();
			rt.setHomeDir(seamGenBuildPath);
			rt.setName(seamFolder.getName()+"."+SeamVersion.V_1_2);
			rt.setDefault(true);
			rt.setVersion(SeamVersion.SEAM_1_2);
			map.put(rt.getName(), rt);
		}
		node.put(SeamFacetPreference.RUNTIME_LIST, new SeamRuntimeListConverter1().getString(map));
	}
}
