/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.model.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;

/**
 *  Abstract class relating with the <code>ICacheInstance</code>
 * <p>
 * Overrides all methods in the ICacheInstance,so implementor of the ICacheInstance
 * extends this class 
 * </p>
 * @author Owner
 */
public abstract class AbstractCacheInstance implements ICacheInstance
{

   /**Tree Cache node name*/
   protected String instanceName;

   /**Tree Cache node fqn*/
   protected String fqnName;

   /**Tree Cache node parent*/
   protected ICacheInstance instanceParent;

   /**Tree Cache child nodes*/
   protected List instanceChilds;

   /**Root instance name (which configuration this node belongs)*/
   protected ICacheRootInstance rootInstance;

   /**Whether or not this node is root of the TreeCache*/
   protected boolean isRootNode = false;

   /**This map contains node content with key-value pairs*/
   protected Map mapCacheInstanceKeys;

   /**Interested listeners*/
   protected static List cacheInstanceListeners;

   protected AbstractCacheInstance(String instanceName, String fqnName, ICacheInstance parent)
   {
      this.instanceName = instanceName;
      this.instanceParent = parent;
      this.fqnName = fqnName;
   }

   /**
    * @see ICacheInstance#getInstanceName()
    */
   public String getInstanceName()
   {
      return instanceName;
   }

   /**
    * @see ICacheInstance#setInstanceName(String)
    */
   public void setInstanceName(String name)
   {
      this.instanceName = name;
   }

   /**
    * @see ICacheInstance#getInstanceParent()
    */
   public ICacheInstance getInstanceParent()
   {
      return instanceParent;
   }

   /**
    * @see ICacheInstance#setInstanceParent()
    */
   public void setInstanceParent(ICacheInstance parent)
   {
      this.instanceParent = parent;
   }

   /**
    * @see ICacheInstance#getInstanceChilds()
    */
   public List getInstanceChilds()
   {
      return instanceChilds;
   }

   /**
    * @see ICacheInstance#setRootInstance(ICacheRootInstance)
    */
   public void setRootInstance(ICacheRootInstance rootInstance)
   {
      this.rootInstance = rootInstance;
   }

   /**
    * @see ICacheInstance#getRootInstance()
    */
   public ICacheRootInstance getRootInstance()
   {
      return rootInstance;
   }

   /**
    * @see ICacheInstance#hasRootInstance()
    */
   public boolean hasRootInstance()
   {
      return (rootInstance != null) ? true : false;
   }

   /**
    * @see ICacheInstance#hasChilds()
    */
   public boolean hasChilds()
   {
      if (instanceChilds == null)
         return false;
      else
      {
         return ((instanceChilds.size() > 0) ? true : false);
      }
   }

   /**
    * @see ICacheInstance#addInstanceChild(ICacheInstance)
    */
   public void addInstanceChild(final ICacheInstance child)
   {
      if (instanceChilds == null)
      {
         instanceChilds = new ArrayList();
         instanceChilds.add(child);
      }
      else
      {
         instanceChilds.add(child);
      }
      
      JBossCachePlugin.getDisplay().asyncExec(new Runnable(){

         public void run()
         {
            fireNewCacheInstanceAdded(child);
            
         }
         
      });
   }

   /**
    * @see ICacheInstance#removeInstanceChild(ICacheInstance)
    */
   public void removeInstanceChild(ICacheInstance child)
   {
      if (instanceChilds != null)
      {
         instanceChilds.remove(child);
      }
      fireNewCacheInstanceRemoved(child);
   }

   /**
    * @see ICacheInstance#deleteInstance()
    */
   public void deleteInstance()
   {
      if (instanceChilds != null)
         instanceChilds = null;

      if (rootInstance != null)
      {
         rootInstance.deleteRootChild(this);
      }

      if (instanceParent != null)
         instanceParent.removeInstanceChild(this);

      instanceName = null;
   }

   /**
    * @see ICacheInstance#isRootNode()
    */
   public boolean isRootNode()
   {
      return isRootNode;
   }

   /**
    * @see ICacheInstance#setRootNode(boolean)
    */
   public void setRootNode(boolean isRootNode)
   {
      this.isRootNode = isRootNode;
   }

   /**
    * Add new listener to the cache instance
    * @param listener
    */
   public static void addCacheInstanceListener(ICacheInstanceListener listener)
   {
      if (listener != null)
      {
         if (cacheInstanceListeners != null)
         {
            cacheInstanceListeners.add(listener);
         }
         else
         {
            cacheInstanceListeners = new ArrayList();
            cacheInstanceListeners.add(listener);
         }
      }
   }

   /**
    * Remove existing listener from the listener list
    * @param listener
    */
   public static void removeCacheInstanceListener(ICacheInstanceListener listener)
   {
      if (listener != null)
      {
         if (cacheInstanceListeners != null)
         {
            cacheInstanceListeners.remove(listener);
         }
      }
   }

   /**
    * Fire all listener that the new cache instance added
    * @param instance
    */
   protected synchronized void fireNewCacheInstanceAdded(ICacheInstance instance)
   {
      if (cacheInstanceListeners != null)
      {
         Iterator it = cacheInstanceListeners.iterator();
         while (it.hasNext())
         {
            ICacheInstanceListener listener = (ICacheInstanceListener) it.next();
            listener.cacheInstanceAdded(instance);
         }
      }
   }

   /**
    * Fire all listener that the new cache instance removed
    * @param instance
    */
   protected synchronized void fireNewCacheInstanceRemoved(ICacheInstance instance)
   {
      if (cacheInstanceListeners != null)
      {
         Iterator it = cacheInstanceListeners.iterator();
         while (it.hasNext())
         {
            ICacheInstanceListener listener = (ICacheInstanceListener) it.next();
            listener.cacheInstanceRemoved(instance);
         }
      }
   }

   /**
    * @see ICacheInstance#setFqnName(String)
    */
   public void setFqnName(String fqn)
   {
      this.fqnName = fqn;
   }

   public String getFqnName()
   {
      return this.fqnName;
   }

   /**
    * @see ICacheInstance#getMapCacheInstanceKeys()
    */
   public Map getMapCacheInstanceKeys()
   {
      return mapCacheInstanceKeys;
   }

   /**
    * @see ICacheInstance#setMapCacheInstanceKeys(Map)
    */
   public void setMapCacheInstanceKeys(Map mapCacheInstanceKeys)
   {
      this.mapCacheInstanceKeys = mapCacheInstanceKeys;
   }

   /**
    * @see ICacheInstance#addNewCacheInstanceKey(Object, Object)
    */
   public void addNewCacheInstanceKey(Object key, Object value)
   {
      if (mapCacheInstanceKeys == null)
         mapCacheInstanceKeys = new HashMap();

      mapCacheInstanceKeys.put(key, value);
   }

   /**
    * @see ICacheInstance#deleteCacheInstanceKey(Object)
    */
   public void deleteCacheInstanceKey(Object key)
   {
      if (checkMap())
      {
         if (mapCacheInstanceKeys.containsKey(key))
            mapCacheInstanceKeys.remove(key);

         return;
      }

      return;
   }

   /**
    * @see ICacheInstance#getCacheInstanceKeyValue(Object)
    */
   public Object getCacheInstanceKeyValue(Object key)
   {
      if (checkMap())
      {
         if (mapCacheInstanceKeys.containsKey(key))
            return mapCacheInstanceKeys.get(key);
         else
            return null;
      }
      else
         return null;
   }

   /**
    * Updates gicen key value 
    * @param key
    * @return
    */
   public void updateCacheInstanceKeyValue(Object key, Object value)
   {
      if (checkMap())
      {
         if (mapCacheInstanceKeys.containsKey(key))
            mapCacheInstanceKeys.put(key, value);
         else
            return;
      }
      else
         return;
   }

   /**
    * Map is null or not
    * @return
    */
   private boolean checkMap()
   {
      if (mapCacheInstanceKeys == null)
         return false;
      else
         return true;
   }

   /**
    * Return whether or not this instnace contians key-values
    * @return
    */
   public boolean hasKeyValueMap()
   {
      if (!checkMap())
         return false;
      else
      {
         return mapCacheInstanceKeys.isEmpty();
      }
   }

}//end of class