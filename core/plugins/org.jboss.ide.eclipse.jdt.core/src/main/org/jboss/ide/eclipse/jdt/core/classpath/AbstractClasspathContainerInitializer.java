/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
   public AbstractClasspathContainerInitializer()
   {
   }

   /**
    * Description of the Method
    *
    * @param containerPath      Description of the Parameter
    * @param project            Description of the Parameter
    * @exception CoreException  Description of the Exception
    */
   public void initialize(IPath containerPath, IJavaProject project) throws CoreException
   {
      int size = containerPath.segmentCount();
      if (size > 0)
      {
         if (containerPath.segment(0).equals(this.getClasspathContainerID()))
         {
            AbstractClasspathContainer container = this.createClasspathContainer(containerPath);
            JavaCore.setClasspathContainer(containerPath, new IJavaProject[]
            {project}, new IClasspathContainer[]
            {container}, null);
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
