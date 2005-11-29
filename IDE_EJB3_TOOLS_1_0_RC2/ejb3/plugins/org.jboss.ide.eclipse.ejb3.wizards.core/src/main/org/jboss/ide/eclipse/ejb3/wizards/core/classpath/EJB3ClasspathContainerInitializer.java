/*
 * Created on Dec 14, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.ejb3.wizards.core.classpath;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EJB3ClasspathContainerInitializer extends ClasspathContainerInitializer {

	public void initialize(IPath containerPath, IJavaProject project)
		throws CoreException
	{
		String containerId = containerPath.segment(0);
		IClasspathContainer container = null;
		
		if (containerId.equals(EJB3ClasspathContainer.CONTAINER_ID))
		{
			container = new EJB3ClasspathContainer (containerPath, project);		
		}
		
		if (container != null)
			JavaCore.setClasspathContainer(containerPath, new IJavaProject[]{project}, new IClasspathContainer[]{container}, null);
	}

	public String getDescription(IPath containerPath, IJavaProject project) {
		String containerId = containerPath.segment(0);
		if (containerId.equals(EJB3ClasspathContainer.CONTAINER_ID))
		{
			 return EJB3ClasspathContainer.DESCRIPTION;
		}
		
		return "";
	}
}
