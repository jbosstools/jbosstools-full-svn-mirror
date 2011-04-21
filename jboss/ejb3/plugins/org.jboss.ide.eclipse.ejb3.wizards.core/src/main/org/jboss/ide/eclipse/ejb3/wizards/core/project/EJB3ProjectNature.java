package org.jboss.ide.eclipse.ejb3.wizards.core.project;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;

/**
 * @author Marshall
 */
public class EJB3ProjectNature implements IProjectNature {

	public static final String NATURE_ID = "org.jboss.ide.eclipse.ejb3.wizards.core.EJB3ProjectNature";
	
	private IProject project;
	
	public EJB3ProjectNature() {
	}

	public void configure() throws CoreException {
	
	}
	
	public void deconfigure() throws CoreException {
	}

	public IProject getProject()  {
		return this.project;
	}

	public void setProject(IProject project)  {
		this.project = project;
	}
	
	
	public static void ensureAopProjectNature (IJavaProject project)
	{
		boolean added = AopCorePlugin.addProjectNature(project.getProject(), NATURE_ID);
	}
}
