/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.ui.launch;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.launching.JavaLocalApplicationLaunchConfigurationDelegate;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;

/**
 * @author Marshall
 */
public class AopLaunchDelegate extends JavaLocalApplicationLaunchConfigurationDelegate {

	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
		throws CoreException
	{
		configuration = appendCurrentAopPath(configuration);
		
		super.launch(configuration, mode, launch, monitor);
	}
	
	
	/**
	 * Append the -Djboss.aop.path=... definition to the end of an AOP ILaunchConfiguration's VM Arguments
	 */
	protected ILaunchConfiguration appendCurrentAopPath (ILaunchConfiguration configuration)
	{
		try {
			ILaunchConfigurationWorkingCopy copy = configuration.getWorkingCopy();
			String projectName = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, (String)null);
			String vmArgs = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "");
			
			if (projectName != null)
			{
				IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
				IJavaProject javaProject = JavaCore.create(project);
				if (javaProject != null)
				{
					if (vmArgs.indexOf("-Djboss.aop.path") != -1)
					{
						String paths = AopCorePlugin.getDefault().getDescriptorPaths(javaProject);
						paths = paths.replaceAll("\\\\", "\\\\\\\\");
						
						vmArgs = vmArgs.replaceAll("-Djboss.aop.path=[^ ]+", "-Djboss.aop.path=" + paths);
					}
					else {
						vmArgs += " -Djboss.aop.path=" + AopCorePlugin.getDefault().getDescriptorPaths(javaProject);
					}
					copy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, vmArgs);
					return copy.doSave();
				}
			}
		} catch (CoreException ce) {
			ce.printStackTrace();
		}
		
		return null;
	}
}
