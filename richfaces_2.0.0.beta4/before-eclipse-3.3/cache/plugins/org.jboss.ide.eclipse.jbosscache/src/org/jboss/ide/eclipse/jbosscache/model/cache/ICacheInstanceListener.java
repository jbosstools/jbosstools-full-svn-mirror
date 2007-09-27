/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.model.cache;

/**
 * Listener interface that is used to register any <code>CacheInstance</code>
 * operations
 * <p>
 * Any party that wants to inform the CacheInstance object process
 * register with this listener 
 * @author Gurkaner
 */
public interface ICacheInstanceListener
{

   /**
    * When the new cache instance is added, this method is called on the registered listeners 
    */
   void cacheInstanceAdded(ICacheInstance instance);

   /**
    * When existing cache instance is removed, this method is called on the registered listeners
    */
   void cacheInstanceRemoved(ICacheInstance instance);

}
