/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.seam.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jst.j2ee.ejb.componentcore.util.EJBArtifactEdit;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.seam.core.project.facet.SeamProjectPreferences;
import org.jboss.tools.seam.internal.core.project.facet.ISeamFacetDataModelProperties;

/**
 * Helper class that collects related J2EE projects for 
 * a given 'seed' project.
 * 
 * If seed project is EAR, it's referenced projects are used to fill 
 * lists with WAR and EJB projects.
 * 
 * If seed project is referenced by a EAR project (the first occurrence is taken),
 * that EAR is used as seed project.
 *  
 * If seed project is WAR or EJB not referenced by any EAR project,
 * field 'ear' remains null, and only lists 'wars' and 'ejbs' are available.
 * 
 * Also this class provides helper methods to obtain root folders 
 * for involved EAR, WAR and EJB projects.
 * 
 * @author Viacheslav Kabanovich
 */
public class SeamProjectsSet {
	IProject ear;
	IProject war;
	IProject ejb;
	IProject test;
	IEclipsePreferences prefs;

	/**
	 * @param project
	 * @return
	 */
	public static SeamProjectsSet create(IProject project) {
		return new SeamProjectsSet(project);
	}

	public SeamProjectsSet(IProject project) {
		
		IScopeContext projectScope = new ProjectScope(project);
		prefs = projectScope.getNode(SeamCorePlugin.PLUGIN_ID);
		
		war = project;
		
		String earName = prefs.get(
				ISeamFacetDataModelProperties.SEAM_EAR_PROJECT,project.getName()+"-ear"); //$NON-NLS-1$
		if(earName!=null && !"".equals(earName.trim())) { //$NON-NLS-1$
			ear = (IProject)project.getWorkspace().getRoot().findMember(earName);
		}
		String ejbName = prefs.get(
				ISeamFacetDataModelProperties.SEAM_EJB_PROJECT,project.getName()+"-ejb"); //$NON-NLS-1$
		if(ejbName!=null && !"".equals(ejbName.trim())) { //$NON-NLS-1$
			ejb = (IProject)project.getWorkspace().getRoot().findMember(ejbName);
		}
		String testName = prefs.get(
				ISeamFacetDataModelProperties.SEAM_TEST_PROJECT,project.getName()+"test"); //$NON-NLS-1$
		if(testName!=null && !"".equals(testName)) { //$NON-NLS-1$
			test = (IProject)project.getWorkspace().getRoot().findMember(testName);
		}
	}
	
	public boolean isWarConfiguration() {
		return prefs.get(
				ISeamFacetDataModelProperties.JBOSS_AS_DEPLOY_AS, 
				ISeamFacetDataModelProperties.DEPLOY_AS_WAR)
					.equals(ISeamFacetDataModelProperties.DEPLOY_AS_WAR);
	}
	
	/**
	 * Returns list of WAR projects.
	 * @return
	 */
	public IProject getWarProject() {
		return war;
	}
	
	/**
	 * Returns EAR project or null, if WAR project is not used by EAR.
	 * @return
	 */
	public IProject getEarProject() {
		return ear;
	}
	
	/**
	 * Returns list of EJB projects.
	 * @return
	 */
	public IProject getEjbProject() {
		return ejb;
	}
	
	/**
	 * Returns list of EJB projects.
	 * @return
	 */
	public IProject getTestProject() {
		return test;
	}
	
	/**
	 * Returns Content folder of EAR project or null 
	 * if EAR is not available.
	 * @return
	 */	
	public IFolder getActionsFolder() {
		IFolder actionsFolder = null;
		if(isWarConfiguration()) {
			IVirtualComponent com = ComponentCore.createComponent(war);
			IVirtualFolder webRootFolder = com.getRootFolder().getFolder(new Path("/")); //$NON-NLS-1$
			final IVirtualFolder srcRootFolder = com.getRootFolder().getFolder(new Path("/WEB-INF/classes")); //$NON-NLS-1$
			IContainer[] folder = webRootFolder.getUnderlyingFolders();
			if(folder.length==1) {
				actionsFolder = (IFolder)folder[0];
			} else if(folder.length>1) {
				IContainer parent = folder[0].getParent();
				IResource actions = parent.findMember("actions"); //$NON-NLS-1$
				if(actions!=null && actions instanceof IFolder) {
					actionsFolder = (IFolder)actions;
				} else {
					actionsFolder = (IFolder)folder[0];
				}
			}
		} else {
			IVirtualComponent com = ComponentCore.createComponent(ejb);
			IVirtualFolder ejbRootFolder = com.getRootFolder().getFolder(new Path("/")); //$NON-NLS-1$
			actionsFolder = (IFolder)ejbRootFolder.getUnderlyingFolder();
		}
		return actionsFolder; 
	}
	
	/**
	 * Returns Content folder for first found WAR project. 
	 * @return
	 */
	public IFolder getBeansFolder() {
		IFolder actionsFolder = null;
		if(isWarConfiguration()) {
			IVirtualComponent com = ComponentCore.createComponent(war);
			final IVirtualFolder srcRootFolder = com.getRootFolder().getFolder(new Path("/WEB-INF/classes")); //$NON-NLS-1$
			IContainer[] folder = srcRootFolder.getUnderlyingFolders();
			if(folder.length==1) {
				actionsFolder = (IFolder)folder[0];
			} else if(folder.length>1) {
				IContainer parent = folder[0].getParent();
				IResource actions = parent.findMember("model"); //$NON-NLS-1$
				if(actions!=null && actions instanceof IFolder) {
					actionsFolder = (IFolder)actions;
				} else {
					actionsFolder = (IFolder)folder[0];
				}
			}
		} else {
			IVirtualComponent com = ComponentCore.createComponent(ejb);
			IVirtualFolder ejbRootFolder = com.getRootFolder().getFolder(new Path("/")); //$NON-NLS-1$
			actionsFolder = (IFolder)ejbRootFolder.getUnderlyingFolder();
		}
		return actionsFolder; 
	}
	
	/**
	 * Returns source roots for first found EJB project.
	 * @return
	 */
	public IFolder getViewsFolder() {
		IVirtualComponent com = ComponentCore.createComponent(war);
		IVirtualFolder webRootFolder = com.getRootFolder().getFolder(new Path("/")); //$NON-NLS-1$
		return (IFolder)webRootFolder.getUnderlyingFolder();
	}
	
	/**
	 * Returns source roots for first found EJB project.
	 * @return
	 */
	public IFolder getTestsFolder() {
		IResource testRes = test.findMember("test-src"); //$NON-NLS-1$
		IFolder testFolder = null;
		if(testRes instanceof IFolder) {
			testFolder = (IFolder)testRes;
		}
		return  testFolder;
	}
	
	public String getEntityPackage(){
		return prefs.get(ISeamFacetDataModelProperties.ENTITY_BEAN_PACKAGE_NAME, "entity"); //$NON-NLS-1$
	}
	
	public void refreshLocal(IProgressMonitor monitor) throws CoreException {
		if(ejb!=null) { 
			ejb.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		}
		if(test!=null) {
			test.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		}
		if(war!=null) {
			war.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		}
	}
}