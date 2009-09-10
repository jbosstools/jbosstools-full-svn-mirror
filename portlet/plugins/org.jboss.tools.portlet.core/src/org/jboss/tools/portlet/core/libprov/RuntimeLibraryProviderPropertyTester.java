package org.jboss.tools.portlet.core.libprov;

import java.io.File;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jst.common.project.facet.core.libprov.EnablementExpressionContext;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectBase;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.internal.facets.FacetUtil;
import org.jboss.tools.portlet.core.internal.PortletRuntimeComponentProvider;


public final class RuntimeLibraryProviderPropertyTester extends PropertyTester {
	
	public boolean test(final Object receiver, final String property,
			final Object[] args, final Object value) {
		if (receiver instanceof EnablementExpressionContext) {
			EnablementExpressionContext context = (EnablementExpressionContext) receiver;
			IFacetedProjectBase facetedProject = context.getFacetedProject();
			org.eclipse.wst.common.project.facet.core.runtime.IRuntime primaryRuntime = facetedProject.getPrimaryRuntime();
			if (primaryRuntime == null) {
				return false;
			}
			IRuntime runtime = FacetUtil.getRuntime(primaryRuntime);
			if (runtime != null) {
				File location = runtime.getLocation().toFile();
				return PortletRuntimeComponentProvider.isPortalPresent(location, runtime, property);
			}
		}
		return false;
	}

}
