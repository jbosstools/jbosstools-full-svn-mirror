package org.jboss.ide.eclipse.archives.ui.providers;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.ui.PrefsInitializer;
import org.jboss.ide.eclipse.archives.ui.views.ProjectArchivesView;

public class ArchivesContentProvider implements ITreeContentProvider {
	
	public ArchivesContentProvider () {
	}
	
	public static class WrappedProject  {
		private IProject project;
		public WrappedProject (IProject proj) { project = proj; }
		public IProject getProject() { return project; }
	}

	private boolean showProjectRoot () {
		return PrefsInitializer.getBoolean(PrefsInitializer.PREF_SHOW_PROJECT_ROOT);
	}
		
	private Object[] wrapProjects(IProject[] project) {
		Object[] ret = new Object[project.length];
		for( int i = 0; i < project.length; i++ )
			ret[i] = new WrappedProject(project[i]);
		return ret;
	}
	public Object[] getChildren(Object parentElement) {
		
		if( parentElement instanceof Object[] ) return (Object[])parentElement;
		
		
		if( parentElement instanceof IArchiveModelNode && showProjectRoot())  {
			IProject[] projects;
			if( PrefsInitializer.getBoolean(PrefsInitializer.PREF_SHOW_ALL_PROJECTS)) {
				projects = ProjectArchivesView.getInstance().getAllProjectsWithPackages();
			} else {
				projects = new IProject[] { ((IArchiveModelNode)parentElement).getProject()};
			}
			return wrapProjects(projects);
		}
		
		if( parentElement instanceof IArchiveModelNode)
			return ((IArchiveModelNode)parentElement).getAllChildren();
		
		if( parentElement instanceof ArchivesModel ) {
			// return all that's there
		}
		if( parentElement instanceof WrappedProject ) {
			return ArchivesModel.instance().getProjectArchives(((WrappedProject)parentElement).getProject());
		}
		if( parentElement instanceof IArchiveNode ) {
			return ((IArchiveNode)parentElement).getAllChildren();
		}
		return new Object[] {};
	}

	public Object getParent(Object element) {
		if( element instanceof IArchiveNode ) 
			return ((IArchiveNode)element).getParent();
		if( element instanceof WrappedProject ) 
			return ArchivesModel.instance().getRoot(((WrappedProject)element).getProject());
		return null;
	}

	public boolean hasChildren(Object element) {
		if( element == null || getChildren(element) == null ) {
			System.out.println("pause");
		}
		return getChildren(element).length > 0;
	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public void dispose() {}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
