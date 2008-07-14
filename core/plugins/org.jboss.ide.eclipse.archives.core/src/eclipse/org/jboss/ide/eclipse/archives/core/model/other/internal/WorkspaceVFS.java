package org.jboss.ide.eclipse.archives.core.model.other.internal;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.model.IArchivesVFS;
import org.jboss.ide.eclipse.archives.core.model.IVariableManager;
import org.jboss.ide.eclipse.archives.core.xpl.StringSubstitutionEngineClone;

public class WorkspaceVFS implements IArchivesVFS {
	protected WorkspaceVariableManager manager;
	protected StringSubstitutionEngineClone engine;
	public WorkspaceVFS() {
		manager = new WorkspaceVariableManager();
		engine = new StringSubstitutionEngineClone();
	}
	
	public WorkspaceVariableManager getVariableManager() {
		return manager;
	}
	
	public String performStringSubstitution(String expression,
			boolean reportUndefinedVariables) throws CoreException {
		return performStringSubstitution(expression, null, reportUndefinedVariables);
	}

	public String performStringSubstitution(String expression,
			String projectName, boolean reportUndefinedVariables)
			throws CoreException {
		// set this project name
		if( expression == null )
			return null;
		
		if( projectName != null ) {
			manager.setValue(IVariableManager.CURRENT_PROJECT, projectName);
		}
		
		String ret = engine.performStringSubstitution(expression, reportUndefinedVariables, manager);

		if( projectName != null ) {
			manager.setValue(IVariableManager.CURRENT_PROJECT, null);
		}
		return ret;
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

	public String getProjectName(IPath absolutePath) {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for( int i = 0; i < projects.length; i++ ) 
			if( projects[i].getLocation().equals(absolutePath))
				return projects[i].getName();
		return null;
	}
}
