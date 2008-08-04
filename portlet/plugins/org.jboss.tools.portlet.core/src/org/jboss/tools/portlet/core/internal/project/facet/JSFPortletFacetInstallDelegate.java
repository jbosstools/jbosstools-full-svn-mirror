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
public class JSFPortletFacetInstallDelegate implements IDelegate {

	private static final String ORG_JBOSS_PORTLET_STATE_MANAGER = "org.jboss.portletbridge.application.PortletStateManager";
	private static final String ORG_JBOSS_PORTLET_VIEW_HANDLER = "org.jboss.portletbridge.application.PortletViewHandler";

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
								.getStatus("Internal Error creating JBoss JSF Portlet Facet.  Missing configuration."));
			}

			// check whether web.xml is available for update
			final IModelProvider provider = PortletCoreActivator
					.getModelProvider(project);
			if (provider == null) {
				throw new CoreException(
						PortletCoreActivator
								.getStatus("Cannot configure web module for JBoss JSF Portlet Facet"));
			} else if (!(provider.validateEdit(null, null).isOK())) {
				if (!(provider.validateEdit(null, null).isOK())) {
					throw new CoreException(PortletCoreActivator
							.getStatus("The web.xml file is not updateable"));
				}
			}

			configureFacesConfig(project, monitor, config);

			configureClassPath(project, monitor, config);

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

	private void configureFacesConfig(IProject project,
			IProgressMonitor monitor, IDataModel config) {

		String facesConfigString = getFacesConfigFile(project, monitor);
		FacesConfigArtifactEdit facesConfigEdit = null;
		try {
			facesConfigEdit = FacesConfigArtifactEdit
					.getFacesConfigArtifactEditForWrite(project,
							facesConfigString);
			FacesConfigType facesConfig = facesConfigEdit.getFacesConfig();
			EList applications = facesConfig.getApplication();
			ApplicationType applicationType = null;
			boolean applicationExists = false;
			if (applications.size() <= 0) {
				applicationType = FacesConfigFactory.eINSTANCE
						.createApplicationType();
			} else {
				applicationType = (ApplicationType) applications.get(0);
				applicationExists = true;
			}
			boolean viewHandlerExists = false;
			for (Iterator iterator = applications.iterator(); iterator
					.hasNext();) {
				ApplicationType application = (ApplicationType) iterator.next();
				EList viewHandlers = applicationType.getViewHandler();
				for (Iterator iterator2 = viewHandlers.iterator(); iterator2
						.hasNext();) {
					ViewHandlerType viewHandler = (ViewHandlerType) iterator2
							.next();
					if (ORG_JBOSS_PORTLET_VIEW_HANDLER.equals(viewHandler
							.getTextContent())) {
						viewHandlerExists = true;
					}
				}
			}
			if (!viewHandlerExists) {
				ViewHandlerType viewHandler = FacesConfigFactory.eINSTANCE
						.createViewHandlerType();
				viewHandler.setTextContent(ORG_JBOSS_PORTLET_VIEW_HANDLER);
				applicationType.getViewHandler().add(viewHandler);
			}
			boolean stateManagerExists = false;
			for (Iterator iterator = applications.iterator(); iterator
					.hasNext();) {
				ApplicationType application = (ApplicationType) iterator.next();
				EList stateManagers = applicationType.getStateManager();
				for (Iterator iterator2 = stateManagers.iterator(); iterator2
						.hasNext();) {
					StateManagerType stateManager = (StateManagerType) iterator2
							.next();
					if (ORG_JBOSS_PORTLET_STATE_MANAGER.equals(stateManager
							.getTextContent())) {
						stateManagerExists = true;
					}
				}
			}
			if (!stateManagerExists) {
				StateManagerType stateManager = FacesConfigFactory.eINSTANCE
						.createStateManagerType();
				stateManager.setTextContent(ORG_JBOSS_PORTLET_STATE_MANAGER);
				applicationType.getStateManager().add(stateManager);
			}
			if (!applicationExists) {
				facesConfig.getApplication().add(applicationType);
			}
			facesConfigEdit.save(monitor);

		} finally {
			if (facesConfigEdit != null) {
				facesConfigEdit.dispose();
			}
		}
	}

	private String getFacesConfigFile(IProject project, IProgressMonitor monitor) {
		final IModelProvider provider = PortletCoreActivator
				.getModelProvider(project);
		if (isWebApp25(provider.getModelObject())) {
			return new JBossWebUtil25().getFacesConfig(project, monitor);
		}
		return new JBossWebUtil().getFacesConfig(project, monitor);
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
				String name = "org.ajax4jsf.VIEW_HANDLERS";
				String value = "org.jboss.portletbridge.application.FaceletPortletViewHandler";
				String description = null;
				util.configureContextParam(project, monitor, name, value,
						description);

				name = "javax.portlet.faces.renderPolicy";
				value = "ALWAYS_DELEGATE";
				util.configureContextParam(project, monitor, name, value,
						description);

				// RichFaces settings

				name = "org.richfaces.LoadStyleStrategy";
				value = "NONE";
				util.configureContextParam(project, monitor, name, value,
						description);

				name = "org.richfaces.LoadScriptStrategy";
				value = "NONE";
				util.configureContextParam(project, monitor, name, value,
						description);

				name = "org.ajax4jsf.RESOURCE_URI_PREFIX";
				value = "rfRes";
				util.configureContextParam(project, monitor, name, value,
						description);

				String displayName = "Ajax4jsf Filter";
				String filterName = "ajax4jsf";
				String className = "org.ajax4jsf.Filter";
				util.configureFilter(project, monitor, filterName, className,
						displayName, description);

				String servletName = util.findJsfServlet(provider
						.getModelObject());
				if (servletName == null) {
					RuntimeException e = new RuntimeException(
							"Cannot find the JSF servlet");
					PortletCoreActivator.log(e);
					throw e;
				}
				util.configureFilterMapping(project, monitor, filterName,
						servletName);

			}
		}, modelPath);
	}

	private void configureClassPath(final IProject project,
			final IProgressMonitor monitor, IDataModel config)
			throws JavaModelException {
		IJavaProject javaProject = JavaCore.create(project);

		boolean deployJars = config
				.getBooleanProperty(IPortletConstants.DEPLOY_JARS);
		if (deployJars) {
			try {
				URL installURL = FileLocator.toFileURL(PortletCoreActivator
						.getDefault().getBundle().getEntry("/"));
				String baseDir = installURL.getFile().toString();
				File libDir = new File(baseDir + "/"
						+ PortletCoreActivator.RESOURCES_FOLDER + "/"
						+ PortletCoreActivator.JSFPORTLET_FOLDER);
				List<File> filesToImport = Arrays.asList(libDir.listFiles());
				IVirtualComponent component = ComponentCore
						.createComponent(project);
				IVirtualFile libVirtualFile = component.getRootFolder()
						.getFile(IPortletConstants.WEB_INF_LIB);

				IFile folder = libVirtualFile.getUnderlyingFile();

				ImportOperation importOperation = new ImportOperation(folder
						.getFullPath(), libDir,
						FileSystemStructureProvider.INSTANCE,
						PortletCoreActivator.OVERWRITE_ALL_QUERY, filesToImport);
				importOperation.setCreateContainerStructure(false);
				importOperation.run(monitor);
			} catch (Exception e) {
				PortletCoreActivator
						.log(e, "Error loading classpath container");
			}
		} else {
			IPath containerPath = new Path(
					IPortletConstants.JSFPORTLET_CONTAINER_10_ID);

			IClasspathEntry entry = JavaCore.newContainerEntry(containerPath,
					true);
			IClasspathEntry[] entries = javaProject.getRawClasspath();
			IClasspathEntry[] newEntries = new IClasspathEntry[entries.length + 1];
			System.arraycopy(entries, 0, newEntries, 0, entries.length);
			newEntries[entries.length] = entry;
			javaProject.setRawClasspath(newEntries, monitor);
		}
	}

	private boolean isWebApp25(final Object webApp) {
		if (webApp instanceof WebApp
				&& ((WebApp) webApp).getVersion() == WebAppVersionType._25_LITERAL)
			return true;
		return false;
	}
}
