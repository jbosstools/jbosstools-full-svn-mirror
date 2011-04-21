/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.model.cache;

import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.jboss.ide.eclipse.jbosscache.model.config.CacheConfigParams;
import org.jboss.ide.eclipse.jbosscache.model.config.RemoteCacheConfigParams;
import org.jboss.ide.eclipse.jbosscache.model.internal.RemoteCacheManager;
import org.jboss.ide.eclipse.jbosscache.model.internal.TreeCacheManager;

/**
 * This interface is an API that is implemented with CacheRootInstance objects 
 * @author Owner
 */
public interface ICacheRootInstance
{

   /**
    * Name of this root object. 
    * <p>
    * It is typically created when the new cache configuration will be created
    * </p>
    * @return
    */
   String getRootName();

   /**
    * Name of the configuration file
    * @return
    */
   String getRootConfigurationFileName();

   /**
    * Childs of this root instance
    * @return
    */
   List getRootChilds();

   /**
    * Sets root childs
    */
   void setRootChilds(List list);

   /**
    * Add new cache instance to this root as child
    */
   void addRootChild(ICacheInstance cacheInstance);

   /**
    * Delete new cache instance from this configuration root
    * @param cacheinstance
    */
   void deleteRootChild(ICacheInstance cacheinstance);

   /**
    * Delete this root from the view
    *
    */
   void deleteRoot();

   /**
    * Whether or not this root has childs
    * @return
    */
   boolean hasRootChilds();

   /**
    * Sets this root name
    * @param rootName
    */
   void setRootName(String rootName);

   /**
    * Sets this root configuration file name
    * @param confFileName
    */
   void setRootConfigurationFileName(String confFileName);

   /**
    * Sets image of this root instance
    * @param image
    */
   void setImage(Image image);

   /**
    * Main root of the cache root instances
    * @return
    */
   ICacheRootInstance getMainRootCacheInstance();

   /**
    * Set the new root instance parent to the main root instance
    *
    */
   void addRootInstanceChild(ICacheRootInstance instance);

   /**
    * Get the root instance list belong to the main root
    *
    */
   List getRootInstanceChilds();

   /**
    * Whether or not main root instance has a root instance child
    * @return
    */
   boolean hasRootInstanceChilds();

   /**
    * Whether or not this cache root instance is connected
    * @return
    */
   boolean isConnected();

   /**
    * Sets connection status of this cache root instance
    * @param isConnected
    */
   void setConnected(boolean isConnected);

   /**
    * Return this root instance's TreeCache
    * @return
    */
   TreeCacheManager getTreeCacheManager();

   /**
    * Sets this root instance's TreeCache
    * @param treeCache
    */
   void setTreeCacheManager(TreeCacheManager treeCache);
   
   /**
    * Return this root instance's TreeCache
    * @return
    */
   RemoteCacheManager getRemoteCacheManager();

   /**
    * Sets this root instance's TreeCache
    * @param treeCache
    */
   void setRemoteCacheManager(RemoteCacheManager treeCache);


   /**
    * Root instance is connected to cache with its configuration
    */
   void connect();

   /**
    * Root instance is disconnected from the cache
    */
   void disconnect();

   void setRootLabel(String rootLabel);

   String getRootLabel();

   /**
    * Get cache instance related params
    * @return
    */
   CacheConfigParams getCacheConfigParams();

   /**
    * Set cache instance related params
    * @param jarFiles
    */
   void setCacheConfigParams(CacheConfigParams params);
   
   RemoteCacheConfigParams getRemoteCacheConfigParams();
   
   void setRemoteCacheConfigParams(RemoteCacheConfigParams params);

   void setCacheViewNodeListener(AbstractTreeCacheManagerListener listener);

   AbstractTreeCacheManagerListener getCacheViewNodeListener();

   /**
    * Sets type of the cache
    * @param cacheType Type of the cache
    */
   void setCacheType(String cacheType);

   /**
    * Return type of the cache, TreeCache or TreeCacheAOP
    * @return cache type
    */
   String getCacheType();

   boolean getIsDirty();

   void setIsDirty(boolean isDirty);
   
   boolean isRemoteCache();
   
   void setRemoteCache(boolean isRemote);

}//end of interface
