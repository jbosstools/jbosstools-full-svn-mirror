package org.jboss.ide.eclipse.packages.ui.providers;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageModelNode;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;
import org.jboss.ide.eclipse.packages.ui.PackagesUIPlugin;
import org.jboss.ide.eclipse.packages.ui.PrefsInitializer;

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
		if( parentElement instanceof PackageModelNode && showProjectRoot()) 
			return wrapProjects(new IProject[] { ((PackageModelNode)parentElement).getProject()});
		
		if( parentElement instanceof PackageModelNode)
			return ((PackageModelNode)parentElement).getAllChildren();
		
		if( parentElement instanceof PackagesModel ) {
			// return all that's there
		}
		if( parentElement instanceof IProject ) {
			return PackagesModel.instance().getProjectPackages((IProject)parentElement);
		}
		if( parentElement instanceof IPackageNode ) {
			return ((IPackageNode)parentElement).getAllChildren();
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
