package org.jboss.ide.eclipse.archives.ui.views;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.ui.PrefsInitializer;
import org.jboss.ide.eclipse.archives.ui.views.ArchivesContentProviderDelegate.WrappedProject;

public class ArchivesRootContentProvider implements ITreeContentProvider {
	private ArchivesContentProviderDelegate delegate;
	public ArchivesRootContentProvider() {
		delegate = ArchivesContentProviderDelegate.getDefault();
	}
	
	public Object[] getChildren(Object parentElement) {
		return delegate.getChildren(parentElement);
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		return delegate.hasChildren(element);
	}

	public Object[] getElements(Object inputElement) {
		if( showProjectRoot() ) {
			if( showAllProjects() ) {
				IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
				ArrayList<IProject> tmp = new ArrayList<IProject>();
				for( int i = 0; i < projects.length; i++ )
					if( ArchivesModel.instance().canReregister(projects[i].getLocation()))
						tmp.add(projects[i]);
				return wrap((IProject[]) tmp.toArray(new IProject[tmp.size()]));
			}
			IProject cp = ProjectArchivesCommonView.getInstance().getCurrentProject();
			if( cp != null )
				return wrap(new IProject[]{cp});
		}
		return new Object[]{};
	}

	protected Object[] wrap(IProject[] objs) {
		WrappedProject[] projs = new WrappedProject[objs.length];
		for( int i = 0; i < projs.length; i++)
			projs[i] = new WrappedProject(objs[i], WrappedProject.NAME);
		return projs;
	}
	
	public void dispose() {
		delegate.dispose();
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		delegate.inputChanged(viewer, oldInput, newInput);
	}
	
	private boolean showProjectRoot () {
		return PrefsInitializer.getBoolean(PrefsInitializer.PREF_SHOW_PROJECT_ROOT);
	}
	private boolean showAllProjects () {
		return PrefsInitializer.getBoolean(PrefsInitializer.PREF_SHOW_ALL_PROJECTS);
	}
}
