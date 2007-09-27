/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
