package org.jboss.tools.forge.importer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.m2e.core.MavenPlugin;

public class ProjectConfigurationUpdater {
	
	public static void updateProject(final IProject project) {		
	    Job job = new WorkspaceJob("Updating project configuration") {
	        public IStatus runInWorkspace(IProgressMonitor monitor) {
	          try {
	        	  MavenPlugin.getProjectConfigurationManager().updateProjectConfiguration(
	        			  project, 
	        			  monitor);
	          } catch(CoreException ex) {
	            return ex.getStatus();
	          }
	          return Status.OK_STATUS;
	        }
	      };
	      job.setRule(MavenPlugin.getProjectConfigurationManager().getRule());
	      job.schedule();
	}
	
}
