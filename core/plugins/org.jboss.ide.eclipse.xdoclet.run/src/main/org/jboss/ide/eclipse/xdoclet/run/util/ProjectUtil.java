/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.run.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.jboss.ide.eclipse.core.AbstractPlugin;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @created   27 mars 2003
 * @todo      Javadoc to complete
 */
public class ProjectUtil
{
   /**
    * Gets the classPath attribute of the ProjectUtil class
    *
    * @param project            Description of the Parameter
    * @param cp                 Description of the Parameter
    * @param hostingProject     Description of the Parameter
    * @exception CoreException  Description of the Exception
    */
   public static void populateClassPath(IJavaProject hostingProject, IJavaProject project, Collection cp)
          throws CoreException
   {
      IClasspathEntry entry;

      IProject homeProject = hostingProject.getProject();
      List allEntries = new ArrayList();

      // Collect every classpath entry
      IClasspathEntry[] entries = project.getRawClasspath();
      for (int i = 0; i < entries.length; i++)
      {
         entry = entries[i];

         switch (entry.getEntryKind())
         {
            case IClasspathEntry.CPE_CONTAINER:
               IClasspathEntry jre = JavaRuntime.getDefaultJREContainerEntry();
               if (!entry.equals(jre))
               {
                  IClasspathContainer container = JavaCore.getClasspathContainer(entry.getPath(), project);
                  IClasspathEntry[] containerEntries = container.getClasspathEntries();
                  allEntries.addAll(Arrays.asList(containerEntries));
               }
               break;
            default:
               allEntries.add(entry);
         }
      }

      // Process every entries
      for (int i = 0; i < allEntries.size(); i++)
      {
         entry = (IClasspathEntry) allEntries.get(i);
         IPath relativePath = entry.getPath();
         String entryProjectName = relativePath.segment(0);
         IProject entryProject = AbstractPlugin.getWorkspace().getRoot().getProject(entryProjectName);

         switch (entry.getEntryKind())
         {
            case IClasspathEntry.CPE_LIBRARY:
               String lib;

               IPath shrinkedPath = shrinkPathToProjectPath(homeProject, entryProject, entry.getPath());
               lib = shrinkedPath.toString();

               if (!cp.contains(lib))
               {
                  cp.add(lib);
               }
               break;
            case IClasspathEntry.CPE_PROJECT:
               String base = entry.getPath().toString();
               IJavaProject p = project.getJavaModel().getJavaProject(base);
               populateClassPath(hostingProject, p, cp);
               break;
            case IClasspathEntry.CPE_SOURCE:
               IPath outputpath = entry.getOutputLocation();
               if (outputpath == null)
               {
                  outputpath = project.getOutputLocation();
               }

               shrinkedPath = shrinkPathToProjectPath(homeProject, entryProject, outputpath);
               lib = shrinkedPath.toString();

               if (!cp.contains(lib))
               {
                  cp.add(lib);
               }
               break;
            case IClasspathEntry.CPE_CONTAINER:
               // Nothing to do
               break;
            case IClasspathEntry.CPE_VARIABLE:
               entry = JavaCore.getResolvedClasspathEntry(entry);
               String var = entry.getPath().toString();
               if (!cp.contains(var))
               {
                  cp.add(var);
               }
               break;
            default:
            // Nothing to do
         }
      }
   }


   /**
    * Description of the Method
    *
    * @param homeProject     Description of the Parameter
    * @param elementProject  Description of the Parameter
    * @param path            Description of the Parameter
    * @return                Description of the Return Value
    */
   public static IPath shrinkPathToProjectPath(IProject homeProject, IProject elementProject, IPath path)
   {
      IPath homeProjectPath = homeProject.getLocation();
      IPath entryProjectPath = elementProject.getLocation();

      if ((homeProjectPath == null) || (entryProjectPath == null))
      {
         return path;
      }

      IPath result = entryProjectPath.append(path.removeFirstSegments(1));

      // The path is within the project
      if (homeProjectPath.isPrefixOf(result))
      {
         result = result.removeFirstSegments(homeProjectPath.segmentCount());
         result = result.setDevice(null);
      }
      else
      {
         // The path is within the project parent path
         if (homeProjectPath.segmentCount() > 0)
         {
            IPath parentProjectPath = homeProjectPath.removeLastSegments(1);
            if (parentProjectPath.isPrefixOf(result))
            {
               result = result.removeFirstSegments(parentProjectPath.segmentCount());
               result = result.setDevice(null);
               result = new Path("..").append(result);//$NON-NLS-1$
            }
         }
      }

      return result;
   }
}
