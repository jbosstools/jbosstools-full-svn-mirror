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
package org.jboss.ide.eclipse.launcher.core.configuration;

import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public abstract class AbstractServerLaunchConfigurationDelegate extends AbstractJavaLaunchConfigurationDelegate
      implements
         IServerLaunchConfigurationDelegate
{
   /**
    * Returns an instance of JavaSourceLocator which points to all open java-projects.
    * This is a optional argument for launching a program if the source should be displayed
    * during debugging.
    *
    * @return                   ISourceLocator
    * @exception CoreException  Description of the Exception
    */
   /*
    * public ISourceLocator getAllOpenProjects()
    * throws CoreException
    * {
    * IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
    * ArrayList openJavaProjects = new ArrayList();
    * for (int i = 0; i < projects.length; i++)
    * {
    * if (projects[i].isOpen()
    * && projects[i].hasNature(
    * IServerLaunchConfigurationConstants.JAVA_PROJECT_ID))
    * {
    * / Make an IJavaProject out of an IProject and add it to the collection
    * try
    * {
    * openJavaProjects.add(JavaCore.create(projects[i]));
    * }
    * catch (Throwable e)
    * {
    * e.printStackTrace();
    * }
    * }
    * }
    * if (openJavaProjects.size() == 0)
    * {
    * return null;
    * }
    * IJavaProject[] result = (IJavaProject[]) openJavaProjects.toArray(new IJavaProject[openJavaProjects.size()]);
    * return new JavaSourceLocator(result, true);
    * }
    */
}
