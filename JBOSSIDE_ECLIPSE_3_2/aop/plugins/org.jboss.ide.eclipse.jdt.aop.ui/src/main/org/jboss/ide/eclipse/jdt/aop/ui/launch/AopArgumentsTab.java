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
public class AopArgumentsTab extends JavaArgumentsTab
{

   public void performApply(ILaunchConfigurationWorkingCopy configuration)
   {
      super.performApply(configuration);

      try
      {

         String vmArgs = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "");
         if (vmArgs.indexOf("-Djboss.aop.path") == -1)
         {
            String projectName = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                  (String) null);
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
      }
      catch (CoreException e)
      {
         e.printStackTrace();
      }
   }
}
