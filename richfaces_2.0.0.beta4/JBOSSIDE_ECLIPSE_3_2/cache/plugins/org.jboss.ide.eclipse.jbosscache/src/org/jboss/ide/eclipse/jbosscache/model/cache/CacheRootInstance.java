/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.model.cache;

/**
 * This class instances will be the main root of all tree cache node objects
 * It has one configuration file related with it
 * <p>
 * When the new cache configuration is created, this class instance will be 
 * created to hold cache node instances in the view
 * </p>
 * @see org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance
 * @see org.jboss.ide.eclipse.jbosscache.model.cache.AbstractCacheRootInstance
 * @author Owner
 */
public class CacheRootInstance extends AbstractCacheRootInstance
{

   private static final CacheRootInstance mainRootInstance = new CacheRootInstance();

   /**
    * Default Constructor
    *
    */
   protected CacheRootInstance()
   {
      this(null, null);
   }

   protected CacheRootInstance(String name, String confFileName)
   {
      super(name, confFileName);
   }

   public static CacheRootInstance getMainRootInstance()
   {
      return mainRootInstance;
   }

   /**
    * @see ICacheRootInstance#getMainRootCacheInstance()
    */
   public ICacheRootInstance getMainRootCacheInstance()
   {
      return mainRootInstance;
   }

   public static ICacheRootInstance getRootInstance(String name, String confFile)
   {
      ICacheRootInstance rootInstance = new CacheRootInstance(name, confFile);
      return rootInstance;
   }

}//end of class