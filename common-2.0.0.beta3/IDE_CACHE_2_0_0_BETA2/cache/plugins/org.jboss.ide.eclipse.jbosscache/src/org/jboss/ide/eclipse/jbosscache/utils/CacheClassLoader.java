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


/**
 * This class represents class loader that will be used for marshalling in cache
 * @author Owner
 */
public final class CacheClassLoader
{


   /**
    * Return URLClassLoader
    * @param jarPaths
    * @return
    * @throws Exception
    */
   public static URLClassLoader getCacheClassLoaderInstance(List jarPaths, ClassLoader parent) throws Exception
   {
      URLClassLoader classLoader = null;

      List urls = new ArrayList();
      for (int i = 0; i < jarPaths.size(); i++)
      {
         File file = new File(jarPaths.get(i).toString());
         urls.add(file.toURL());
      }

         classLoader = new URLClassLoader((URL[]) urls.toArray(new URL[urls.size()]), parent);

      return classLoader;
   }

}
