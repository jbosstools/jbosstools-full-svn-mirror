/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.utils;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.adaptor.EclipseClassLoader;

/**
 * This class represents class loader that will be used for marshalling in cache
 * @author Owner
 */
public final class CacheClassLoader
{

   private static URLClassLoader classLoader;

   /**
    * Return URLClassLoader
    * @param jarPaths
    * @return
    * @throws Exception
    */
   public static URLClassLoader getCacheClassLoaderInstance(List jarPaths, ClassLoader parent) throws Exception
   {

      if (classLoader == null)
      {
         List urls = new ArrayList();
         for (int i = 0; i < jarPaths.size(); i++)
         {
            File file = new File(jarPaths.get(i).toString());
            urls.add(file.toURL());
         }

         classLoader = new URLClassLoader((URL[]) urls.toArray(new URL[urls.size()]), parent);
      }

      return classLoader;
   }

   public static URLClassLoader getSingletonClassLoader()
   {
      if (classLoader == null)
         throw new RuntimeException();
      else
         return classLoader;
   }
}
