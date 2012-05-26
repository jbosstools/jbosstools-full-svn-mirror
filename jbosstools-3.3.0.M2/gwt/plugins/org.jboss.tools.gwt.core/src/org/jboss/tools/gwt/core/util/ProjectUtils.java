/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.gwt.core.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.ModuleCoreNature;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

/**
 * A class that holds various utility methods that help to deal with projects.
 * 
 * @author Andre Dietisheim
 * 
 * @see IProject
 */
public class ProjectUtils {

	/** The name of the web xml file */
	public static final String WEB_XML_FILE = "web.xml";

	/** the folder that holds the web configurations and deployed classes */
	public static final String WEB_INF_FOLDER = "WEB-INF";

	public static IPath getWebXmlPath(IProject project) {
		IPath webXmlPath = new Path(WEB_INF_FOLDER).append(WEB_XML_FILE);
		boolean exists = project.getProjectRelativePath().append(webXmlPath).toFile().exists();
		if (!exists) {
			webXmlPath = IModelProvider.FORCESAVE;
		}
		return webXmlPath;
	}

	/**
	 * Adds a classpath entry to the given java project.
	 * 
	 * @param javaProject
	 *            the java project
	 * @param entry
	 *            the classpath entry to add
	 * @param monitor
	 * @throws JavaModelException
	 *             signals that an error occurred when adding the given entry
	 * 
	 * @see IJavaProject
	 * @see IClasspathEntry
	 */
	public static void addClasspathEntry(IJavaProject javaProject, IClasspathEntry entry, IProgressMonitor monitor)
			throws JavaModelException {
		IClasspathEntry[] rawClasspath = javaProject.getRawClasspath();
		IClasspathEntry[] newClasspath = new IClasspathEntry[rawClasspath.length + 1];
		System.arraycopy(rawClasspath, 0, newClasspath, 0, rawClasspath.length);
		newClasspath[rawClasspath.length] = entry;
		javaProject.setRawClasspath(newClasspath, monitor);
	}

	/**
	 * Returns the path of the web content folder in the given project. The path
	 * returned is relative to the workspace.
	 * 
	 * @param project
	 *            the project to return the web content folder for
	 * @return the path of the web content folder (relative to the workspace)
	 */
	public static IPath getWebContentRootPath(IProject project) {
		if (!ModuleCoreNature.isFlexibleProject(project)) {
			return project.getFullPath();
		}
		IPath path = null;
		IVirtualComponent component = ComponentCore.createComponent(project);
		if (component != null && component.exists()) {
			path = component.getRootFolder().getWorkspaceRelativePath();
		} else {
			path = project.getFullPath();
		}
		return path;
	}

	/**
	 * Returns the (paths of) the source folders for the given java project. The
	 * paths returned are relative to the workspace.
	 * 
	 * @param javaProject
	 *            the java project
	 * @return the source folders (relative to the workspace)
	 * @throws JavaModelException
	 *             the java model exception
	 * 
	 * @see IPath
	 * @see IJavaProject
	 * @see IClasspathEntry
	 * @see IClasspathEntry#CPE_SOURCE
	 */
	public static List<IPath> getSourceFolders(IJavaProject javaProject) throws JavaModelException {
		List<IPath> srcFolderList = new ArrayList<IPath>();
		for (IClasspathEntry classpathEntry : javaProject.getRawClasspath()) {
			if (IClasspathEntry.CPE_SOURCE == classpathEntry.getEntryKind()) {
				srcFolderList.add(classpathEntry.getPath());
			}
		}
		return srcFolderList;
	}



	public static WebApp getWebApp(IProject project) {
		IModelProvider modelProvider = ModelProviderManager.getModelProvider(project);
		Object modelObject = modelProvider.getModelObject();
		Assert.isTrue(modelObject instanceof WebApp, MessageFormat.format(
				"Could not get webapp model for project {0}. The given project is not a web app.", project.getName()));
		return (WebApp) modelObject;
	}
}
