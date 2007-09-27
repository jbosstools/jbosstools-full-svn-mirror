/*
 * Created on Sep 20, 2004
 */
package org.jboss.ide.eclipse.jdt.aop.core.classpath;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;


/**
 * @author Marshall
 * 
 * The initializer for the AopClasspathContainer
 */
public class AopClasspathContainerInitializer extends ClasspathContainerInitializer
{
	public void initialize(IPath containerPath, IJavaProject project)
		throws CoreException
	{
		String containerId = containerPath.segment(0);
		IClasspathContainer container = null;
		
		if (containerId.equals(AopJdk14ClasspathContainer.CONTAINER_ID))
		{
			 container = new AopJdk14ClasspathContainer (containerPath);		
		}
		else if (containerId.equals(AopJdk15ClasspathContainer.CONTAINER_ID))
		{
			 container = new AopJdk15ClasspathContainer (containerPath);
		}
		
		if (container != null)
			JavaCore.setClasspathContainer(containerPath, new IJavaProject[]{project}, new IClasspathContainer[]{container}, null);
	}
	
	public String getDescription(IPath containerPath, IJavaProject project) {
		String containerId = containerPath.segment(0);
		if (containerId.equals(AopJdk14ClasspathContainer.CONTAINER_ID))
		{
			 return AopJdk14ClasspathContainer.DESCRIPTION;
		}
		else if (containerId.equals(AopJdk15ClasspathContainer.CONTAINER_ID))
		{
			return AopJdk15ClasspathContainer.DESCRIPTION;
		}
		return "";
	}
}
