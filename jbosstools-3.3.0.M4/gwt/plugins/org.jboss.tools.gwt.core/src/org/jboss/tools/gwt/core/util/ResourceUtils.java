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

import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

public class ResourceUtils {

	/**
	 * Returns a file for the given path in the given project.
	 * 
	 * @param path
	 *            the path to return the file for
	 * @param project
	 *            the project the path's located in
	 * @return the file that represents the given path in the given project
	 */
	public static File getFile(IPath path, IProject project) {
		IFolder folder = project.getWorkspace().getRoot().getFolder(path);
		return folder.getLocation().toFile();
	}

	/**
	 * Creates the given resource and all its parents (recursively)
	 * 
	 * @param resource
	 * @param monitor
	 * @throws CoreException
	 */
	public static void create(IResource resource, IProgressMonitor monitor) throws CoreException {
		if (resource.exists())
			return;
		create(resource.getParent(), monitor);

		switch (resource.getType()) {
		case IResource.FOLDER:
			((IFolder) resource).create(IResource.NONE, true, null);
			break;
		case IResource.PROJECT:
			((IProject) resource).create(monitor);
			((IProject) resource).open(monitor);
			break;
		}
	}
}
