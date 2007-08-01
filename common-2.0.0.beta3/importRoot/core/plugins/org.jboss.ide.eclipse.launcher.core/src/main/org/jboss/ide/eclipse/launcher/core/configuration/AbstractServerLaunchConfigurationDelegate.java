/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.core.configuration;

import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public abstract class AbstractServerLaunchConfigurationDelegate
       extends AbstractJavaLaunchConfigurationDelegate
       implements IServerLaunchConfigurationDelegate
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
