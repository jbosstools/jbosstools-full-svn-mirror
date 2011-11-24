package org.jboss.tools.profiler.internal.ui.util;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class FileNameFilter extends ViewerFilter {

	private String[] validFilters;

	public FileNameFilter(String[] strings) {
		validFilters = strings;
	}

	public boolean select(Viewer viewer, Object parent, Object element) {
		if (element instanceof IFile) {
			String name = ((IFile) element).getName();
			for (String filter : validFilters) {
				if(filter.contains("*.")) {
					return name.endsWith(filter.substring(filter.indexOf("*.")+1));
				} else {
					return validFilters.equals(name);	
				}
			}
		}

		if (element instanceof IProject && !((IProject) element).isOpen())
			return false;

		if (element instanceof IContainer) { // i.e. IProject, IFolder
			try {
				IResource[] resources = ((IContainer) element).members();
				for (int i = 0; i < resources.length; i++) {
					if (select(viewer, parent, resources[i]))
						return true;
				}
			} catch (CoreException e) {
				//ignore
			}
		}
		return false;
	}

}
