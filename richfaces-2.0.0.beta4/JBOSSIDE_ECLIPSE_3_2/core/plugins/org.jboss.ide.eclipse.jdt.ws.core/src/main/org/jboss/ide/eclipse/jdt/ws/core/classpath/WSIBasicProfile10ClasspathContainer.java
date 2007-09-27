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
package org.jboss.ide.eclipse.jdt.ws.core.classpath;

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.core.classpath.AbstractClasspathContainer;
import org.jboss.ide.eclipse.jdt.core.classpath.ClassPathConstants;
import org.jboss.ide.eclipse.jdt.ws.core.JDTWSCoreMessages;
import org.jboss.ide.eclipse.jdt.ws.core.JDTWSCorePlugin;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class WSIBasicProfile10ClasspathContainer extends AbstractClasspathContainer
{
   /** Description of the Field */
   public final static String CLASSPATH_CONTAINER = ClassPathConstants.CLASSPATH_CONTAINER_PREFIX
         + "." + WSIBasicProfile10ClasspathContainer.SUFFIX;//$NON-NLS-1$

   /** Description of the Field */
   public final static String DESCRIPTION = JDTWSCoreMessages.getString(CLASSPATH_CONTAINER);

   /** Description of the Field */
   public final static String SUFFIX = "wsi-1.0";//$NON-NLS-1$

   /**
    *Constructor for the WSIBasicProfile10ClasspathContainer object
    *
    * @param path  Description of the Parameter
    */
   public WSIBasicProfile10ClasspathContainer(IPath path)
   {
      super(path);
   }

   /**
    * Gets the description attribute of the WSIBasicProfile10ClasspathContainer object
    *
    * @return   The description value
    */
   public String getDescription()
   {
      return DESCRIPTION;
   }

   /**
    * Gets the libFolder attribute of the WSIBasicProfile10ClasspathContainer object
    *
    * @return   The libFolder value
    */
   protected String getLibFolder()
   {
      return SUFFIX;
   }

   /**
    * Gets the plugin attribute of the WSIBasicProfile10ClasspathContainer object
    *
    * @return   The plugin value
    */
   protected AbstractPlugin getPlugin()
   {
      return JDTWSCorePlugin.getDefault();
   }
}
