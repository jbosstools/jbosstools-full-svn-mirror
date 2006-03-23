/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.model.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.swt.graphics.Image;
import org.jboss.cache.TreeCache;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.model.config.CacheConfigParams;
import org.jboss.ide.eclipse.jbosscache.model.config.RemoteCacheConfigParams;
import org.jboss.ide.eclipse.jbosscache.model.internal.RemoteCacheManager;
import org.jboss.ide.eclipse.jbosscache.model.internal.TreeCacheManager;

/**
 * Abstract class relating with the ICacheRootInstance
 * <p>
 *  Overrides all methods in the ICacheRootInstance,so implementor of the ICacheRootInstance
 *  extends this class 
 * </p>
 * @author Owner
 */
public abstract class AbstractCacheRootInstance extends PlatformObject implements ICacheRootInstance
{

   protected String rootName;

   protected String configFileName;

   protected CacheConfigParams cacheConfigParams;//for local cache
   
   protected RemoteCacheConfigParams remoteCacheConfigParams;//for remote cache

   protected List rootChilds;

   protected Image rootImage;

   protected List rootInstanceChilds;

   protected static List cacheRootListeners;

   protected static List cacheConnectionListeners;

   protected boolean isConnected = false;
   
   protected boolean isRemoteCache= false;

   protected boolean isDirty = false;

   /**Cache type is TreeCache or TreeCacheAOP*/
   private String cacheType = ICacheConstants.JBOSS_CACHE_TREE_CACHE;//Default as TreeCache

   private String rootLabel;

   /**TreeCache instance with this root instance configuration*///local cache
   protected TreeCacheManager treeCacheManager;
   
   protected RemoteCacheManager remoteCacheManager;//remote cache

   /**Related Listener with this root instance*/
   protected AbstractTreeCacheManagerListener cacheNodeListener;

   protected AbstractCacheRootInstance(String rootName, String configFileName)
   {

      this.rootName = rootName;
      this.configFileName = configFileName;
      this.rootLabel = rootName;
   }

   public void setRootLabel(String rootLabel)
   {
      this.rootLabel = rootLabel;
   }

   public String getRootLabel()
   {
      return this.rootLabel;
   }

   /**
    * @see ICacheRootInstance#getRootName()
    */
   public String getRootName()
   {
      return this.rootName;
   }

   /**
    * @see ICacheRootInstance#getRootConfigurationFileName()
    */
   public String getRootConfigurationFileName()
   {
      return this.configFileName;
   }

   /**
    * @see ICacheRootInstance#getRootChilds()
    */
   public List getRootChilds()
   {
      return this.rootChilds;
   }

   /**
    * @see ICacheRootInstance#addRootChild(ICacheInstance)
    */
   public void addRootChild(final ICacheInstance cacheInstance)
   {
      if (this.rootChilds == null)
      {
         rootChilds = new ArrayList();
         rootChilds.add(cacheInstance);
      }
      else
         rootChilds.add(cacheInstance);

      cacheInstance.setRootNode(true);
      cacheInstance.setRootInstance(this);
      
      JBossCachePlugin.getDisplay().asyncExec(new Runnable(){

         public void run()
         {
            fireNewInstanceToRootAdded(cacheInstance);
            
         }
         
         
      });

     
   }

   /**
    * @see ICacheRootInstance#deleteRootChild(ICacheInstance)
    */
   public void deleteRootChild(ICacheInstance cacheinstance)
   {
      if (this.rootChilds != null)
         rootChilds.remove(cacheinstance);
      fireNewInstanceToRootRemoved(cacheinstance);
   }

   /**
    * @see ICacheRootInstance#deleteRoot()
    */
   public void deleteRoot()
   {
      if (rootChilds != null)
         rootChilds = null;

      if(!isRemoteCache)
      {
         cacheConfigParams = null;   
         configFileName = null;  
      }
      
      fireNewRootInstanceRemoved(this);
   }

   /**
    * @see ICacheRootInstance#hasRootChilds()
    */
   public boolean hasRootChilds()
   {
      if (this.rootChilds == null)
         return false;
      else
         return (this.rootChilds.size() != 0) ? true : false;
   }

   /**
    * @see ICacheRootInstance#setRootName(String)
    */
   public void setRootName(String rootName)
   {
      this.rootName = rootName;
   }

   /**
    * @see ICacheRootInstance#setRootConfigurationFileName(String)
    */
   public void setRootConfigurationFileName(String confFileName)
   {
      this.configFileName = confFileName;
   }

   /**
    * @see ICacheRootInstance#setImage(Image)
    */
   public void setImage(Image image)
   {

   }

   /**
    * @see ICacheRootInstance#addRootInstanceChild(ICacheRootInstance)
    */
   public void addRootInstanceChild(ICacheRootInstance instance)
   {
      if (rootInstanceChilds == null)
         rootInstanceChilds = new ArrayList();

      rootInstanceChilds.add(instance);
      fireNewRootInstanceAdded(instance);
   }

   /**
    * @see ICacheRootInstance#getRootInstanceChild(ICacheRootInstance)
    */
   public List getRootInstanceChilds()
   {
      return this.rootInstanceChilds;
   }

   /**
    * @see ICacheRootInstance#hasRootInstanceChilds()
    */
   public boolean hasRootInstanceChilds()
   {
      if (rootInstanceChilds == null)
         return false;
      else
         return (rootInstanceChilds.size() > 0) ? true : false;
   }

   /**
    * Add new listener to the cache root instance
    * @param listener
    */
   public static void addCacheRootListener(ICacheRootInstanceListener listener)
   {
      if (listener != null)
      {
         if (cacheRootListeners != null)
         {
            cacheRootListeners.add(listener);
         }
         else
         {
            cacheRootListeners = new ArrayList();
            cacheRootListeners.add(listener);
         }
      }
   }

   /**
    * Remove existing listener from the listener list
    * @param listener
    */
   public static void removeCacheRootListener(ICacheRootInstanceListener listener)
   {
      if (listener != null)
      {
         if (cacheRootListeners != null)
         {
            cacheRootListeners.remove(listener);
         }
      }
   }

   /**
    * Fire all listener that the new cache root instance added
    * @param instance
    */
   protected synchronized void fireNewRootInstanceAdded(ICacheRootInstance instance)
   {
      if (cacheRootListeners != null)
      {
         Iterator it = cacheRootListeners.iterator();
         while (it.hasNext())
         {
            ICacheRootInstanceListener listener = (ICacheRootInstanceListener) it.next();
            listener.cacheRootInstanceAdded(instance);
         }
      }
   }

   /**
    * Fire all listener that the new cache root instance added
    * @param instance
    */
   protected synchronized void fireNewRootInstanceRemoved(ICacheRootInstance instance)
   {
      if (cacheRootListeners != null)
      {
         Iterator it = cacheRootListeners.iterator();
         while (it.hasNext())
         {
            ICacheRootInstanceListener listener = (ICacheRootInstanceListener) it.next();
            listener.cacheRootInstanceRemoved(instance);
         }
      }
   }

   /**
    * Fire all listener that the new cache instance added to this root
    * @param instance
    */
   protected synchronized void fireNewInstanceToRootAdded(ICacheInstance instance)
   {
      if (cacheRootListeners != null)
      {
         Iterator it = cacheRootListeners.iterator();
         while (it.hasNext())
         {
            ICacheRootInstanceListener listener = (ICacheRootInstanceListener) it.next();
            listener.cacheInstanceToRootAdded(instance);
         }
      }
   }

   /**
    * Fire all listener that the cache instance removed from this root
    * @param instance
    */
   protected synchronized void fireNewInstanceToRootRemoved(ICacheInstance instance)
   {
      if (cacheRootListeners != null)
      {
         Iterator it = cacheRootListeners.iterator();
         while (it.hasNext())
         {
            ICacheRootInstanceListener listener = (ICacheRootInstanceListener) it.next();
            listener.cacheInstanceToRootRemoved(instance);
         }
      }
   }

   /**
    * @see ICacheRootInstance#isConnected()
    */
   public boolean isConnected()
   {
      return isConnected;
   }

   /**
    * @see ICacheRootInstance#setConnected(boolean)
    */
   public void setConnected(boolean isConnected)
   {
      this.isConnected = isConnected;
   }

   /**
    * @see ICacheRootInstance#getTreeCache()
    */
   public TreeCacheManager getTreeCacheManager()
   {
      return this.treeCacheManager;
   }

   /**
    * @see ICacheRootInstance#setTreeCache(TreeCache)
    */
   public void setTreeCacheManager(TreeCacheManager treeCacheManager)
   {
      this.treeCacheManager = treeCacheManager;
   }

   /**
    * @see ICacheRootInstance#setRootChilds(List)
    */
   public void setRootChilds(List list)
   {
      this.rootChilds = list;
   }

   /**
    * @see ICacheRootInstance#connect()
    */
   public void connect()
   {
      fireConnection(this);
   }

   /**
    * @see ICacheRootInstance#disconnect()
    */
   public void disconnect()
   {
      this.isConnected = false;
      this.rootChilds = null;
      fireDisconnected(this);
   }

   /**
    * Add new listener to the cache root instance connect
    * @param listener
    */
   public static void addConnectListener(ICacheRootInstanceConnectionListener listener)
   {
      if (listener != null)
      {
         if (cacheConnectionListeners != null)
         {
            cacheConnectionListeners.add(listener);
         }
         else
         {
            cacheConnectionListeners = new ArrayList();
            cacheConnectionListeners.add(listener);
         }
      }
   }

   /**
    * Remove existing listener from the connect listener list
    * @param listener
    */
   public static void removeConnectListener(ICacheRootInstanceConnectionListener listener)
   {
      if (listener != null)
      {
         if (cacheConnectionListeners != null)
         {
            cacheConnectionListeners.remove(listener);
         }
      }
   }

   /**
    * Fire all listener that the new cache instance added to this root
    * @param instance
    */
   protected synchronized void fireConnection(ICacheRootInstance instance)
   {
      if (cacheConnectionListeners != null)
      {
         Iterator it = cacheConnectionListeners.iterator();
         while (it.hasNext())
         {
            ICacheRootInstanceConnectionListener listener = (ICacheRootInstanceConnectionListener) it.next();
            listener.cacheRootInstanceConnected(instance);
         }
      }
   }

   /**
    * Fire all listener that the cache instance removed from this root
    * @param instance
    */
   protected synchronized void fireDisconnected(ICacheRootInstance instance)
   {
      if (cacheConnectionListeners != null)
      {
         Iterator it = cacheConnectionListeners.iterator();
         while (it.hasNext())
         {
            ICacheRootInstanceConnectionListener listener = (ICacheRootInstanceConnectionListener) it.next();
            listener.cacheRootInstanceDisConnected(instance);
         }
      }
   }

   /**
    * Get cache instance related params
    * @return
    */
   public CacheConfigParams getCacheConfigParams()
   {
      return this.cacheConfigParams;
   }
   
   public RemoteCacheConfigParams getRemoteCacheConfigParams(){
      return this.remoteCacheConfigParams;
   }
   
   public void setRemoteCacheConfigParams(RemoteCacheConfigParams params){
      if(!isRemoteCache)
         throw new UnsupportedOperationException();
   
      this.remoteCacheConfigParams = params;
   }


   /**
    * Set cache instance related params
    * @param jarFiles
    */
   public void setCacheConfigParams(CacheConfigParams params)
   {
      this.cacheConfigParams = params;
   }

   public void setCacheViewNodeListener(AbstractTreeCacheManagerListener listener)
   {
      this.cacheNodeListener = listener;
   }

   public AbstractTreeCacheManagerListener getCacheViewNodeListener()
   {
      return this.cacheNodeListener;
   }

   /**
    * @see ICacheRootInstance#setCacheType(String)
    */
   public void setCacheType(String cacheType)
   {
      this.cacheType = cacheType;
   }

   /**
    * @see ICacheRootInstance#getCacheType()
    */
   public String getCacheType()
   {
      return this.cacheType;
   }

   public boolean equals(Object obj)
   {
      try{
         if (!(obj instanceof ICacheRootInstance))
            return false;
         ICacheRootInstance rootInstance = (ICacheRootInstance) obj;
   
         if (rootInstance.getRootName() == null)
         {
            if (getRootName() == null)
               return true;
            else
               return false;
         }
   
         return rootInstance.getRootName().equals(getRootName());
      }catch(Exception e){
         System.out.println("In Equals method");
         e.printStackTrace();
         
      }
      return true;
   }

   public int hashCode()
   {
      if (getRootName() == null)
         return new String("Root").hashCode();

      return getRootName().hashCode();
   }

   public ICacheRootInstance getMainRootCacheInstance()
   {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * 
    */
   public boolean getIsDirty()
   {
      return this.isDirty;
   }

   /**
    * 
    */
   public void setIsDirty(boolean isDirty)
   {
      this.isDirty = isDirty;
   }

   public boolean isRemoteCache()
   {
      return isRemoteCache;
   }

   public void setRemoteCache(boolean isRemoteCache)
   {
      this.isRemoteCache = isRemoteCache;
   }
   
   /**
    * Return this root instance's TreeCache
    * @return
    */
   public RemoteCacheManager getRemoteCacheManager(){
      return this.remoteCacheManager;
   }

   /**
    * Sets this root instance's TreeCache
    * @param treeCache
    */
   public void setRemoteCacheManager(RemoteCacheManager treeCache){
      this.remoteCacheManager = treeCache;
   }


}//end of class