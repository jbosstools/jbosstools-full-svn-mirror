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
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * Factory used to create/delete JSP markers on a given resource.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPMarkerFactory
{

   /**Constructor for the JSPMarkerFactory object */
   private JSPMarkerFactory()
   {
   }

   /**
    * Description of the Method
    *
    * @param file               Description of the Parameter
    * @return                   Description of the Return Value
    * @exception CoreException  Description of the Exception
    */
   public static IMarker createMarker(IFile file) throws CoreException
   {
      IMarker marker = file.createMarker(IMarker.PROBLEM);
      return marker;
   }

   /**
    * Description of the Method
    *
    * @param file               Description of the Parameter
    * @exception CoreException  Description of the Exception
    */
   public static void deleteMarkers(IFile file) throws CoreException
   {
      file.deleteMarkers(IMarker.PROBLEM, false, IResource.DEPTH_ZERO);
   }

   /**
    * Description of the Method
    *
    * @param folder             Description of the Parameter
    * @exception CoreException  Description of the Exception
    */
   public static void deleteMarkers(IFolder folder) throws CoreException
   {
      folder.deleteMarkers(IMarker.PROBLEM, false, IResource.DEPTH_INFINITE);
   }
}
