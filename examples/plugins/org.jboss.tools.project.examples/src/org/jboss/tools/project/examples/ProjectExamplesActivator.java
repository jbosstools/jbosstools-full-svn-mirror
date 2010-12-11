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
package org.jboss.tools.project.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.validation.internal.operations.ValidationBuilder;
import org.jboss.tools.project.examples.fixes.ProjectExamplesFix;
import org.jboss.tools.project.examples.fixes.SeamRuntimeFix;
import org.jboss.tools.project.examples.fixes.WTPRuntimeFix;
import org.jboss.tools.project.examples.model.IImportProjectExample;
import org.jboss.tools.project.examples.model.Project;
import org.jboss.tools.project.examples.model.ProjectFix;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ProjectExamplesActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.project.examples"; //$NON-NLS-1$
	public static final String ALL_SITES = Messages.ProjectExamplesActivator_All;
	public static final String SHOW_EXPERIMENTAL_SITES = "showExperimentalSites"; //$NON-NLS-1$
	public static final String USER_SITES = "userSites"; //$NON-NLS-1$
	public static final boolean SHOW_EXPERIMENTAL_SITES_VALUE = false;
	public static final String SHOW_INVALID_SITES = "invalidSites"; //$NON-NLS-1$
	public static final boolean SHOW_INVALID_SITES_VALUE = true;
	private static final String IMPORT_PROJECT_EXAMPLES_EXTENSION_ID = "org.jboss.tools.project.examples.importProjectExamples"; //$NON-NLS-1$
	private static final String NAME = "name"; //$NON-NLS-1$
	private static final String TYPE = "type"; //$NON-NLS-1$
	
	// The shared instance
	private static ProjectExamplesActivator plugin;

	private static BundleContext context;

	public static Job waitForBuildAndValidation = new Job(Messages.ProjectExamplesActivator_Waiting) {

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			try {
				Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD,
						monitor);
				Job.getJobManager().join(
						ValidationBuilder.FAMILY_VALIDATION_JOB, monitor);
			} catch (OperationCanceledException e) {
				return Status.CANCEL_STATUS;
			} catch (InterruptedException e) {
				return Status.CANCEL_STATUS;
			}
			return Status.OK_STATUS;
		}

	};
	private Map<String, IImportProjectExample> importProjectExamplesMap;

	/**
	 * The constructor
	 */
	public ProjectExamplesActivator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		ProjectExamplesActivator.context = context;
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		context = null;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static ProjectExamplesActivator getDefault() {
		return plugin;
	}

	public static void log(Throwable e) {
		IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, e
				.getLocalizedMessage(), e);
		ProjectExamplesActivator.getDefault().getLog().log(status);
	}
	
	public static void log(String message) {
		IStatus status = new Status(IStatus.WARNING, PLUGIN_ID,message);
		ProjectExamplesActivator.getDefault().getLog().log(status);
	}

	public static BundleContext getBundleContext() {
		return context;
	}

	public static List<IMarker> getMarkers(List<Project> projects) {
		List<IMarker> markers = new ArrayList<IMarker>();
		for (Project project : projects) {
			try {
				if (project.getIncludedProjects() == null) {
					String projectName = project.getName();
					getMarkers(markers, projectName);
				} else {
					List<String> includedProjects = project.getIncludedProjects();
					for (String projectName:includedProjects) {
						getMarkers(markers, projectName);
					}
				}
			} catch (CoreException e) {
				ProjectExamplesActivator.log(e);
			}
		}
		return markers;
	}

	private static List<IMarker> getMarkers(List<IMarker> markers,
			String projectName) throws CoreException {
		IProject eclipseProject = ResourcesPlugin.getWorkspace()
				.getRoot().getProject(projectName);
		IMarker[] projectMarkers = eclipseProject.findMarkers(
				IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
		for (int i = 0; i < projectMarkers.length; i++) {
			if (projectMarkers[i].getAttribute(IMarker.SEVERITY,
					IMarker.SEVERITY_ERROR) == IMarker.SEVERITY_ERROR) {
				markers.add(projectMarkers[i]);
			}
		}
		return markers;
	}
	
	public static IProject[] getEclipseProject(Project project,
			ProjectFix fix) {
		String pName = fix.getProperties().get(
				ProjectFix.ECLIPSE_PROJECTS);
		if (pName == null) {
			List<String> projectNames = project.getIncludedProjects();
			List<IProject> projects = new ArrayList<IProject>();
			for (String projectName:projectNames) {
				IProject eclipseProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
				if (eclipseProject != null && eclipseProject.isOpen()) {
					projects.add(eclipseProject);
				}
			}
			return projects.toArray(new IProject[0]);
		}
		StringTokenizer tokenizer = new StringTokenizer(pName,","); //$NON-NLS-1$
		List<IProject> projects = new ArrayList<IProject>();
		while (tokenizer.hasMoreTokens()) {
			String projectName = tokenizer.nextToken().trim();
			if (projectName != null && projectName.length() > 0) {
				IProject eclipseProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
				if (eclipseProject != null && eclipseProject.isOpen()) {
					projects.add(eclipseProject);
				}
			}
		}
		return projects.toArray(new IProject[0]);
	}

	public IImportProjectExample getImportProjectExample(String importType) {
		initImportProjectExamples();
		return importProjectExamplesMap.get(importType);
	}

	private void initImportProjectExamples() {
		if (importProjectExamplesMap == null) {
			importProjectExamplesMap = new HashMap<String,IImportProjectExample>();
			IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = registry
					.getExtensionPoint(IMPORT_PROJECT_EXAMPLES_EXTENSION_ID);
			IExtension[] extensions = extensionPoint.getExtensions();
			for (int i = 0; i < extensions.length; i++) {
				IExtension extension = extensions[i];
				IConfigurationElement[] configurationElements = extension
						.getConfigurationElements();
				for (int j = 0; j < configurationElements.length; j++) {
					IConfigurationElement configurationElement = configurationElements[j];
					IImportProjectExample importProjectExample;
					try {
						importProjectExample = (IImportProjectExample) configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
					} catch (CoreException e) {
						log(e);
						continue;
					}
					String name = configurationElement.getAttribute(NAME);
					String type = configurationElement.getAttribute(TYPE);
					importProjectExample.setName(name);
					importProjectExample.setType(type);
					importProjectExamplesMap.put(type, importProjectExample);
				}
			}
				
		}
	}
	
	public static void fix(Project project, IProgressMonitor monitor) {
		List<ProjectFix> fixes = project.getFixes();
		for (ProjectFix fix:fixes) {
			ProjectExamplesFix projectExamplesFix = ProjectExamplesFixFactory.getProjectFix(fix);
			if (projectExamplesFix != null) {
				projectExamplesFix.fix(project, fix, monitor);
			}
		}
	}
	
	private static class ProjectExamplesFixFactory {
		public static ProjectExamplesFix getProjectFix(ProjectFix fix) {
			if (ProjectFix.WTP_RUNTIME.equals(fix.getType())) {
				return new WTPRuntimeFix();
			}
			if (ProjectFix.SEAM_RUNTIME.equals(fix.getType())) {
				return new SeamRuntimeFix();
			}
			return null;
		}
	}
}
