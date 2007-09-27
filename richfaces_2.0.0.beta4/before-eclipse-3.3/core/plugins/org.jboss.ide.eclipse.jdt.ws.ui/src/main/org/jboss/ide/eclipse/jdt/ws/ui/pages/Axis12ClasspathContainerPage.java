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
package org.jboss.ide.eclipse.jdt.ws.ui.pages;

import org.jboss.ide.eclipse.jdt.ui.pages.ClasspathContainerPage;
import org.jboss.ide.eclipse.jdt.ws.core.classpath.Axis12ClasspathContainer;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class Axis12ClasspathContainerPage extends ClasspathContainerPage
{
   /**Constructor for the Axis12ClasspathContainerPage object */
   public Axis12ClasspathContainerPage()
   {
      super();
   }

   /**
    * Gets the classpathContainerId attribute of the Axis12ClasspathContainerPage object
    *
    * @return   The classpathContainerId value
    */
   protected String getClasspathContainerId()
   {
      return Axis12ClasspathContainer.CLASSPATH_CONTAINER;
   }

   /**
    * Gets the classpathEntryDescription attribute of the Axis12ClasspathContainerPage object
    *
    * @return   The classpathEntryDescription value
    */
   protected String getClasspathEntryDescription()
   {
      return Axis12ClasspathContainer.DESCRIPTION;
   }
}
