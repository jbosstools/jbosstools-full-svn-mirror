/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.core.classpath;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class AbstractClasspathContainerInitializer extends ClasspathContainerInitializer
{
   /**Constructor for the AbstractClasspathContainerInitializer object */
   public AbstractClasspathContainerInitializer() { }


   /**
    * Description of the Method
    *
    * @param containerPath      Description of the Parameter
    * @param project            Description of the Parameter
    * @exception CoreException  Description of the Exception
    */
   public void initialize(IPath containerPath, IJavaProject project)
      throws CoreException
   {
      int size = containerPath.segmentCount();
      if (size > 0)
      {
         if (containerPath.segment(0).equals(this.getClasspathContainerID()))
         {
            AbstractClasspathContainer container = this.createClasspathContainer(containerPath);
            JavaCore.setClasspathContainer(containerPath, new IJavaProject[]{project}, new IClasspathContainer[]{container}, null);
         }
      }
   }


   /**
    * Description of the Method
    *
    * @param path  Description of the Parameter
    * @return      Description of the Return Value
    */
   protected abstract AbstractClasspathContainer createClasspathContainer(IPath path);


   /**
    * Gets the classpathContainerID attribute of the AbstractClasspathContainerInitializer object
    *
    * @return   The classpathContainerID value
    */
   protected abstract String getClasspathContainerID();
}
