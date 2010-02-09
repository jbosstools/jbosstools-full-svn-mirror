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

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryInstallDelegate;
import org.eclipse.jst.j2ee.classpathdep.ClasspathDependencyUtil;
import org.eclipse.jst.j2ee.classpathdep.IClasspathDependencyConstants;
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
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetDataModelProperties;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectWorkingCopy;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.server.core.IRuntime;
import org.jboss.ide.eclipse.as.core.server.IJBossServerConstants;
import org.jboss.ide.eclipse.as.core.server.IJBossServerRuntime;
import org.jboss.tools.portlet.core.IJBossWebUtil;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.JBossWebUtil;
import org.jboss.tools.portlet.core.JBossWebUtil25;
import org.jboss.tools.portlet.core.Messages;
import org.jboss.tools.portlet.core.PortletCoreActivator;

/**
 * @author snjeza
 * 
 */
public class JSFPortletFacetInstallDelegate implements IDelegate {

	private static final String ORG_JBOSS_PORTLET_STATE_MANAGER = "org.jboss.portletbridge.application.PortletStateManager"; //$NON-NLS-1$
	private static final String ORG_JBOSS_PORTLET_VIEW_HANDLER = "org.jboss.portletbridge.application.PortletViewHandler"; //$NON-NLS-1$

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
								.getStatus(Messages.JSFPortletFacetInstallDelegate_Missing_configuration));
			}

			// check whether web.xml is available for update
			final IModelProvider provider = PortletCoreActivator
					.getModelProvider(project);
			if (provider == null) {
				throw new CoreException(
						PortletCoreActivator
								.getStatus(Messages.JSFPortletFacetInstallDelegate_Cannot_configure_web_module_for_JBoss_JSF_Portlet_Facet));
			} else if (!(provider.validateEdit(null, null).isOK())) {
				if (!(provider.validateEdit(null, null).isOK())) {
					throw new CoreException(PortletCoreActivator
							.getStatus(Messages.JSFPortletFacetInstallDelegate_The_web_xml_file_is_not_updateable));
				}
			}

			configureFacesConfig(project, monitor, config);

			//configureClassPath(project, monitor, config);
			
			//Configure libraries
			( (LibraryInstallDelegate) config.getProperty( IPortletConstants.JSFPORTLET_LIBRARY_PROVIDER_DELEGATE ) ).execute( new NullProgressMonitor() );
			

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
		if (facesConfigString == null || facesConfigString.trim().length() <= 0) {
			return;
		}
		StringTokenizer tokenizer = new StringTokenizer(facesConfigString, ","); //$NON-NLS-1$
		List<String> facesConfigs = new ArrayList<String>();
		while (tokenizer.hasMoreTokens()) {
			facesConfigs.add(tokenizer.nextToken().trim());
		}
		FacesState state = checkState(project, facesConfigs);
		if (state.applicationExists && state.viewHandlerExists && state.stateManagerExists) {
			return;
		}
		if (!state.applicationExists) {
			FacesConfigArtifactEdit facesConfigEdit = null;
			try {
				facesConfigEdit = FacesConfigArtifactEdit
						.getFacesConfigArtifactEditForWrite(project,
								facesConfigs.get(0));
				FacesConfigType facesConfig = facesConfigEdit.getFacesConfig();
				EList applications = facesConfig.getApplication();
				ApplicationType application = FacesConfigFactory.eINSTANCE.createApplicationType();
				state.application = application;
				state.facesConfigString = facesConfigs.get(0);
				facesConfig.getApplication().add(application);
				facesConfigEdit.save(monitor);
			} finally {
				if (facesConfigEdit != null) {
					facesConfigEdit.dispose();
				}
			}
		}
		FacesConfigArtifactEdit facesConfigEdit = null;
		try {
			facesConfigEdit = FacesConfigArtifactEdit.getFacesConfigArtifactEditForWrite(project,state.facesConfigString);
			FacesConfigType facesConfig = facesConfigEdit.getFacesConfig();
			if (!state.viewHandlerExists) {
				ViewHandlerType viewHandler = FacesConfigFactory.eINSTANCE.createViewHandlerType();
				viewHandler.setTextContent(ORG_JBOSS_PORTLET_VIEW_HANDLER);
				state.application.getViewHandler().add(viewHandler);
			}
			if (!state.stateManagerExists) {
				StateManagerType stateManager = FacesConfigFactory.eINSTANCE.createStateManagerType();
				stateManager.setTextContent(ORG_JBOSS_PORTLET_STATE_MANAGER);
				state.application.getStateManager().add(stateManager);
			}
			facesConfigEdit.save(monitor);

		} finally {
			if (facesConfigEdit != null) {
				facesConfigEdit.dispose();
			}
		}
	}

	private FacesState checkState(IProject project, List<String> facesConfigs) {
		FacesState facesState = new FacesState();
		for (String facesConfigString : facesConfigs) {
			FacesConfigArtifactEdit facesConfigEdit = null;
			try {
				facesConfigEdit = FacesConfigArtifactEdit.getFacesConfigArtifactEditForRead(project,facesConfigString);
				FacesConfigType facesConfig = facesConfigEdit.getFacesConfig();
				EList applications = facesConfig.getApplication();
				if (applications.size() <= 0) {
					continue;
				} else {
					facesState.applicationExists = true;
					facesState.application = (ApplicationType) applications.get(0);
					facesState.facesConfigString = facesConfigString;
				}
				for (Iterator iterator = applications.iterator(); iterator.hasNext();) {
					ApplicationType application = (ApplicationType) iterator.next();
					EList viewHandlers = application.getViewHandler();
					for (Iterator iterator2 = viewHandlers.iterator(); iterator2.hasNext();) {
						ViewHandlerType viewHandler = (ViewHandlerType) iterator2.next();
						if (ORG_JBOSS_PORTLET_VIEW_HANDLER.equals(viewHandler.getTextContent())) {
							facesState.viewHandlerExists = true;
						}
					}	
				}
				for (Iterator iterator = applications.iterator(); iterator.hasNext();) {
					ApplicationType application = (ApplicationType) iterator.next();
					EList stateManagers = application.getStateManager();
					for (Iterator iterator2 = stateManagers.iterator(); iterator2.hasNext();) {
						StateManagerType stateManager = (StateManagerType) iterator2.next();
						if (ORG_JBOSS_PORTLET_STATE_MANAGER.equals(stateManager.getTextContent())) {
							facesState.stateManagerExists = true;
						}
					}
				}
				if (facesState.applicationExists && facesState.viewHandlerExists && facesState.stateManagerExists) {
					break;
				}
			} finally {
				if (facesConfigEdit != null) {
					facesConfigEdit.dispose();
				}
			}
		}
		return facesState;
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
				String name = "org.ajax4jsf.VIEW_HANDLERS"; //$NON-NLS-1$
				String value = "org.jboss.portletbridge.application.FaceletPortletViewHandler"; //$NON-NLS-1$
				String description = null;
				util.configureContextParam(project, monitor, name, value,
						description);

				name = "javax.portlet.faces.renderPolicy"; //$NON-NLS-1$
				value = "ALWAYS_DELEGATE"; //$NON-NLS-1$
				util.configureContextParam(project, monitor, name, value,
						description);

				// RichFaces settings

				name = "org.richfaces.LoadStyleStrategy"; //$NON-NLS-1$
				value = "NONE"; //$NON-NLS-1$
				util.configureContextParam(project, monitor, name, value,
						description);

				name = "org.richfaces.LoadScriptStrategy"; //$NON-NLS-1$
				value = "NONE"; //$NON-NLS-1$
				util.configureContextParam(project, monitor, name, value,
						description);

				name = "org.ajax4jsf.RESOURCE_URI_PREFIX"; //$NON-NLS-1$
				value = "rfRes"; //$NON-NLS-1$
				util.configureContextParam(project, monitor, name, value,
						description);

				String displayName = "Ajax4jsf Filter"; //$NON-NLS-1$
				String filterName = "ajax4jsf"; //$NON-NLS-1$
				String className = "org.ajax4jsf.Filter"; //$NON-NLS-1$
				util.configureFilter(project, monitor, filterName, className,
						displayName, description);

				String servletName = util.findJsfServlet(provider
						.getModelObject());
				if (servletName == null) {
					RuntimeException e = new RuntimeException(
							Messages.JSFPortletFacetInstallDelegate_Cannot_find_the_JSF_servlet);
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
		
		String implementationLibrary = config.getStringProperty(IPortletConstants.IMPLEMENTATION_LIBRARY);

		if (IPortletConstants.LIBRARIES_PROVIDED_BY_PORTLETBRIDGE.equals(implementationLibrary)) {
			addLibrariesFromPortletBridgeRuntime(project,monitor,config);
		} else if (IPortletConstants.LIBRARIES_PROVIDED_BY_SERVER_RUNTIME.equals(implementationLibrary)) {
			addLibrariesFromServerRuntime(project,monitor,config);
		} else if (IPortletConstants.USER_LIBRARY.equals(implementationLibrary)) {
			addUserLibrary(project,monitor,config);
		}
		
	}

	private void addUserLibrary(IProject project, IProgressMonitor monitor,
			IDataModel config) throws JavaModelException {
		String userLibraryName = config.getStringProperty(IPortletConstants.USER_LIBRARY_NAME);
		IPath containerPath = new Path(JavaCore.USER_LIBRARY_CONTAINER_ID).append(userLibraryName);
		IJavaProject javaProject = JavaCore.create(project);
		IClasspathAttribute dependencyAttribute = JavaCore.newClasspathAttribute(
				IClasspathDependencyConstants.CLASSPATH_COMPONENT_DEPENDENCY,
				ClasspathDependencyUtil.getDefaultRuntimePath(
						true).toString());
		IClasspathEntry[] oldClasspathEntries = javaProject.getRawClasspath();
		IClasspathEntry[] newClasspathEntries = null;
		for (int i = 0; i < oldClasspathEntries.length; i++) {
			IClasspathEntry entry = oldClasspathEntries[i];
			if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER &&
					containerPath.equals(entry.getPath())) {
				LinkedHashMap<String, IClasspathAttribute> attrs = new LinkedHashMap<String, IClasspathAttribute>();
		        for(IClasspathAttribute attr : entry.getExtraAttributes()) {
		          attrs.put(attr.getName(), attr);
		        }
		        attrs.put(dependencyAttribute.getName(), dependencyAttribute);
		        IClasspathAttribute[] newAttrs = attrs.values().toArray(new IClasspathAttribute[attrs.size()]);
		        entry = JavaCore.newContainerEntry(entry.getPath(), entry.getAccessRules(), newAttrs, entry.isExported());
		        oldClasspathEntries[i] = entry;
		        newClasspathEntries = oldClasspathEntries;
				break;
			}
		}
		if (newClasspathEntries == null) {
			newClasspathEntries = new IClasspathEntry[oldClasspathEntries.length + 1];
			for (int i = 0; i < oldClasspathEntries.length; i++) {
				newClasspathEntries[i] = oldClasspathEntries[i];
			}
			IClasspathEntry newClasspath = JavaCore.newContainerEntry(containerPath, null,
					new IClasspathAttribute[] { dependencyAttribute }, true);
			newClasspathEntries[oldClasspathEntries.length] = newClasspath;
		}
		javaProject.setRawClasspath(newClasspathEntries, monitor);
	}

	private void addLibrariesFromServerRuntime(IProject project,
			IProgressMonitor monitor, IDataModel config) {
		IFacetedProjectWorkingCopy fpwc = (IFacetedProjectWorkingCopy) config.getProperty(IFacetDataModelProperties.FACETED_PROJECT_WORKING_COPY);
		
		org.eclipse.wst.common.project.facet.core.runtime.IRuntime facetRuntime = fpwc.getPrimaryRuntime();
		IRuntime runtime = PortletCoreActivator.getRuntime(facetRuntime);
		IJBossServerRuntime jbossRuntime = (IJBossServerRuntime)runtime.loadAdapter(IJBossServerRuntime.class, new NullProgressMonitor());
		if (jbossRuntime != null) {
			// JBoss Portal server
			IPath jbossLocation = runtime.getLocation();
			IPath configPath = jbossLocation.append(IJBossServerConstants.SERVER).append(jbossRuntime.getJBossConfiguration());
			IPath portletLib = configPath.append(IPortletConstants.PORTLET_SAR_LIB);
			File portletLibFile = portletLib.toFile();
			String[] files = getPortletbridgeLibraries(portletLibFile);
			if (files == null) {
				portletLib = configPath.append(IPortletConstants.PORTLET_SAR_HA_LIB);
				portletLibFile = portletLib.toFile();
				files = getPortletbridgeLibraries(portletLibFile);
					
			}
			if (files != null) {
				try {
					List<File> filesToImport = new ArrayList<File>();

					for (int i = 0; i < files.length; i++) {
						filesToImport.add(new File(portletLibFile, files[i]));
					}
					IVirtualComponent component = ComponentCore
							.createComponent(project);
					IVirtualFile libVirtualFile = component.getRootFolder()
							.getFile(IPortletConstants.WEB_INF_LIB);

					IFile folder = libVirtualFile.getUnderlyingFile();

					File sourceFolder = new File(portletLib.toOSString());
					ImportOperation importOperation = new ImportOperation(
							folder.getFullPath(), sourceFolder,
							FileSystemStructureProvider.INSTANCE,
							PortletCoreActivator.OVERWRITE_ALL_QUERY,
							filesToImport);
					importOperation.setCreateContainerStructure(false);
					importOperation.run(monitor);
				} catch (Exception e) {
					PortletCoreActivator
					.log(e, Messages.JSFPortletFacetInstallDelegate_Error_loading_classpath_container);
				}
			}
		}
		
	}

	private String[] getPortletbridgeLibraries(File file) {
		if (file != null && file.isDirectory()) {
			String[] list = file.list(new FilenameFilter() {

				public boolean accept(File dir, String name) {
					if ("portletbridge-api.jar".equals(name) || //$NON-NLS-1$
							"portletbridge-impl.jar".equals(name)) { //$NON-NLS-1$
						return true;
					}
					return false;
				}
				
			});
			return list;
		}
		return null;
	}
	private void addLibrariesFromPortletBridgeRuntime(IProject project,
			IProgressMonitor monitor, IDataModel config) {
		try {

			String pbRuntime = config
					.getStringProperty(IPortletConstants.PORTLET_BRIDGE_RUNTIME);
			if (pbRuntime != null && pbRuntime.trim().length() > 0) {
				pbRuntime = pbRuntime.trim();
				File pbFolder = new File(pbRuntime);
				if (pbFolder.exists() && pbFolder.isDirectory()) {
					String[] fileList = pbFolder.list(new FilenameFilter() {

						public boolean accept(File dir, String name) {
							if (name.startsWith("portletbridge") || name.endsWith(".jar")) { //$NON-NLS-1$ //$NON-NLS-2$
								return true;
							}
							return false;
						}

					});

					List<File> filesToImport = new ArrayList<File>();

					for (int i = 0; i < fileList.length; i++) {
						filesToImport.add(new File(pbRuntime, fileList[i]));
					}
					IVirtualComponent component = ComponentCore
							.createComponent(project);
					IVirtualFile libVirtualFile = component.getRootFolder()
							.getFile(IPortletConstants.WEB_INF_LIB);

					IFile folder = libVirtualFile.getUnderlyingFile();

					ImportOperation importOperation = new ImportOperation(
							folder.getFullPath(), pbFolder,
							FileSystemStructureProvider.INSTANCE,
							PortletCoreActivator.OVERWRITE_ALL_QUERY,
							filesToImport);
					importOperation.setCreateContainerStructure(false);
					importOperation.run(monitor);
				}
			}
		} catch (Exception e) {
			PortletCoreActivator
					.log(e, Messages.JSFPortletFacetInstallDelegate_Error_loading_classpath_container);
		}
	}

	private boolean isWebApp25(final Object webApp) {
		if (webApp instanceof WebApp
				&& ((WebApp) webApp).getVersion() == WebAppVersionType._25_LITERAL)
			return true;
		return false;
	}
	
	private class FacesState {
		private boolean applicationExists = false;
		private boolean viewHandlerExists = false;
		private boolean stateManagerExists = false;
		private ApplicationType application = null;
		private String facesConfigString = null;
	}
}
