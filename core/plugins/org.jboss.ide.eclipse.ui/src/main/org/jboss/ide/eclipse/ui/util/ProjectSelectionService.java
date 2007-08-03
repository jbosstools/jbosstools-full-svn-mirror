package org.jboss.ide.eclipse.ui.util;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.ui.IProjectSelectionListener;

public class ProjectSelectionService implements ISelectionListener {

	private ArrayList listeners;
	private static ProjectSelectionService _instance;
	
	private ProjectSelectionService ()
	{
		listeners = new ArrayList();
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(this);
	}
	
	public static ProjectSelectionService instance ()
	{
		if (_instance == null)
			_instance = new ProjectSelectionService();
		return _instance;
	}
	
	public void addProjectSelectionListener (IProjectSelectionListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeProjectSelectionListener (IProjectSelectionListener listener)
	{
		listeners.remove(listener);
	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (!(selection instanceof IStructuredSelection))
			return;
		
		Object element = ((IStructuredSelection)selection).getFirstElement();
		IProject project = ProjectUtil.getProject(element);
		if (project != null)
			fireProjectSelected(project);
	}
	
	private void fireProjectSelected (IProject project)
	{
		for (Iterator iter = listeners.iterator(); iter.hasNext(); )
		{
			IProjectSelectionListener listener = (IProjectSelectionListener) iter.next();
			listener.projectSelected(project);
		}
	}

}
