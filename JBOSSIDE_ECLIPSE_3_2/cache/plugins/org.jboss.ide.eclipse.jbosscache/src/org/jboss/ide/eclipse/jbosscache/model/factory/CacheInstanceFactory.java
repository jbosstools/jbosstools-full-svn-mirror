/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.model.factory;

import org.jboss.ide.eclipse.jbosscache.model.cache.CacheInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.CacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;

/**
 * This class is factory for the cache instance objects.
 * @author Owner
 *
 */
public class CacheInstanceFactory
{

   /**
    * Not instantiate
    *
    */
   private CacheInstanceFactory()
   {

   }

   /**
    * Return newly created cache instance objects
    * @return
    */
   public static ICacheInstance getCacheInstance(String instanceName, String fqnName, ICacheInstance parent)
   {
      return CacheInstance.getCacheInstance(instanceName, fqnName, parent);
   }//end of method

   /**
    * Return new cache root instance with null properties
    * @return
    */
   public static ICacheRootInstance getCacheRootInstance()
   {
      return CacheRootInstance.getRootInstance(null, null);
   }

   /**
    * Return new cache root instance with given name and configuration file name
    * @return
    */
   public static ICacheRootInstance getCacheRootInstance(String name, String fileName)
   {
      return CacheRootInstance.getRootInstance(name, fileName);
   }

   /** 
    * All tree cache unvisible root
    * @return
    */
   public static ICacheRootInstance getCacheRootMainInstance()
   {
      return CacheRootInstance.getMainRootInstance();
   }

   public static void main(String[] args)
   {
   }
}
