/*************************************************************************************
 * Copyright (c) 2008-2009 JBoss by Red Hat and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.portlet.core.internal.project.facet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryInstallDelegate;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.Messages;
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
			monitor.beginTask("", 1); //$NON-NLS-1$
		}
		try {
			IDataModel config = null;

			if (cfg != null) {
				config = (IDataModel) cfg;
			} else {
				throw new CoreException(
						PortletCoreActivator
								.getStatus(Messages.PortletFacetInstallDelegate_Missing_configuration));
			}

			// check whether web.xml is available for update
			IModelProvider provider = PortletCoreActivator
					.getModelProvider(project);
			if (provider == null) {
				throw new CoreException(
						PortletCoreActivator
								.getStatus(Messages.PortletFacetInstallDelegate_Cannot_configure_web_module_for_JBoss_Portlet_Facet));
			} else if (!(provider.validateEdit(null, null).isOK())) {
				if (!(provider.validateEdit(null, null).isOK())) {
					throw new CoreException(PortletCoreActivator
							.getStatus(Messages.PortletFacetInstallDelegate_The_web_xml_file_is_not_updateable));
				}
			}

			/*IJavaProject javaProject = JavaCore.create(project);	
			boolean enableImplementationLibrary = config.getBooleanProperty(IPortletConstants.ENABLE_IMPLEMENTATION_LIBRARY);
			if (enableImplementationLibrary) {
				setClasspath(monitor, javaProject, config);
			}*/
			
			//Configure libraries
			( (LibraryInstallDelegate) config.getProperty( IPortletConstants.PORTLET_LIBRARY_PROVIDER_DELEGATE ) ).execute( new NullProgressMonitor() );
			
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

	private void setClasspath(final IProgressMonitor monitor,
			IJavaProject javaProject, IDataModel config) throws CoreException {
		boolean deployPortletJars = config.getBooleanProperty(IPortletConstants.DEPLOY_PORTLET_JARS);
		if (deployPortletJars) {
			copyLibraries(monitor,javaProject,config);
		} else {
			String implementationLibrary = config.getStringProperty(IPortletConstants.IMPLEMENTATION_LIBRARY);
			/*if (IPortletConstants.LIBRARY_PROVIDED_BY_JBOSS_TOOLS.equals(implementationLibrary)) {
				IPath containerPath = new Path(IPortletConstants.PORTLET_CONTAINER_20_ID);
				setContainerPath(monitor, javaProject, containerPath);
			} else*/ 
			if (IPortletConstants.LIBRARIES_PROVIDED_BY_SERVER_RUNTIME.equals(implementationLibrary)) {
				IPath containerPath = new Path(IPortletConstants.PORTLET_RUNTIME_CONTAINER_ID);
				setContainerPath(monitor, javaProject, containerPath);
			} else {
				String libraryName = config.getStringProperty(IPortletConstants.USER_LIBRARY_NAME);
				if (libraryName != null && libraryName.trim().length() > 0) {
					IPath containerPath = new Path(JavaCore.USER_LIBRARY_CONTAINER_ID + "/" + libraryName); //$NON-NLS-1$
					setContainerPath(monitor, javaProject, containerPath);
				} else {
					PortletCoreActivator.log(null, Messages.PortletFacetInstallDelegate_User_library_name_is_invalid);
				}
			    // user library	
				//JavaCore.getCla
			}
		
		}
	}

	private void copyLibraries(IProgressMonitor monitor,
			IJavaProject javaProject, IDataModel config) {
		
	}

	private void setContainerPath(IProgressMonitor monitor, IJavaProject javaProject,IPath containerPath) throws CoreException {
		IClasspathEntry entry = JavaCore.newContainerEntry(containerPath, true);
		IClasspathEntry[] entries = javaProject.getRawClasspath();
		IClasspathEntry[] newEntries = new IClasspathEntry[entries.length + 1];
		System.arraycopy( entries, 0, newEntries, 0, entries.length );
		newEntries[entries.length] = entry;
		javaProject.setRawClasspath(newEntries, monitor);
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
					Messages.PortletFacetInstallDelegate_Exception_occured_while_creating_portlet_xml);
		}
	}
}
