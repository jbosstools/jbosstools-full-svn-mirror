/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.model.cache;

/**
 * This class provides cache instance model objects
 * <p>
 * Every cache instance object represents a node in the <code>TreeCacheView</code>
 * </p>
 * @author Owner
 * @see org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheViewContentProvider
 */
public class CacheInstance extends AbstractCacheInstance
{

   /**
    * Constructor
    * @param name
    * @param parent
    */
   protected CacheInstance(String name, String fqnName, ICacheInstance parent)
   {
      super(name, fqnName, parent);
   }

   /**
    * Get cache instance
    * @param name
    * @param parent
    * @return
    */
   public static ICacheInstance getCacheInstance(String name, String fqnName, ICacheInstance parent)
   {
      ICacheInstance ins = new CacheInstance(name, fqnName, parent);
      return ins;
   }

}
