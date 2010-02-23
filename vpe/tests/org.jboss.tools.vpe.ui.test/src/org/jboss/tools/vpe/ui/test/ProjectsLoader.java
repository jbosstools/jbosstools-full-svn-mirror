/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.test;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.test.util.JobUtils;
import org.jboss.tools.test.util.ResourcesUtils;
import org.osgi.framework.Bundle;

/**
 * Singleton class to operate on test projects.
 * 
 * @see {@code org.jboss.tools.vpe.ui.tests} extension point
 * @author Yahor Radtsevich (yradtsevich)
 */
public class ProjectsLoader {
	private static final String TEST_PROJECT_ELEMENT = "testProject";
	private static final String TEST_PROJECT_PATH_ATTRIBUTE = "path";
	private static final String TEST_PROJECT_NAME_ATTRIBUTE = "name";
	private Map<String, String> projectNameToPath;
	private static ProjectsLoader instance = null;

	private ProjectsLoader() {
		loadProjectPaths();
	}

	/**
	 * Returns the instance of {@link ProjectsLoader}
	 */
	public static ProjectsLoader getInstance() {
		if (instance == null) {
			instance = new ProjectsLoader();
		}

		return instance;
	}

	/**
	 * Returns instance of {@link IProject} by {@code projectName}.
	 * If the project does not exist in the workspace, imports it from the
	 * resources specified by extensions of {@code org.jboss.tools.vpe.ui.tests}
	 * extension point. Returns {@code null} if the project is not declared in
	 * the extensions.
	 */
	public IProject getProject(String projectName) {
		IProject project = getExistingProject(projectName);

		if (project == null) {
			String projectPath = projectNameToPath.get(projectName);
			if (projectPath != null) {
				project = ResourcesUtils.importProjectIntoWorkspace(
						projectPath, projectName);
			}
		}

		return project;
	}

	/**
	 * Returns a workspace project by its {@code projectName}, or {@code null}
	 * if there is no project with this name in the workspace.
	 */
	public static IProject getExistingProject(String projectName) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);
		if (project.isAccessible()) {
			return project;
		} else {
			return null;
		}
	}

	/**
	 * Loads project names and paths to them from the extensions of
	 * {@link VpeAllTests#VPE_TEST_EXTENTION_POINT_ID}. And stores
	 * loaded data in {@link #projectNameToPath}.
	 */
	private void loadProjectPaths() {
		projectNameToPath = new HashMap<String, String>();
		IExtensionRegistry extensionRepository = Platform
				.getExtensionRegistry();

		IExtensionPoint extensionPoint = extensionRepository
				.getExtensionPoint(VpeAllTests.VPE_TEST_EXTENTION_POINT_ID);
		IExtension[] extensions = extensionPoint.getExtensions();
		for (IExtension extension : extensions) {
			IConfigurationElement[] confElements = extension
					.getConfigurationElements();
			for (IConfigurationElement configurationElement : confElements) {
				if (TEST_PROJECT_ELEMENT.equals(configurationElement.getName())) {
					try {
						Bundle bundle = Platform.getBundle(configurationElement
								.getNamespaceIdentifier());

						String pluginRoot = FileLocator
								.resolve(bundle.getEntry("/")).getPath();
						String name = configurationElement.getAttribute(TEST_PROJECT_NAME_ATTRIBUTE);
						String path = configurationElement.getAttribute(TEST_PROJECT_PATH_ATTRIBUTE);
						projectNameToPath.put(name, pluginRoot + path);
					} catch (Exception e) {
						VPETestPlugin.getDefault().logError(e);
					}
				}
			}
		}
	}
	
	/**
	 * Removes the project with the {@code projectName} from the workspace.
	 * 
	 * @param projectName the project name
	 * @throws CoreException the core exception
	 */
	static public void removeProject(String projectName) throws CoreException {
		IProject project = ProjectsLoader.getExistingProject(projectName);
		removeProject(project);
	}

	/**
	 * Removes given {@code project} from the workspace
	 * 
	 * @param project project to remove
	 * @throws CoreException
	 */
	private static void removeProject(IProject project) throws CoreException {
		boolean saveAutoBuild = ResourcesUtils.setBuildAutomatically(false);
		try {
			if (project != null) {
				project.delete(IResource.ALWAYS_DELETE_PROJECT_CONTENT,
						new NullProgressMonitor());
				JobUtils.waitForIdle();
			}
		} finally {
			ResourcesUtils.setBuildAutomatically(saveAutoBuild);
		}
	}

	/**
	 * Removes all projects from the workspace
	 * 
	 * @throws CoreException
	 */
	static public void removeAllProjects() throws CoreException {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		for (IProject project: projects) {
			removeProject(project);
		}
	}
}
