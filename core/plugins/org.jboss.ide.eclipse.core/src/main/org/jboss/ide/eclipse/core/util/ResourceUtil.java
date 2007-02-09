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
package org.jboss.ide.eclipse.core.util;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

public class ResourceUtil
{

   public static void safeRefresh(IResource resource, int depth)
   {
      try
      {

         if (resource != null)
         {
            resource.refreshLocal(depth, null);
         }

         //	 BUG: JBAS-1218 we're swallowing a clearcase/vss - specific resource handling bug here... 		
      }
      catch (CoreException e)
      {
         e.printStackTrace();
      }
      catch (IllegalArgumentException e)
      {
         e.printStackTrace();
      }
   }
   
   /**
    * This will create a file (using IFile.create()) and any parent directories that do not exist as well.
    * @param file The file to create
    * @param source The input stream that will be copied as the file's contents
    * @param force Whether or not to force creation
    */
   public static void safeCreateFile (IFile file, InputStream source, boolean force, IProgressMonitor monitor)
   	throws CoreException
   {
	   if (file.exists()) return;
	   
	   IProject project = file.getProject();
	   
	   if (project.isAccessible())
	   {
		 IPath relativePath = file.getProjectRelativePath();
		 int folderCount = relativePath.segmentCount() - 1;
		 
		 for (int i = 0; i < folderCount; i++)
		 {
			 IFolder folder = project.getFolder(relativePath.removeLastSegments(folderCount - i));
			 
			 if (!folder.exists())
			 {
				folder.create(force, true, monitor);
			 }
		 }
		 
		 file.create(source, force, monitor);
	   }
   }
}
