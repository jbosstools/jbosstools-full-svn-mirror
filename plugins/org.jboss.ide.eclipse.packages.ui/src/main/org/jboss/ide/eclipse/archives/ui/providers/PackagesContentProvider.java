package org.jboss.ide.eclipse.archives.ui.providers;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveModelNode;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchivesModel;
import org.jboss.ide.eclipse.archives.ui.PackagesUIPlugin;
import org.jboss.ide.eclipse.archives.ui.PrefsInitializer;

public class PackagesContentProvider implements ITreeContentProvider {
	
	public PackagesContentProvider () {
	}
	
	public static class WrappedProject  {
		private IProject project;
		public WrappedProject (IProject proj) { project = proj; }
		public IProject getProject() { return project; }
	}

	private boolean showProjectRoot () {
		return PackagesUIPlugin.getDefault().getPluginPreferences().getBoolean(PrefsInitializer.PREF_SHOW_PROJECT_ROOT);
	}
		
	private Object[] wrapProjects(IProject[] project) {
		Object[] ret = new Object[project.length];
		for( int i = 0; i < project.length; i++ )
			ret[i] = new WrappedProject(project[i]);
		return ret;
	}
	public Object[] getChildren(Object parentElement) {
		if( parentElement instanceof ArchiveModelNode && showProjectRoot()) 
			return wrapProjects(new IProject[] { ((ArchiveModelNode)parentElement).getProject()});
		
		if( parentElement instanceof ArchiveModelNode)
			return ((ArchiveModelNode)parentElement).getAllChildren();
		
		if( parentElement instanceof ArchivesModel ) {
			// return all that's there
		}
		if( parentElement instanceof IProject ) {
			return ArchivesModel.instance().getProjectArchives((IProject)parentElement);
		}
		if( parentElement instanceof IArchiveNode ) {
			return ((IArchiveNode)parentElement).getAllChildren();
		}
		return new Object[] {};
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public void dispose() {}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
