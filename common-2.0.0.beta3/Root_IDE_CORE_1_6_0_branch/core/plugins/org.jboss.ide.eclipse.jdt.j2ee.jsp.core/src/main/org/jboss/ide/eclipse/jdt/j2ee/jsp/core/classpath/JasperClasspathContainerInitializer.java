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
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core.classpath;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.jboss.ide.eclipse.jdt.core.classpath.AbstractClasspathContainer;
import org.jboss.ide.eclipse.jdt.core.classpath.AbstractClasspathContainerInitializer;

/**
 * Classpath container initializer for the Jasper runtime classes.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JasperClasspathContainerInitializer extends AbstractClasspathContainerInitializer
{
   /**Constructor for the JasperClasspathContainerInitializer object */
   public JasperClasspathContainerInitializer()
   {
      super();
   }

   /**
    * Gets the description attribute of the JasperClasspathContainerInitializer object
    *
    * @param containerPath  Description of the Parameter
    * @param project        Description of the Parameter
    * @return               The description value
    */
   public String getDescription(IPath containerPath, IJavaProject project)
   {
      return "Jasper Classpath Container Initializer";//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param path  Description of the Parameter
    * @return      Description of the Return Value
    */
   protected AbstractClasspathContainer createClasspathContainer(IPath path)
   {
      return new JasperClasspathContainer(path);
   }

   /**
    * Gets the classpathContainerID attribute of the JasperClasspathContainerInitializer object
    *
    * @return   The classpathContainerID value
    */
   protected String getClasspathContainerID()
   {
      return JasperClasspathContainer.CLASSPATH_CONTAINER;
   }
}
