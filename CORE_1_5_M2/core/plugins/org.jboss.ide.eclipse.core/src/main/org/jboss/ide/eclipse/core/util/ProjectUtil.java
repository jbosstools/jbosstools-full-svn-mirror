/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.core.util;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
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
