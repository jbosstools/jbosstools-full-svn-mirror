package org.jboss.tools.common.el.core.test.resolver;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class ProjectNature1 implements IProjectNature {
	public static final String ID = "org.jboss.tools.common.el.core.test.project-nature1";
	IProject prj;
	
	@Override
	public void configure() throws CoreException {
	}

	@Override
	public void deconfigure() throws CoreException {
	}

	@Override
	public IProject getProject() {
		return prj;
	}

	@Override
	public void setProject(IProject project) {
		prj = project;
	}

}
