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
package org.jboss.tools.test.util;

import junit.extensions.TestSetup;
import junit.framework.Test;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.test.util.xpl.EditorTestHelper;

/**
 * @author eskimo
 *
 */
public class ProjectImportTestSetup extends TestSetup {

	private String bundleName;
	private String[] projectPaths;
	private String[] projectNames;

	/**
	 * @param test
	 */
	public ProjectImportTestSetup(Test test, String bundleName, String projectPath, String projectName) {
		super(test);
		this.bundleName = bundleName;
		this.projectPaths = new String[]{projectPath};
		this.projectNames = new String[]{projectName};
	}

	public ProjectImportTestSetup(Test test, String bundleName, String[] projectPaths, String[] projectNames) {
		super(test);
		this.bundleName = bundleName;
		this.projectPaths = projectPaths;
		this.projectNames = projectNames;
	}

	public IProject importProject() throws Exception {
		return importProjects()[0];
	}

	public IProject[] importProjects() throws Exception {
		IProject[] projects = new IProject[projectPaths.length]; 
		for (int i = 0; i < projectPaths.length; i++) {
			EditorTestHelper.joinBackgroundActivities();
			projects[i] = (IProject)ResourcesUtils.importProject(bundleName, projectPaths[i]);
			EditorTestHelper.joinBackgroundActivities();
		}
		return projects;
	}	

	public static IProject loadProject(String projectName) throws CoreException {
		IResource project = ResourcesPlugin.getWorkspace().getRoot().findMember(projectName);
		assertNotNull("Can't load " + projectName, project);
		IProject result = project.getProject();
		try {
			result.build(IncrementalProjectBuilder.FULL_BUILD, null);
		} catch (ResourceException e) {
			JUnitUtils.fail(e.getMessage(), e);
		}
		EditorTestHelper.joinBackgroundActivities();
		return result;
	}

	@Override
	protected void setUp() throws Exception {
		importProjects();
	}

	@Override
	protected void tearDown() throws Exception {
		boolean saveAutoBuild = ResourcesUtils.setBuildAutomatically(false);
		JobUtils.waitForIdle();
		for (int i = 0; i < projectNames.length; i++) {
			ResourcesUtils.deleteProject(projectNames[i]);
			JobUtils.waitForIdle();
		}
		ResourcesUtils.setBuildAutomatically(saveAutoBuild);
	}
}