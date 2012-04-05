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
package org.jboss.tools.jmx.core.test.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.jboss.tools.jmx.core.test.JMXTestPlugin;
import org.osgi.framework.Bundle;

/**
 * Test plugins may define test projects to be added
 * to the test workspace to run tests on them.
 * This utility class manages adding/removing test projects.
 *
 * @author V.Kabanovich
 */

public class TestProjectProvider {
	IProject project;
	boolean makeCopy = false;

	/**
	 *
	 * @param bundleName - name of plugin that contains code of test project.
	 * @param projectPath - path of test project relative to the plugin that
	 * contains it. If null, then by default is computed as '/projects/%name%'
	 * @param name - name of test project. If null, then by default is computed
	 * as last part of projectPath.
	 * @param makeCopy - boolean flag, if true, then test project will be copied
	 * to the test workspace; on test completing, the copy is destroyed.
	 * @throws Exception
	 */
	public TestProjectProvider(String bundleName, String projectPath, String name, boolean makeCopy) throws CoreException {
		if(projectPath == null) {
			projectPath = "/projects/" + name;
		} else if(name == null) {
			name = projectPath.substring(projectPath.lastIndexOf('/')+1);
		}
		this.makeCopy = makeCopy;
		init(bundleName, projectPath, name);
	}

	public IProject getProject() {
		return project;
	}

	private void init(String bundleName, String projectPath, String name) throws CoreException {
		IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
		if(p.exists()) {
			project = p;
			if(!p.isOpen()) {
				project.open(new NullProgressMonitor());
				System.out.println("open");
			}
			return;
		}

		Bundle bundle = Platform.getBundle(bundleName);
		URL url = null;
		try {
			url = FileLocator.resolve(bundle.getEntry(projectPath));
		} catch (IOException e) {
			String msg = "Cannot find project " + name + " in " + bundleName;
			IStatus status = new Status(IStatus.ERROR, JMXTestPlugin.PLUGIN_ID, msg, e);
			throw new CoreException(status);
		}
		String location = url.getFile();
		if(makeCopy) {
			IPath root = ResourcesPlugin.getWorkspace().getRoot().getLocation();
			File destination = new File(root.toFile(), name);
			FileUtil.copyDir(new File(location), destination, true);
			importExistingProject(p, destination.getAbsolutePath(), name);
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		} else {
			importExistingProject(p, location, name);
		}
		if(p.exists()) {
			project = p;
		}
	}

	static void importExistingProject(IProject project, String location, String name) throws CoreException {
		IPath path = new Path(location).append(".project");
		IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(path);
		description.setName(name);
		try {
			project.create(description, new NullProgressMonitor());
			project.open(IResource.BACKGROUND_REFRESH, new NullProgressMonitor());
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (Exception e) {
			throw new RuntimeException("Cannot create project " + name + " from " + location, e);
		}
	}

	public void dispose() throws CoreException {
		if(project == null || !project.exists()) return;
		ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				IPath loc = project.getLocation();
				project.delete(false, true, new NullProgressMonitor());
				if(makeCopy) {
					FileUtil.remove(loc.toFile());
				}
				ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
			}
		}, new NullProgressMonitor());
	}
}
