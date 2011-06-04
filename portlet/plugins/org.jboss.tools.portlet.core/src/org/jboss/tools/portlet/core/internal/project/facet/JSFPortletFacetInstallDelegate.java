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
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryInstallDelegate;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.jsf.facesconfig.emf.ApplicationType;
import org.eclipse.jst.jsf.facesconfig.emf.FacesConfigFactory;
import org.eclipse.jst.jsf.facesconfig.emf.FacesConfigType;
import org.eclipse.jst.jsf.facesconfig.emf.StateManagerType;
import org.eclipse.jst.jsf.facesconfig.emf.ViewHandlerType;
import org.eclipse.jst.jsf.facesconfig.util.FacesConfigArtifactEdit;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
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

			//Configure libraries
			( (LibraryInstallDelegate) config.getProperty( IPortletConstants.JSFPORTLET_LIBRARY_PROVIDER_DELEGATE ) ).execute( monitor );
			
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
		if (PortletCoreActivator.isWebApp25(provider.getModelObject())) {
			return new JBossWebUtil25().getFacesConfig(project, monitor);
		}
		return new JBossWebUtil().getFacesConfig(project, monitor);
	}
	
	private class FacesState {
		private boolean applicationExists = false;
		private boolean viewHandlerExists = false;
		private boolean stateManagerExists = false;
		private ApplicationType application = null;
		private String facesConfigString = null;
	}
}
