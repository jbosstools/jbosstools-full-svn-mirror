package org.jboss.ide.eclipse.archives.ui.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jboss.ide.eclipse.archives.core.model.ArchivesCore;

public class BuildProjectAction implements IWorkbenchWindowActionDelegate {
	private IProject selectedProject;
	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
	}

	public void run(IAction action) {
		if( selectedProject != null ) 
			ArchivesCore.buildProject(selectedProject, null);
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if( !selection.isEmpty() && selection instanceof IStructuredSelection ) {
			Object o = ((IStructuredSelection)selection).getFirstElement();
			if( o instanceof IAdaptable ) {
				IResource res = (IResource)  ((IAdaptable)o).getAdapter(IResource.class);
				if( res != null ) {
					selectedProject = res.getProject();				
					return;
				}
			}
		}
		selectedProject = null;
	}
	
}