/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.seam.core;

import java.io.File;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IProject;
import org.jboss.tools.seam.core.project.facet.SeamRuntime;

/**
 * @author Alexey Kazakov
 */
public class SeamUtil {

	/**
	 * Returns Seam version from <Seam Runtime>/lib/jboss-seam.jar/META-INF/MANIFEST.MF
	 * from Seam Runtime which is set for the project.
	 * @param project
	 * @return
	 */
	public static String getSeamVersionFromManifest(IProject project) {
		ISeamProject seamProject = SeamCorePlugin.getSeamProject(project, false);
		if(seamProject == null) {
			SeamCorePlugin.getPluginLog().logWarning("Can't find Seam Project for " + project.getName());
			return null;
		}
		return getSeamVersionFromManifest(seamProject);
	}

	/**
	 * Returns Seam version from <Seam Runtime>/lib/jboss-seam.jar/META-INF/MANIFEST.MF
	 * from Seam Runtime which is set for the project.
	 * @param project
	 * @return
	 */
	public static String getSeamVersionFromManifest(ISeamProject project) {
		SeamRuntime runtime = project.getRuntime();
		if(runtime==null) {
			SeamCorePlugin.getPluginLog().logWarning("Seam Runtime for " + project.getProject().getName() + " is null.");
			return null;
		}
		return getSeamVersionFromManifest(runtime);
	}

	private final static String seamJarName = "jboss-seam.jar";
	private final static String seamVersionAttributeName = "Seam-Version";

	/**
	 * Returns Seam version from <Seam Runtime>/lib/jboss-seam.jar/META-INF/MANIFEST.MF
	 * from Seam Runtime.
	 * @param runtime
	 * @return
	 */
	public static String getSeamVersionFromManifest(SeamRuntime runtime) {
		File jarFile = new File(runtime.getLibDir(), seamJarName);
		if(!jarFile.isFile()) {
			jarFile = new File(runtime.getHomeDir(), seamJarName);
			if(!jarFile.isFile()) {
				SeamCorePlugin.getPluginLog().logWarning(jarFile.getAbsolutePath() + " as well as " + new File(runtime.getLibDir(), seamJarName).getAbsolutePath() + " don't exist for Seam Runtime " + runtime.getName());
				return null;
			}
		}
		try {
			JarFile jar = new JarFile(jarFile);
			Attributes attributes = jar.getManifest().getMainAttributes();
			String version = attributes.getValue(seamVersionAttributeName);
			if(version==null) {
				SeamCorePlugin.getPluginLog().logWarning("Can't get Seam-Version from " + jar.getName() + " MANIFEST.");
			}
			return version;
		} catch (IOException e) {
			SeamCorePlugin.getPluginLog().logError(e);
			return null;
		}
	}
}