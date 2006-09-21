/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.classpath;

import org.eclipse.core.runtime.IPath;

public class CacheVersion130CpContainer extends CacheClassPathContainer
{
   public static final String CONTAINER_ID = "org.jboss.ide.eclipse.jbosscache.classpath.CACHE_13";
   public static final String DESCRIPTION = "JBoss Cache 1.3.0 Libraries";
   
   public CacheVersion130CpContainer(IPath path){
      super(path);
   }
   
   protected IPath[] getCacheJarRelativePaths()
   {
      // FIXME getCacheJarRelativePaths
      return null;
   }
   public String getContainerId()
   {
      // FIXME getContainerId
      return null;
   }
   public String getDescription()
   {
      // FIXME getDescription
      return null;
   }

}
