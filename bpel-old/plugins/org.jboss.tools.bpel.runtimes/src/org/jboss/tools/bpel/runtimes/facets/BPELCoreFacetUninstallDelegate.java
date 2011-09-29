package org.jboss.tools.bpel.runtimes.facets;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.common.project.facet.WtpUtils;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

/*
 * Added to support deprecated jbt.bpel.facet.core
 * https://issues.jboss.org/browse/JBIDE-8533
 */
public class BPELCoreFacetUninstallDelegate implements IDelegate {

	@Override
	public void execute(IProject project, IProjectFacetVersion fv, Object config, IProgressMonitor monitor)
			throws CoreException {
		WtpUtils.removeNatures(project);
	}

}
