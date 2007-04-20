package org.jboss.ide.eclipse.archives.ui.providers;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.archives.core.model.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveModelNode;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchivesModel;
import org.jboss.ide.eclipse.archives.ui.PrefsInitializer;

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
		if( parentElement instanceof ArchiveModelNode && showProjectRoot())  {
			IProject[] projects;
			if( PrefsInitializer.getBoolean(PrefsInitializer.PREF_SHOW_ALL_PROJECTS)) {
				IProject[] projects2 = ResourcesPlugin.getWorkspace().getRoot().getProjects();
				ArrayList list = new ArrayList();
				for( int i = 0; i < projects2.length; i++ ) {
					if( ArchivesCore.packageFileExists(projects2[i])) {
						list.add(projects2[i]);
					}
				}
				projects = (IProject[]) list.toArray(new IProject[list.size()]);
			} else {
				projects = new IProject[] { ((ArchiveModelNode)parentElement).getProject()};
			}
			return wrapProjects(projects);
		}
		
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
