package org.jboss.ide.eclipse.archives.ui.views;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.ui.PrefsInitializer;

public class ArchivesRootContentProvider implements ITreeContentProvider {
	
	public Object[] getChildren(Object parentElement) {
		return new Object[0];
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		return false;
	}

	public Object[] getElements(Object inputElement) {
		if( showProjectRoot() ) {
			if( showAllProjects() ) {
				IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
				ArrayList<IProject> tmp = new ArrayList<IProject>();
				for( int i = 0; i < projects.length; i++ )
					if( ArchivesModel.instance().canReregister(projects[i].getLocation()))
						tmp.add(projects[i]);
				return (IProject[]) tmp.toArray(new IProject[tmp.size()]);
			}
			IProject cp = ProjectArchivesCommonView.getInstance().getCurrentProject();
			if( cp != null )
				return new Object[]{cp};
		}
		return new Object[]{};
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
	
	private boolean showProjectRoot () {
		return PrefsInitializer.getBoolean(PrefsInitializer.PREF_SHOW_PROJECT_ROOT);
	}
	private boolean showAllProjects () {
		return PrefsInitializer.getBoolean(PrefsInitializer.PREF_SHOW_ALL_PROJECTS);
	}
}
