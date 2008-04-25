/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.classpath;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class CacheVersion124CpContainer extends CacheClassPathContainer
{
   public static final String CONTAINER_ID = "org.jboss.ide.eclipse.jbosscache.classpath.CACHE_124";
   public static final String DESCRIPTION = "JBoss Cache Libraries";
   
   protected static final IPath[] cacheJars = new IPath[]{new Path("lib/bsh-2.0b4.jar"),
                                                          new Path("lib/commons-logging.jar"),
                                                          new Path("lib/concurrent.jar"),
                                                          new Path("lib/javassist.jar"),
                                                          new Path("lib/jboss-aop.jar"),
                                                          new Path("lib/jboss-cache.jar"),
                                                          new Path("lib/jboss-common.jar"),
                                                          new Path("lib/jboss-j2ee.jar"),
                                                          new Path("lib/jboss-jmx.jar"),
                                                          new Path("lib/jboss-minimal.jar"),
                                                          new Path("lib/jboss-system.jar"),
                                                          new Path("lib/jgroups.jar"),
                                                          new Path("lib/jrunit.jar"),
                                                          new Path("lib/junit.jar"),
                                                          new Path("lib/log4j.jar"),
                                                          new Path("lib/qdox.jar"),
                                                          new Path("lib/trove.jar")};
   
   public CacheVersion124CpContainer(IPath path){
      super(path);
   }
   
   protected IPath[] getCacheJarRelativePaths()
   {
      return cacheJars;
   }
   
   public String getContainerId()
   {
      return CONTAINER_ID;
   }
   public String getDescription()
   {
      return DESCRIPTION;
   }

}
