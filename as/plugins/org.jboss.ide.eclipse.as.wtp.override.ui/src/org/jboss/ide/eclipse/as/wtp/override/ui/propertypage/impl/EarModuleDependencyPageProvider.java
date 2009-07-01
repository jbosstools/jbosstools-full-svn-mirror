package org.jboss.ide.eclipse.as.wtp.override.ui.propertypage.impl;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.jboss.ide.eclipse.as.wtp.override.ui.propertypage.IDependencyPageProvider;
import org.jboss.ide.eclipse.as.wtp.override.ui.propertypage.IModuleDependenciesControl;
import org.jboss.ide.eclipse.as.wtp.override.ui.propertypage.J2EEDependenciesPage;

public class EarModuleDependencyPageProvider implements IDependencyPageProvider {

	public boolean canHandle(IFacetedProject project) {
		boolean isEAR = project.hasProjectFacet(ProjectFacetsManager.getProjectFacet("jst.ear")); //$NON-NLS-1$
		return isEAR;
	}

	public IModuleDependenciesControl[] createPages(IFacetedProject project,
			J2EEDependenciesPage parent) {
		return new IModuleDependenciesControl[] {
				new EarModuleDependenciesPropertyPage(project.getProject(), parent)
		};
	}

	public Composite createRootControl(IModuleDependenciesControl[] pages,
			Composite parent) {
		if( pages.length == 1 && pages[0] != null)
			return pages[0].createContents(parent);
		return null;
	}
}
