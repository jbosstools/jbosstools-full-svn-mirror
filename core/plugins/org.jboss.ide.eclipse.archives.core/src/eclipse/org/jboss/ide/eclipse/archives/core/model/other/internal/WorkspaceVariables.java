package org.jboss.ide.eclipse.archives.core.model.other.internal;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.variables.VariablesPlugin;
import org.jboss.ide.eclipse.archives.core.model.IArchivesVFS;

public class WorkspaceVariables implements IArchivesVFS {
//
//	public IPath getProjectPath (String projectName) {
//		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
//		
//		if (project.exists() && project.isAccessible())
//		{
//			IPath location = project.getRawLocation();
//			if (location == null) return project.getLocation();
//			
//			return location;
//		}
//		
//		return null;
//	}

//	public boolean isDebugging(String option) {
//		return ArchivesCorePlugin.getDefault().isDebugging()
//		&& "true".equalsIgnoreCase(Platform.getDebugOption(option));
//	}
//
//	public String getProjectName(IPath path) {
//		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
//		for( int i = 0; i < projects.length; i++ ) 
//			if( projects[i].getLocation().equals(path))
//				return projects[i].getName();
//		return null;
//	}
//
	public String performStringSubstitution(String expression,
			boolean reportUndefinedVariables) throws CoreException {
		return VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(expression, reportUndefinedVariables);
	}

	public IPath[] getWorkspaceChildren(IPath path) {
		IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
		if( res != null || !(res instanceof IContainer)) {
			try {
				IResource[] resources = ((IContainer)res).members();
				IPath[] paths = new IPath[resources.length];
				for( int i = 0; i < resources.length; i++ ) 
					paths[i] = resources[i].getFullPath();
				return paths;
			} catch( CoreException ce ) {
				return new IPath[0];
			}
		}
		return new IPath[0];
	}

	public IPath workspacePathToAbsolutePath(IPath path) {
		IResource r = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
		IPath append = new Path("");
		while( r == null && path.segmentCount() > 0) {
			append = new Path(path.lastSegment()).append(append);
			path = path.removeLastSegments(1);
			r = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
		}
		if( r != null )
			return r.getLocation().append(append);
		return null;
	}

	public IPath[] workspacePathToAbsolutePath(IPath[] paths) {
		IPath[] results = new IPath[paths.length];
		for( int i = 0; i < paths.length; i++ )
			results[i] = workspacePathToAbsolutePath(paths[i]);
		return results;
	}
}
