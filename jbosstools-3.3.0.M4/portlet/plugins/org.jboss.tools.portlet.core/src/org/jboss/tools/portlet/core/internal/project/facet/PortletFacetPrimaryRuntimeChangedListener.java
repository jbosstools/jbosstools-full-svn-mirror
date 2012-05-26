package org.jboss.tools.portlet.core.internal.project.facet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectEvent;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectListener;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.PortletCoreActivator;
import org.jboss.tools.portlet.core.internal.PortletRuntimeLibrariesContainerInitializer;

public class PortletFacetPrimaryRuntimeChangedListener implements
		IFacetedProjectListener {

	public void handleEvent(IFacetedProjectEvent event) {
		final IFacetedProject fproj = event.getProject();
		final IProjectFacet PORTLET_FACET = ProjectFacetsManager
				.getProjectFacet(IPortletConstants.PORTLET_FACET_ID);
		if (fproj.hasProjectFacet(PORTLET_FACET)) {
			//final IProjectFacetVersion fv = fproj
			//		.getInstalledVersion(PORTLET_FACET);
			try {
				IPath containerPath = new Path(IPortletConstants.PORTLET_RUNTIME_CONTAINER_ID);
				IProject project = fproj.getProject();
				IJavaProject javaProject = JavaCore.create(project);
				IClasspathContainer containerEntry = JavaCore.getClasspathContainer(containerPath, javaProject);
				ClasspathContainerInitializer initializer = JavaCore.getClasspathContainerInitializer(IPortletConstants.PORTLET_RUNTIME_CONTAINER_ID);
				initializer.requestClasspathContainerUpdate(containerPath, javaProject, containerEntry);
			} catch (CoreException e) {
				PortletCoreActivator.log(e);
			}
		}
	}

}
