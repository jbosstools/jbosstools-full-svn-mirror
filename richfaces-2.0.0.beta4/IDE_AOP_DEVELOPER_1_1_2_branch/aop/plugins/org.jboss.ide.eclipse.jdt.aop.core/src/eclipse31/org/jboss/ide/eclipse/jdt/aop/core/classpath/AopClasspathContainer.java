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
package org.jboss.ide.eclipse.jdt.aop.core.classpath;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.ClasspathEntry;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;

/**
 * @author Marshall
 */
public abstract class AopClasspathContainer implements IClasspathContainer
{

   protected IPath path;

   public AopClasspathContainer(IPath path)
   {
      this.path = path;
   }

   protected abstract IPath[] getAopJarRelativePaths();

   public abstract String getContainerId();

   public IPath[] getAopJarPaths()
   {
      String baseDir = AopCorePlugin.getDefault().getBaseDir();
      ArrayList paths = new ArrayList();

      IPath aopJars[] = getAopJarRelativePaths();

      for (int i = 0; i < aopJars.length; i++)
      {
         IPath jar = aopJars[i];
         IPath entryPath = new Path(baseDir).append(jar);
         paths.add(entryPath);
      }

      return (IPath[]) paths.toArray(new IPath[paths.size()]);
   }

   public int getKind()
   {
      return K_APPLICATION;
   }

   public IPath getPath()
   {
      return path;
   }

   public IClasspathEntry[] getClasspathEntries()
   {
      ArrayList entries = new ArrayList();
      IPath jarPaths[] = getAopJarPaths();

      for (int i = 0; i < jarPaths.length; i++)
      {
         IPath jar = jarPaths[i];

         // Later we can add the source jars here..
         IClasspathEntry entry = JavaCore.newLibraryEntry(jar, null, null, false);
         entries.add(entry);
      }

      return (IClasspathEntry[]) entries.toArray(new IClasspathEntry[entries.size()]);
   }

   public IClasspathEntry createClasspathEntry(IProject project)
   {
      return new ClasspathEntry(IPackageFragmentRoot.K_BINARY, IClasspathEntry.CPE_CONTAINER,
            new Path(getContainerId()), ClasspathEntry.INCLUDE_ALL, ClasspathEntry.EXCLUDE_NONE, null, // source attachment
            null, // source attachment root
            null, // specific output folder
            false, // is exported
            null, // access rules
            false, // combine access rules
            null // extra classpath attributes
      );
   }
}
