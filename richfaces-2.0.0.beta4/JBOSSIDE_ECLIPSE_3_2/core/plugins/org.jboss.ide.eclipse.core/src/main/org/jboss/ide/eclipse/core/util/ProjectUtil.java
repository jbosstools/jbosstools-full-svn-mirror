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
package org.jboss.ide.eclipse.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.core.AbstractPlugin;

/**
 * Utility class for IProject object access
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ProjectUtil
{
   /** Avoid instantiation */
   private ProjectUtil()
   {
      super();
   }

   public static boolean projectHasBuilder(IProject project, String builderId)
   {
      try
      {
         IProjectDescription desc = project.getDescription();
         ICommand[] commands = desc.getBuildSpec();

         for (int i = 0; i < commands.length; i++)
         {
            if (commands[i].getBuilderName().equals(builderId))
               return true;
         }

         return false;
      }
      catch (CoreException e)
      {
         e.printStackTrace();
         return false;
      }
   }

   /**
    * Adds a specific builder to the end of a given project's builder list.
    * @param project
    * @param builderId
    * @return Whether or not the builder was successfully added
    */
   public static boolean addProjectBuilder(IProject project, String builderId)
   {
      try
      {
         IProjectDescription desc = project.getDescription();
         ICommand[] commands = desc.getBuildSpec();

         //add builders to project
         ICommand builderCommand = desc.newCommand();
         builderCommand.setBuilderName(builderId);

         ICommand[] newCommands = new ICommand[commands.length + 1];
         System.arraycopy(commands, 0, newCommands, 0, commands.length);
         newCommands[newCommands.length - 1] = builderCommand;

         desc.setBuildSpec(newCommands);

         project.setDescription(desc, new NullProgressMonitor());
      }
      catch (CoreException e)
      {
         e.printStackTrace();
         return false;
      }

      return true;
   }

   /**
    * Removes a specific builder from the end of a given project's builder list.
    * @param project
    * @param builderId
    * @return Whether or not the builder was successfully removed
    */
   public static boolean removeProjectBuilder(IProject project, String builderId)
   {
      try
      {
         IProjectDescription desc = project.getDescription();
         ICommand[] commands = desc.getBuildSpec();
         ArrayList newCommands = new ArrayList();

         for (int i = 0; i < commands.length; i++)
         {
            if (!commands[i].getBuilderName().equals(builderId))
            {
               newCommands.add(commands[i]);
            }
         }

         desc.setBuildSpec((ICommand[]) newCommands.toArray(new ICommand[newCommands.size()]));

         project.setDescription(desc, new NullProgressMonitor());
      }
      catch (CoreException e)
      {
         e.printStackTrace();
         return false;
      }

      return true;
   }

   /**
    * Adds the specified project nature to a project
    * @param project
    * @param natureId
    * @return
    */
   public static boolean addProjectNature(IProject project, String natureId)
   {
      boolean added = false;
      try
      {
         if (project != null && natureId != null)
         {
            IProjectDescription desc = project.getDescription();

            if (!project.hasNature(natureId))
            {
               String natureIds[] = desc.getNatureIds();
               String newNatureIds[] = new String[natureIds.length + 1];

               System.arraycopy(natureIds, 0, newNatureIds, 1, natureIds.length);
               newNatureIds[0] = natureId;
               desc.setNatureIds(newNatureIds);

               project.getProject().setDescription(desc, new NullProgressMonitor());
               added = true;
            }
         }
      }
      catch (CoreException e)
      {
         e.printStackTrace();
      }
      return added;
   }

   /**
    * Removes the specified project nature from the project
    * @param project
    * @param natureId
    * @return
    */
   public static boolean removeProjectNature(IProject project, String natureId)
   {
      boolean removed = false;
      try
      {
         if (project != null && natureId != null)
         {
            IProjectDescription desc = project.getDescription();

            if (project.hasNature(natureId))
            {
               String natureIds[] = desc.getNatureIds();
               String newNatureIds[] = new String[natureIds.length - 1];
               int n = 0;

               for (int i = 0; i < natureIds.length; i++)
               {
                  if (!natureIds[i].equals(natureId))
                  {
                     newNatureIds[n++] = natureIds[i];
                  }
               }

               desc.setNatureIds(newNatureIds);

               project.getProject().setDescription(desc, new NullProgressMonitor());
               removed = true;
            }
         }
      }
      catch (CoreException e)
      {
         e.printStackTrace();
      }
      return removed;
   }

   /**
    * Creates a folder in a project, with the intermediate folders if needed.
    *
    * @param project
    * @param folderName
    */
   public static void createFolder(IProject project, String folderName)
   {
      IFolder newFolder = project.getFolder(folderName);
      IPath folderPath = newFolder.getProjectRelativePath();

      if (!newFolder.exists())
      {
         try
         {
            for (int i = 0; i < folderPath.segmentCount(); i++)
            {
               IPath path = folderPath.uptoSegment(i);
               IFolder folder = project.getFolder(path);
               folder.create(true, true, null);
            }
         }
         catch (CoreException e)
         {
            AbstractPlugin.log(e);
         }
      }
   }

   /**
    * Gets the all the opened projects in the worksapce
    *
    * @return   A collection of opened projects
    */
   public static Collection getAllOpenedProjects()
   {
      return Arrays.asList(AbstractPlugin.getWorkspace().getRoot().getProjects());
   }

   /**
    * Gets a file from a workspace relative path. The file may not exists.
    *
    * @param pathRelativeToWorkspace  A workspace relative path
    * @return                         The file handle
    */
   public static IResource getFile(String pathRelativeToWorkspace)
   {
      IPath path = new Path(null, pathRelativeToWorkspace);

      // Try to map to an existing resource
      IResource resource = getResource(pathRelativeToWorkspace);
      if (resource == null)
      {
         // If not, build a handle on an non-existing resource
         resource = AbstractPlugin.getWorkspace().getRoot().getFile(path);
      }
      return resource;
   }

   /**
    * Gets a folder from a workspace relative path. The folder may not exists.
    *
    * @param pathRelativeToWorkspace  A workspace relative path
    * @return                         The folder handle
    */
   public static IResource getFolder(String pathRelativeToWorkspace)
   {
      IPath path = new Path(null, pathRelativeToWorkspace);

      // Try to map to an existing resource
      IResource resource = getResource(pathRelativeToWorkspace);
      if (resource == null)
      {
         // If not, build a handle on an non-existing resource
         resource = AbstractPlugin.getWorkspace().getRoot().getFolder(path);
      }
      return resource;
   }

   /**
    * Gets a resource from a workspace relative path. The resoruce must exists.
    *
    * @param pathRelativeToWorkspace  A workspace relative path
    * @return                         The resoruce handle
    */
   public static IResource getResource(String pathRelativeToWorkspace)
   {
      IPath path = new Path(null, pathRelativeToWorkspace);
      IResource resource = AbstractPlugin.getWorkspace().getRoot().findMember(path);
      return resource;
   }
}
