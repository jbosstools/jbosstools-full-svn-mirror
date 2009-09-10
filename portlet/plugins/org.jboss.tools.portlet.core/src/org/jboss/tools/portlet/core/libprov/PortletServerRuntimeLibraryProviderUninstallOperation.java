package org.jboss.tools.portlet.core.libprov;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryProviderOperation;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryProviderOperationConfig;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectBase;
import org.jboss.tools.portlet.core.IPortletConstants;

public class PortletServerRuntimeLibraryProviderUninstallOperation extends LibraryProviderOperation {

	@Override
	public void execute(LibraryProviderOperationConfig config,
			IProgressMonitor monitor) throws CoreException {
		IFacetedProjectBase facetedProject = config.getFacetedProject();
		IProject project = facetedProject.getProject();
			
		IJavaProject javaProject = JavaCore.create(project);	
		IPath containerPath = new Path(IPortletConstants.PORTLET_RUNTIME_CONTAINER_ID);
		List<IClasspathEntry> list = new ArrayList<IClasspathEntry>();
		IClasspathEntry[] entries = javaProject.getRawClasspath();
		boolean changed = false;
		for (int i = 0; i < entries.length; i++) {
			IClasspathEntry entry = entries[i];
			if (!entry.getPath().equals(containerPath)) {
				list.add(entry);
			} else {
				changed = true;
			}
		}
		if (changed) {
			IClasspathEntry[] newEntries = list.toArray(new IClasspathEntry[0]);
			javaProject.setRawClasspath(newEntries, monitor);
		}
	}

}
