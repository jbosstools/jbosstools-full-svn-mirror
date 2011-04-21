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
package org.jboss.ide.eclipse.jdt.core.classpath;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.ide.eclipse.core.AbstractPlugin;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class AbstractClasspathContainer implements IClasspathContainer
{
   /** Description of the Field */
   protected IClasspathEntry[] entries;

   /** Description of the Field */
   protected IPath path;

   /**
    *Constructor for the AbstractClasspathContainer object
    *
    * @param path  Description of the Parameter
    */
   public AbstractClasspathContainer(IPath path)
   {
      this.path = path;
   }

   /**
    * Gets the classpathEntries attribute of the AbstractClasspathContainer object
    *
    * @return   The classpathEntries value
    */
   public IClasspathEntry[] getClasspathEntries()
   {
      if (entries == null)
      {
         entries = computeEntries();
      }
      return entries;
   }

   /**
    * Gets the description attribute of the AbstractClasspathContainer object
    *
    * @return   The description value
    */
   public abstract String getDescription();

   /**
    * Gets the kind attribute of the AbstractClasspathContainer object
    *
    * @return   The kind value
    */
   public int getKind()
   {
      return IClasspathContainer.K_APPLICATION;
   }

   /**
    * Gets the path attribute of the AbstractClasspathContainer object
    *
    * @return   The path value
    */
   public IPath getPath()
   {
      return this.path;
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected IClasspathEntry[] computeEntries()
   {
      ArrayList entries = new ArrayList();

      String baseDir = this.getPlugin().getBaseDir();
      File libDir = new File(baseDir + "/" + ClassPathConstants.LIB_FOLDER + "/" + getLibFolder());//$NON-NLS-1$ //$NON-NLS-2$
      File libSrcDir = new File(baseDir + "/" + ClassPathConstants.LIB_SOURCE_FOLDER + "/" + getLibFolder());//$NON-NLS-1$ //$NON-NLS-2$

      // Lists every modules in the lib dir
      File[] jars = libDir.listFiles(new FileFilter()
      {
         public boolean accept(File file)
         {
            return (file.toString().endsWith(".jar"));//$NON-NLS-1$
         }
      });

      if (jars != null)
      {
         for (int i = 0; i < jars.length; i++)
         {
            File jarFile = jars[i];
            String jarFileName = jarFile.getName();
            File jarSrcFile = new File(libSrcDir, jarFileName);

            IPath entryPath = new Path(jarFile.toString());

            IPath sourceAttachementPath = null;
            IPath sourceAttachementRootPath = null;

            if (jarSrcFile.exists())
            {
               sourceAttachementPath = new Path(jarSrcFile.toString());
               sourceAttachementRootPath = new Path("/");//$NON-NLS-1$
            }

            IClasspathEntry entry = JavaCore.newLibraryEntry(entryPath, sourceAttachementPath,
                  sourceAttachementRootPath, true);
            entries.add(entry);
         }
      }

      return (IClasspathEntry[]) entries.toArray(new IClasspathEntry[entries.size()]);
   }

   /**
    * Gets the libFolder attribute of the AbstractClasspathContainer object
    *
    * @return   The libFolder value
    */
   protected abstract String getLibFolder();

   /**
    * Gets the plugin attribute of the AbstractClasspathContainer object
    *
    * @return   The plugin value
    */
   protected abstract AbstractPlugin getPlugin();
}
