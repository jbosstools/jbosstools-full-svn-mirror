/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.model.cache;

import java.util.List;
import java.util.Map;

/**
 * This interface is used to get TreeCache node related operations 
 * @author Owner
 */
public interface ICacheInstance
{

   /**
    * Name of this cache node instance
    * <p>
    * This name will be shown in the view
    * </p>
    * @return
    */
   String getInstanceName();

   /**
    * Set name of this cache node instance
    * <p>
    * This name will be shown in the view
    * </p>
    * @return
    */
   void setInstanceName(String name);

   /**
    * Parent cache node instance,if any, of this instance
    * @return
    */
   ICacheInstance getInstanceParent();

   /**
    * Set parent node
    * @return
    */
   void setInstanceParent(ICacheInstance parent);

   /**
    * Child cache nodes of this cache node instance, if any
    * @return
    */
   List getInstanceChilds();

   /**
    * Sets main cache node(tree cache root)'s cache root instance
    * @param rootInstance
    */
   void setRootInstance(ICacheRootInstance rootInstance);

   /**
    * Return this cache node root instance if any
    * @return
    */
   ICacheRootInstance getRootInstance();

   /**
    * Whether or not this node has root instance(namely; it is the root of the TreeCache)
    * @return
    */
   boolean hasRootInstance();

   /**
    * Whether or not this node has childs node
    * @return
    */
   boolean hasChilds();

   /**
    * Add new child
    * @param child
    */
   void addInstanceChild(ICacheInstance child);

   /**
    * Remove child
    * @param child
    */
   void removeInstanceChild(ICacheInstance child);

   /**
    * Delete node
    *
    */
   void deleteInstance();

   /**
    * Whether or not root of the TreeCache
    * @return
    */
   boolean isRootNode();

   /**
    * Sets the root of the TreeCache
    */
   void setRootNode(boolean isRootNode);

   /**
    * Sets fqn of this node
    * @param fqn
    */
   void setFqnName(String fqn);

   /**
    * Get fqn of this node
    * @return
    */
   String getFqnName();

   /**
    * Get this cache instance content
    * @return
    */
   Map getMapCacheInstanceKeys();

   /**
    * Set this cache instance content
    * @param mapCacheInstanceKeys
    */
   void setMapCacheInstanceKeys(Map mapCacheInstanceKeys);

   /**
    * Add new key-value pair
    * @param key
    * @param value
    */
   void addNewCacheInstanceKey(Object key, Object value);

   /**
    * Delete given key from the cache instance
    * @param key
    */
   void deleteCacheInstanceKey(Object key);

   /**
    * Get given key value from the instance cache
    * @param key
    * @return
    */
   Object getCacheInstanceKeyValue(Object key);

   /**
    * Updates gicen key value 
    * @param key
    * @return
    */
   void updateCacheInstanceKeyValue(Object key, Object value);

   /**
    * Return whether or not this instnace contians key-values
    * @return
    */
   boolean hasKeyValueMap();

}//end of interface