/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.eclipse.console.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.eclipse.launch.IConsoleConfigurationLaunchConstants;
import org.hibernate.util.StringHelper;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class ProjectUtils {

	private ProjectUtils() {
		
	}
	
	public static boolean toggleHibernateOnProject(IProject project, boolean enable,String defaultConsoleName) {
		IScopeContext scope = new ProjectScope(project);
		
		Preferences node = scope.getNode("org.hibernate.eclipse.console");
		
		if(node!=null) {
			node.putBoolean("hibernate3.enabled", enable );
			node.put("default.configuration", defaultConsoleName );
			try {
				node.flush();
			} catch (BackingStoreException e) {
				HibernateConsolePlugin.getDefault().logErrorMessage("Could not save changes to preferences", e);
				return false;
			}
		} else {
			return false;
		}
		
		try {
			if(enable) {
				return ProjectUtils.addProjectNature(project, "org.hibernate.eclipse.console.hibernateNature", new NullProgressMonitor() );
			} else {
				return ProjectUtils.removeProjectNature(project, "org.hibernate.eclipse.console.hibernateNature", new NullProgressMonitor() );
			}
		} catch(CoreException ce) {
			HibernateConsolePlugin.getDefault().logErrorMessage("Could not activate Hibernate nature on project " + project.getName(), ce);
			HibernateConsolePlugin.getDefault().log(ce.getStatus() );
			return false;
		}
		
	}
	
	/**
	 * Add the given project nature to the given project (if it isn't already added).
	 * @return true if nature where added, false if not
	 * @throws OperationCanceledException if job were cancelled or CoreException if something went wrong. 
	 */
	public static boolean addProjectNature(IProject project, String nature, IProgressMonitor monitor) throws CoreException {
		if (monitor != null && monitor.isCanceled() ) {
			throw new OperationCanceledException();
		}
		
		if (!project.hasNature(nature) ) {
			IProjectDescription description = project.getDescription();
			String[] prevNatures= description.getNatureIds();
			String[] newNatures= new String[prevNatures.length + 1];
			System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
			newNatures[prevNatures.length]= nature;
			description.setNatureIds(newNatures);
			project.setDescription(description, monitor);
			return true;
		} else {
			monitor.worked(1);
			return false;
		}
	}

	public static boolean removeProjectNature(IProject project, String nature, NullProgressMonitor monitor) throws CoreException {
		if (monitor != null && monitor.isCanceled() ) {
			throw new OperationCanceledException();
		}
		
		if (project.hasNature(nature) ) {
			IProjectDescription description = project.getDescription();
			
			String[] natures = description.getNatureIds();
	        String[] newNatures = new String[natures.length - 1];
	        for(int i = 0; i < natures.length; i++) {
	            if (!natures[i].equals(nature) )
	                newNatures[i] = natures[i];
	        }
	        description.setNatureIds(newNatures);
	        project.setDescription(description, monitor);
			return true;
		} else {
			monitor.worked(1);
			return false;
		}
	}

	static public IJavaProject findJavaProject(IEditorPart part) {
		if(part!=null) return findJavaProject(part.getEditorInput());
		return null;
	}

	static public IJavaProject findJavaProject(IEditorInput input) {
		if(input!=null && input instanceof IFileEditorInput) {
	         IFile file = null;
	         IProject project = null;
	         IJavaProject jProject = null;
	         
	         IFileEditorInput fileInput = (IFileEditorInput) input;
	         file = fileInput.getFile();
	         project = file.getProject();
	         jProject = JavaCore.create(project);
	
	         return jProject;
	      }
	
		return null;
	}	
	
	static public IJavaProject findJavaProject(String name) {
		if(StringHelper.isEmpty( name )) {
			return null;
		}
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = null;
		project = root.getProject(name);
		if (project != null) {
			return JavaCore.create(project);
		} else {
			return null;
		}
	}

	public static IJavaProject findJavaProject(ConsoleConfiguration consoleConfiguration) {
		IJavaProject proj = null;
		if (consoleConfiguration != null) {
			ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
			ILaunchConfigurationType launchConfigurationType = launchManager.getLaunchConfigurationType( "org.hibernate.eclipse.launch.ConsoleConfigurationLaunchConfigurationType" );
			ILaunchConfiguration[] launchConfigurations;
			try {
				launchConfigurations = launchManager.getLaunchConfigurations( launchConfigurationType );
				for (int i = 0; i < launchConfigurations.length; i++) { // can't believe there is no look up by name API
					ILaunchConfiguration launchConfiguration = launchConfigurations[i];
					if(launchConfiguration.getName().equals(consoleConfiguration.getName())) {
						proj = ProjectUtils.findJavaProject(launchConfiguration.getAttribute(IConsoleConfigurationLaunchConstants.PROJECT_NAME, ""));
					}
				}								
			} catch (CoreException e1) {
				HibernateConsolePlugin.getDefault().log(e1);
			}
		}
		return proj;
	}
}