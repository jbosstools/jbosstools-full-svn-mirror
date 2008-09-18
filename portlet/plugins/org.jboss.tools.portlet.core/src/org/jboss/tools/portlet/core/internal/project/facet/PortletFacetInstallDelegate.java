/*************************************************************************************
 * Copyright (c) 2008 JBoss, a division of Red Hat and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss, a division of Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.portlet.core.internal.project.facet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.PortletCoreActivator;

/**
 * @author snjeza
 * 
 */
public class PortletFacetInstallDelegate implements IDelegate {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.common.project.facet.core.IDelegate#execute(org.eclipse
	 * .core.resources.IProject,
	 * org.eclipse.wst.common.project.facet.core.IProjectFacetVersion,
	 * java.lang.Object, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void execute(final IProject project, final IProjectFacetVersion fv,
			final Object cfg, final IProgressMonitor monitor)
			throws CoreException {

		if (monitor != null) {
			monitor.beginTask("", 1);
		}
		try {
			IDataModel config = null;

			if (cfg != null) {
				config = (IDataModel) cfg;
			} else {
				throw new CoreException(
						PortletCoreActivator
								.getStatus("Internal Error creating JBoss Portlet Facet.  Missing configuration."));
			}

			// check whether web.xml is available for update
			IModelProvider provider = PortletCoreActivator
					.getModelProvider(project);
			if (provider == null) {
				throw new CoreException(
						PortletCoreActivator
								.getStatus("Cannot configure web module for JBoss Portlet Facet"));
			} else if (!(provider.validateEdit(null, null).isOK())) {
				if (!(provider.validateEdit(null, null).isOK())) {
					throw new CoreException(PortletCoreActivator
							.getStatus("The web.xml file is not updateable"));
				}
			}

			IJavaProject javaProject = JavaCore.create(project);	
			
			IPath containerPath = null;
			//if (IPortletConstants.PORTLET_FACET_VERSION_10.equals(fv.getVersionString())) {
				containerPath = new Path(IPortletConstants.PORTLET_CONTAINER_10_ID);
			//} else {
				containerPath = new Path(IPortletConstants.PORTLET_CONTAINER_20_ID);
			//}
			
			IClasspathEntry entry = JavaCore.newContainerEntry(containerPath, true);
			IClasspathEntry[] entries = javaProject.getRawClasspath();
			IClasspathEntry[] newEntries = new IClasspathEntry[entries.length + 1];
			System.arraycopy( entries, 0, newEntries, 0, entries.length );
			newEntries[entries.length] = entry;
			javaProject.setRawClasspath(newEntries, monitor);
			
			
			createPortletXml(project, fv, config, monitor);

			if (monitor != null) {
				monitor.worked(1);
			}

		} finally {
			if (monitor != null) {
				monitor.done();
			}
		}
	}

	private void createPortletXml(final IProject project,
			final IProjectFacetVersion fv, IDataModel config,
			final IProgressMonitor monitor) {

		try {
			final IWorkspaceRunnable op = new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor_inner)
						throws CoreException {
					PortletCoreActivator
							.createPortletXml(fv.getVersionString(),project, monitor);
					project.refreshLocal(IResource.DEPTH_INFINITE,
							monitor_inner);
				}
			};
			op.run(monitor);
		} catch (CoreException e) {
			PortletCoreActivator.log(e,
					"Exception occured while creating portlet.xml");
		}
	}
}
