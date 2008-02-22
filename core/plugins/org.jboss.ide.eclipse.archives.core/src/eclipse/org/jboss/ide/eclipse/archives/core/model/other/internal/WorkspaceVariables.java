package org.jboss.ide.eclipse.archives.core.model.other.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.variables.VariablesPlugin;
import org.jboss.ide.eclipse.archives.core.ArchivesCorePlugin;
import org.jboss.ide.eclipse.archives.core.model.IRuntimeVariables;

public class WorkspaceVariables implements IRuntimeVariables {

	public IPath getProjectPath (String projectName) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		
		if (project.exists() && project.isAccessible())
		{
			IPath location = project.getRawLocation();
			if (location == null) return project.getLocation();
			
			return location;
		}
		
		return null;
	}

	public boolean isDebugging(String option) {
		return ArchivesCorePlugin.getDefault().isDebugging()
		&& "true".equalsIgnoreCase(Platform.getDebugOption(option));
	}

	public String getProjectName(IPath path) {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for( int i = 0; i < projects.length; i++ ) 
			if( projects[i].getLocation().equals(path))
				return projects[i].getName();
		return null;
	}

	public String performStringSubstitution(String expression,
			boolean reportUndefinedVariables) throws CoreException {
		return VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(expression, reportUndefinedVariables);
	}
}
