/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.classpath;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.ClasspathEntry;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;

public abstract class CacheClassPathContainer implements IClasspathContainer
{
   protected IPath path;

   public CacheClassPathContainer(IPath path)
   {
      this.path = path;
   }

   protected abstract IPath[] getCacheJarRelativePaths();

   public abstract String getContainerId();

   public IPath[] getCacheJarPaths()
   {
      String baseDir = JBossCachePlugin.getDefault().getBaseDir();
      ArrayList paths = new ArrayList();

      IPath cacheJars[] = getCacheJarRelativePaths();

      for (int i = 0; i < cacheJars.length; i++)
      {
         IPath jar = cacheJars[i];
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
      IPath jarPaths[] = getCacheJarPaths();

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
