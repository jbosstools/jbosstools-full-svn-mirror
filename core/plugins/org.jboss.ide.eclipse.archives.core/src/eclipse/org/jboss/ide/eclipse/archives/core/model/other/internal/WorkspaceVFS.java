package org.jboss.ide.eclipse.archives.core.model.other.internal;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.jboss.ide.eclipse.archives.core.ArchivesCorePlugin;
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
	
	public WorkspaceVariableManager getManager() {
		return manager;
	}
	
	public class WorkspaceVariableManager implements IVariableManager {
		private static final String PREFIX = "org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspaceVariableManager.";
		public boolean containsVariable(String variable) {
			IEclipsePreferences prefs = new DefaultScope().getNode(ArchivesCorePlugin.PLUGIN_ID);
			return prefs.get(PREFIX + variable, null) != null;
		}

		public String getVariableValue(String variable) {
			IEclipsePreferences prefs = new DefaultScope().getNode(ArchivesCorePlugin.PLUGIN_ID);
			return prefs.get(PREFIX + variable, null);
		}
		
		
		public void setValue(String name, IPath value) throws CoreException {
			try {
				IEclipsePreferences prefs = new DefaultScope().getNode(ArchivesCorePlugin.PLUGIN_ID);
				if( value != null )
					prefs.put(PREFIX + name, value.toString());
				else 
					prefs.remove(PREFIX + name);
				prefs.flush();
			} catch (org.osgi.service.prefs.BackingStoreException e) { 
				e.printStackTrace();
			}
		}
	}
	
	public String performStringSubstitution(String expression,
			boolean reportUndefinedVariables) throws CoreException {
		return performStringSubstitution(expression, null, reportUndefinedVariables);
	}

	public String performStringSubstitution(String expression,
			String projectName, boolean reportUndefinedVariables)
			throws CoreException {
		// set this project name
		if( projectName != null ) {
			manager.setValue("current.project", new Path(projectName));
		}
		
		String ret = engine.performStringSubstitution(expression, reportUndefinedVariables, manager);

		if( projectName != null ) {
			manager.setValue("current.project", null);
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

	public String findProject(IPath absolutePath) {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for( int i = 0; i < projects.length; i++ ) 
			if( projects[i].getLocation().equals(absolutePath))
				return projects[i].getName();
		return null;
	}
}
