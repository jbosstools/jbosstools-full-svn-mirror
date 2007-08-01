package org.jboss.ide.eclipse.firstrun.providers;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public abstract class AbstractProjectProvider implements IStructuredContentProvider {

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public abstract boolean checkProject (IProject project);
	
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof IWorkspace)
		{
			IWorkspace workspace = (IWorkspace) inputElement;
			
			IProject projects[] = workspace.getRoot().getProjects();
			ArrayList elements = new ArrayList();
			
			for (int i = 0; i < projects.length; i++)
			{
				if (projects[i] != null && projects[i].isAccessible())
				{
					if (checkProject(projects[i]))
						elements.add(projects[i]);
				}
			}
			
			return elements.toArray();			
		}
		
		return new Object[0];
	}

	
}
