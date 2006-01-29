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
package org.jboss.ide.eclipse.jdt.core.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.ide.eclipse.core.util.ProjectUtil;

/**
 * Utility class for IJavaProject object access
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JavaProjectUtil
{
   /** Avoid instantiation */
   private JavaProjectUtil()
   {
   }

   /**
    * Description of the Method
    *
    * @param project            Description of the Parameter
    * @param srcFolderName      Description of the Parameter
    * @return                   Description of the Return Value
    * @exception CoreException  Description of the Exception
    */
   public static IClasspathEntry createSourceClasspathEntry(IProject project, String srcFolderName)
         throws CoreException
   {
      return createSourceClasspathEntry(project, srcFolderName, null);
   }

   /**
    * Description of the Method
    *
    * @param project            Description of the Parameter
    * @param srcFolderName      Description of the Parameter
    * @param outputFolderName   Description of the Parameter
    * @return                   Description of the Return Value
    * @exception CoreException  Description of the Exception
    */
   public static IClasspathEntry createSourceClasspathEntry(IProject project, String srcFolderName,
         String outputFolderName) throws CoreException
   {
      ProjectUtil.createFolder(project, srcFolderName);
      if (outputFolderName != null)
      {
         ProjectUtil.createFolder(project, outputFolderName);
      }

      // Add the source entry with a specific output location
      IPath outputFolderPath = null;
      if (outputFolderName != null)
      {
         outputFolderPath = new Path("/" + project.getName() + "/" + outputFolderName);//$NON-NLS-1$ //$NON-NLS-2$
      }

      IClasspathEntry entry = JavaCore.newSourceEntry(
            new Path("/" + project.getName() + "/" + srcFolderName), new Path[]{}, outputFolderPath);//$NON-NLS-1$ //$NON-NLS-2$

      return entry;
   }

   /**
    * Description of the Method
    *
    * @param entries            Description of the Parameter
    * @param cpEntry            Description of the Parameter
    * @return                   Description of the Return Value
    * @exception CoreException  Description of the Exception
    */
   public static IClasspathEntry[] mergeClasspathEntry(IClasspathEntry[] entries, IClasspathEntry cpEntry)
         throws CoreException
   {
      if (cpEntry == null)
      {
         return entries;
      }

      List list = new ArrayList();
      if (entries != null)
      {
         String containerId = cpEntry.getPath().toString();
         for (int i = 0; i < entries.length; i++)
         {
            if (!entries[i].getPath().toString().equals(containerId))
            {
               list.add(entries[i]);
            }
         }
      }
      list.add(cpEntry);

      return (IClasspathEntry[]) list.toArray(new IClasspathEntry[list.size()]);
   }

   /**
    * Description of the Method
    *
    * @param entries            Description of the Parameter
    * @param cpEntry            Description of the Parameter
    * @return                   Description of the Return Value
    * @exception CoreException  Description of the Exception
    */
   public static IClasspathEntry[] removeClasspathEntry(IClasspathEntry[] entries, IClasspathEntry cpEntry)
         throws CoreException
   {
      List list = new ArrayList();
      if (entries != null)
      {
         String containerId = cpEntry.getPath().toString();
         for (int i = 0; i < entries.length; i++)
         {
            if (!entries[i].getPath().toString().equals(containerId))
            {
               list.add(entries[i]);
            }
         }
      }

      return (IClasspathEntry[]) list.toArray(new IClasspathEntry[list.size()]);
   }
}
