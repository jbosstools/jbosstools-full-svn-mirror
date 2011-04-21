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

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
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
	private Map<String, ProjectLocation> projectNameToLocation;
	private static ProjectsLoader instance = null;

	private ProjectsLoader() {
		/*
		 * Load project names and paths to them from the extensions of
		 * {@link VpeAllTests#VPE_TEST_EXTENTION_POINT_ID}. And store
		 * loaded data in {@link #projectNameToLocation}.
		 */
		projectNameToLocation = new HashMap<String, ProjectLocation>();
		IExtension[] extensions = VPETestPlugin.getDefault().getVpeTestExtensions();
		for (IExtension extension : extensions) {
			IConfigurationElement[] confElements = extension
					.getConfigurationElements();
			for (IConfigurationElement configurationElement : confElements) {
				if (TEST_PROJECT_ELEMENT.equals(
						configurationElement.getName())) {
					Bundle bundle = Platform.getBundle(
							configurationElement.getNamespaceIdentifier());
					String name = configurationElement.getAttribute(
							TEST_PROJECT_NAME_ATTRIBUTE);
					String path = configurationElement.getAttribute(
							TEST_PROJECT_PATH_ATTRIBUTE);

					projectNameToLocation.put(name,
							new ProjectLocation(bundle, path));
				}
			}
		}
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
	 * extension point.
	 * <p>
	 * This method has <i>fail-fast</i> behavior. It never returns {@code null}.
	 * It throws exceptions in the cases if the project is not defined,
	 * can not be opened, etc.
	 */
	public IProject getProject(String projectName) throws IOException {
		IProject project = getExistingProject(projectName);

		if (project == null) {
			ProjectLocation location = projectNameToLocation.get(projectName);
			if (location == null) {
				throw new RuntimeException(
						"Project '" + project + "' is not defined.");
			}
			
			Bundle bundle = location.getBundle();
			if (bundle == null) {
				throw new NullPointerException(
						"Owning bundle of '" + project + "' is null.");
			}
			
			URL rootEntry = bundle.getEntry("/");
			if (rootEntry == null) {
				throw new NullPointerException(
						"Root entry to the bundle with id='"
						+ bundle.getBundleId() + "' cannot be resolved.");
			}
			
			URL resolvedRootEntry = FileLocator.resolve(rootEntry);
			String pluginRoot = resolvedRootEntry.getPath();
			if (pluginRoot.equals("")) {
				throw new RuntimeException("Path to '" + resolvedRootEntry 
						+ "' does not exist.");
			}

			String projectPath = pluginRoot + location.getPath();
			project = ResourcesUtils.importProjectIntoWorkspace(
						projectPath, projectName);
			if (project == null) {
				throw new RuntimeException("Project by the path='" + projectPath
						+ "' cannot be imported.");
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

	/**
	 * Stores the {@code path} to a project and the owning {@code bundle}.
	 * 
	 * @author Yahor Radtsevich (yradtsevich)
	 */
	private class ProjectLocation {
		private Bundle bundle;
		private String path;
		
		public ProjectLocation(Bundle bundle, String path) {
			this.bundle = bundle;
			this.path = path;
		}
		public Bundle getBundle() {
			return bundle;
		}
		public String getPath() {
			return path;
		}

		public String toString() {
			return String.format("(%s, %s)",
					bundle == null ? null : bundle.getLocation(), path);
		}
	}
}
