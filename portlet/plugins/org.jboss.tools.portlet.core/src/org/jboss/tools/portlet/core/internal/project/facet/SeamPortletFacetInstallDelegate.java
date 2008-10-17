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

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.javaee.web.WebAppVersionType;
import org.eclipse.jst.jsf.facesconfig.emf.ApplicationType;
import org.eclipse.jst.jsf.facesconfig.emf.FacesConfigFactory;
import org.eclipse.jst.jsf.facesconfig.emf.FacesConfigType;
import org.eclipse.jst.jsf.facesconfig.emf.StateManagerType;
import org.eclipse.jst.jsf.facesconfig.emf.ViewHandlerType;
import org.eclipse.jst.jsf.facesconfig.util.FacesConfigArtifactEdit;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.jboss.tools.portlet.core.IJBossWebUtil;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.JBossWebUtil;
import org.jboss.tools.portlet.core.JBossWebUtil25;
import org.jboss.tools.portlet.core.PortletCoreActivator;

/**
 * @author snjeza
 * 
 */
public class SeamPortletFacetInstallDelegate implements IDelegate {

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
								.getStatus("Internal Error creating JBoss Seam Portlet Facet.  Missing configuration."));
			}

			// check whether web.xml is available for update
			final IModelProvider provider = PortletCoreActivator
					.getModelProvider(project);
			if (provider == null) {
				throw new CoreException(
						PortletCoreActivator
								.getStatus("Cannot configure web module for JBoss Seam Portlet Facet"));
			} else if (!(provider.validateEdit(null, null).isOK())) {
				if (!(provider.validateEdit(null, null).isOK())) {
					throw new CoreException(PortletCoreActivator
							.getStatus("The web.xml file is not updateable"));
				}
			}

			configureWebApp(project, monitor, config);
			
			if (monitor != null) {
				monitor.worked(1);
			}

		} finally {
			if (monitor != null) {
				monitor.done();
			}
		}
	}

	private void configureWebApp(final IProject project,
			final IProgressMonitor monitor, IDataModel config) {
		final IModelProvider provider = PortletCoreActivator
				.getModelProvider(project);
		IPath modelPath = new Path("WEB-INF").append("web.xml"); //$NON-NLS-1$ //$NON-NLS-2$
		boolean exists = project.getProjectRelativePath().append(modelPath)
				.toFile().exists();
		if (isWebApp25(provider.getModelObject()) && !exists) {
			modelPath = IModelProvider.FORCESAVE;
		}
		provider.modify(new Runnable() {
			public void run() {
				IJBossWebUtil util = null;

				if (isWebApp25(provider.getModelObject())) {
					util = new JBossWebUtil25();
				} else {
					util = new JBossWebUtil();
				}
				String name = "org.jboss.portletbridge.ExceptionHandler";
				String value = "org.jboss.portletbridge.SeamExceptionHandlerImpl";
				String description = null;
				util.configureContextParam(project, monitor, name, value,
						description);

				// FIXME optional for Seam portlets version 2.1.x and up
				name = "javax.faces.LIFECYCLE_ID";
				value = "SEAM_PORTLET";
				util.configureContextParam(project, monitor, name, value,
						description);
			}
		}, modelPath);
	}

	private boolean isWebApp25(final Object webApp) {
		if (webApp instanceof WebApp
				&& ((WebApp) webApp).getVersion() == WebAppVersionType._25_LITERAL)
			return true;
		return false;
	}
}
