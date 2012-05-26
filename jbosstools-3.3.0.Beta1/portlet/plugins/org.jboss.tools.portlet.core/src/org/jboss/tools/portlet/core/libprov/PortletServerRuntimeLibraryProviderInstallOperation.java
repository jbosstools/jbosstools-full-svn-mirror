package org.jboss.tools.portlet.core.libprov;

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

public class PortletServerRuntimeLibraryProviderInstallOperation extends
		LibraryProviderOperation {

	@Override
	public void execute(LibraryProviderOperationConfig config,
			IProgressMonitor monitor) throws CoreException {
		IFacetedProjectBase facetedProject = config.getFacetedProject();
		IProject project = facetedProject.getProject();
			
		IJavaProject javaProject = JavaCore.create(project);	
		IPath containerPath = new Path(IPortletConstants.PORTLET_RUNTIME_CONTAINER_ID);
		setContainerPath(monitor, javaProject, containerPath);
		
	}

	private void setContainerPath(IProgressMonitor monitor, IJavaProject javaProject,IPath containerPath) throws CoreException {
		IClasspathEntry[] entries = javaProject.getRawClasspath();
		for (IClasspathEntry entry:entries) {
			if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
				IPath path = entry.getPath();
				if (containerPath.equals(path)) {
					return;
				}
			}
		}
		IClasspathEntry entry = JavaCore.newContainerEntry(containerPath, true);
		IClasspathEntry[] newEntries = new IClasspathEntry[entries.length + 1];
		System.arraycopy( entries, 0, newEntries, 0, entries.length );
		newEntries[entries.length] = entry;
		javaProject.setRawClasspath(newEntries, monitor);
	}
	
}
