/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.model.cache;

/**
 * Listener interface that is used to register any <code>CacheRootInstance</code>
 * operations
 * <p>
 * Any party that wants to inform the CacheRootInstance object process
 * register with this listener 
 * @author Gurkaner
 */
public interface ICacheRootInstanceListener
{

   /**
    * When the new CacheRootInstance is added, this method is called on the registered listeners 
    */
   void cacheRootInstanceAdded(ICacheRootInstance rootInstance);

   /**
    * When existing CacheRootInstance is removed, this method is called on the registered listeners
    */
   void cacheRootInstanceRemoved(ICacheRootInstance rootInstance);

   /**
    * When the new cache node added, this method is called on the registered listeners
    * @param cacheInstance
    */
   void cacheInstanceToRootAdded(ICacheInstance cacheInstance);

   /**
    * When the cache node removed, this method is called on the registered listeners
    * @param cacheInstance
    */
   void cacheInstanceToRootRemoved(ICacheInstance cacheInstance);

}
