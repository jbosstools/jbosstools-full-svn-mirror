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
package org.jboss.ide.eclipse.jdt.test.core.classpath;

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.core.classpath.AbstractClasspathContainer;
import org.jboss.ide.eclipse.jdt.core.classpath.ClassPathConstants;
import org.jboss.ide.eclipse.jdt.test.core.JDTTestCoreMessages;
import org.jboss.ide.eclipse.jdt.test.core.JDTTestCorePlugin;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JUnitClasspathContainer extends AbstractClasspathContainer
{
   /** Description of the Field */
   public final static String CLASSPATH_CONTAINER = ClassPathConstants.CLASSPATH_CONTAINER_PREFIX
         + "." + JUnitClasspathContainer.SUFFIX;//$NON-NLS-1$

   /** Description of the Field */
   public final static String DESCRIPTION = JDTTestCoreMessages.getString(CLASSPATH_CONTAINER);

   /** Description of the Field */
   public final static String SUFFIX = "junit-3.8.1";//$NON-NLS-1$

   /**
    *Constructor for the JUnitClasspathContainer object
    *
    * @param path  Description of the Parameter
    */
   public JUnitClasspathContainer(IPath path)
   {
      super(path);
   }

   /**
    * Gets the description attribute of the JUnitClasspathContainer object
    *
    * @return   The description value
    */
   public String getDescription()
   {
      return DESCRIPTION;
   }

   /**
    * Gets the libFolder attribute of the JUnitClasspathContainer object
    *
    * @return   The libFolder value
    */
   protected String getLibFolder()
   {
      return SUFFIX;
   }

   /**
    * Gets the plugin attribute of the JUnitClasspathContainer object
    *
    * @return   The plugin value
    */
   protected AbstractPlugin getPlugin()
   {
      return JDTTestCorePlugin.getDefault();
   }
}
