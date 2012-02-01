/*************************************************************************************
 * Copyright (c) 2008-2011 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.portlet.core.internal.project.facet;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.jboss.tools.portlet.core.IPortletConstants;

/**
 * @author snjeza
 *
 */
public class PortletFacetUninstallDelegate implements IDelegate {

	/* (non-Javadoc)
	 * @see org.eclipse.wst.common.project.facet.core.IDelegate#execute(org.eclipse.core.resources.IProject, org.eclipse.wst.common.project.facet.core.IProjectFacetVersion, java.lang.Object, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void execute(IProject project, IProjectFacetVersion fv,
			Object config, IProgressMonitor monitor) throws CoreException {
		IJavaProject javaProject = JavaCore.create(project);
		if (javaProject != null && javaProject.exists()) {
			IClasspathEntry[] entries = javaProject.getRawClasspath();
			IPath containerPath = new Path(IPortletConstants.PORTLET_RUNTIME_CONTAINER_ID);
			List<IClasspathEntry> newEntries = new ArrayList<IClasspathEntry>();
			for (IClasspathEntry entry:entries) {
				if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
					IPath path = entry.getPath();
					if (containerPath.equals(path)) {
						continue;
					}
				}
				newEntries.add(entry);
			}
			if (newEntries.size() != entries.length) {
				javaProject.setRawClasspath(newEntries.toArray(new IClasspathEntry[0]), monitor);
			}
		}
	}

}
