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
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;

/**
 * @author Marshall
 * 
 * This class holds the extra -Djboss.aop.path=.... argument
 */
public class AopArgumentsTab extends JavaArgumentsTab {
	
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		super.performApply(configuration);
		
		try {
			
			String vmArgs = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "");
			if (vmArgs.indexOf("-Djboss.aop.path") == -1)
			{
				String projectName = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, (String)null);
				if (projectName != null)
				{
					IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
					IJavaProject javaProject = JavaCore.create(project);
					if (javaProject != null)
					{
						vmArgs += " -Djboss.aop.path=" + AopCorePlugin.getDefault().getDescriptorPaths(javaProject);
						configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, vmArgs);	
					}
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}		
	}
}
