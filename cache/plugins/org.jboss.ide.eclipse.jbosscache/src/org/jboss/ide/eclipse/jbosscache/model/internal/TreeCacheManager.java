/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.model.internal;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.jboss.cache.PropertyConfigurator;
import org.jboss.cache.TreeCache;
import org.jboss.cache.aop.TreeCacheAop;

/**
 * Internal class for TreeCache related operations
 * @author Owner
 */
public class TreeCacheManager
{

   private TreeCacheAop treeCacheAopInstance;

   private TreeCache treeCache;
   
   private ClassLoader managerClassLoader;

   /**
    * Constructor
    * @throws Exception
    */
   protected TreeCacheManager() throws Exception
   {
      super();
   }

   /**
    * Constructor
    * @throws Exception
    */
   protected TreeCacheManager(boolean isTreeCacheAop) throws Exception
   {
      if (isTreeCacheAop)
      {
         treeCacheAopInstance = new TreeCacheAop();
      }
      else
         treeCache = new TreeCache();
   }

   /**
    * Sets given TreeCacheManager instance with given listener class
    * @param treeCacheManager
    * @param listener
    */
   public static void addTreeCacheManagerListener(TreeCacheManager treeCacheManager, ITreeCacheManagerListener listener)
   {
      treeCacheManager.getTreeCache().addTreeCacheListener(listener);
   }

   /**
    * Remove given listner from TreeCacheManager
    * @param treeCacheManager
    * @param listener
    */
   public static void removeTreeCacheManagerListener(TreeCacheManager treeCacheManager,
         ITreeCacheManagerListener listener)
   {
      treeCacheManager.getTreeCache().removeTreeCacheListener(listener);
   }

   /**
    * Provide new Tree Cache instance
    * @return
    * @throws Exception
    */
   public static TreeCacheManager getTreeCacheManagerInstance() throws Exception
   {
      return new TreeCacheManager();
   }

   /**
    * Provide new Tree Cache instance
    * @return
    * @throws Exception
    */
   public static TreeCacheManager getTreeCacheManagerInstance(boolean isTreeCacheAOP) throws Exception
   {
      return new TreeCacheManager(isTreeCacheAOP);
   }

   /**
    * Configure the given tree cache manager
    * @param treeCacheManager
    * @return
    */
   public static TreeCacheManager configureTreeCacheManager(TreeCacheManager treeCacheManager, InputStream is)
         throws Exception
   {
      PropertyConfigurator propConf = new PropertyConfigurator();
      try
      {
         propConf.configure(treeCacheManager.getTreeCache(), is);
         return treeCacheManager;
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw e;
      }
      finally
      {
         is.close();
      }
   }

   /**
    */
   public static void createService_(TreeCacheManager cacheManager) throws Exception
   {
      cacheManager.getTreeCache().createService();
   }

   /**
    * 
    */
   public static void startService_(TreeCacheManager cacheManager) throws Exception
   {
      cacheManager.getTreeCache().startService();
   }

   /**
    * 
    */
   public static void stopService_(TreeCacheManager cacheManager)
   {
      cacheManager.getTreeCache().stopService();
   }

   /**
    * 
    */
   public static void destroyService_(TreeCacheManager cacheManager)
   {
      cacheManager.getTreeCache().destroyService();
   }

   /**
    * :Get keys from node
    * @param manager
    * @param fqn
    * @return
    * @throws Exception
    */
   public static Set getKeys_(TreeCacheManager manager, String fqn) throws Exception
   {
      return manager.getTreeCache().getKeys(fqn);
   }

   public static Set getChildrenNames_(TreeCacheManager manager, String fqn) throws Exception
   {
      return manager.getTreeCache().getChildrenNames(fqn);
   }

   /**
    * Get value from node with given key
    * @param manager
    * @param fqn
    * @param key
    * @return
    * @throws Exception
    */
   public static Object getValue_(TreeCacheManager manager, String fqn, Object key) throws Exception
   {
      return manager.getTreeCache().get(fqn, key);
   }

   /**
    * Put method
    * @param manager
    * @param fqn
    * @param map
    * @throws Exception
    */
   public static void put_(TreeCacheManager manager, String fqn, Map map) throws Exception
   {
      manager.getTreeCache().put(fqn, map);
   }

   /**
    * Put method
    * @param manager
    * @param fqn
    * @param map
    * @throws Exception
    */
   public static void remove_(TreeCacheManager manager, String fqn) throws Exception
   {
      manager.getTreeCache().remove(fqn);
   }

   public static Map getListeners(TreeCacheManager manager)
   {
      return manager.getTreeCache().getTreeCacheListeners();
   }

   public static void registerClassLoader(TreeCacheManager manager, String region, ClassLoader loader) throws Exception
   {
      manager.getTreeCache().registerClassLoader(region, loader);
   }

   public static void setInActivateOnStartup(TreeCacheManager manager, boolean inactive)
   {
      manager.getTreeCache().setInactiveOnStartup(inactive);
   }

   public static void activateRegion(TreeCacheManager manager, String fqn) throws Exception
   {
      manager.getTreeCache().activateRegion(fqn);
   }

   public static Object putObjectAop(TreeCacheManager manager, String fqn, Object obj) throws Exception
   {
      if (manager.treeCacheAopInstance == null)
         throw new IllegalArgumentException();
      else
      {
         return manager.treeCacheAopInstance.putObject(fqn, obj);
      }
   }

   public static Object getObject_(TreeCacheManager manager, String fqn) throws Exception
   {
      if (manager.treeCacheAopInstance == null)
         throw new IllegalArgumentException();
      else
      {
         return manager.treeCacheAopInstance.getObject(fqn);
      }

   }

   public TreeCache getTreeCache()
   {
      if (treeCache != null)
         return this.treeCache;
      else
         return this.treeCacheAopInstance;
   }

   public ClassLoader getManagerClassLoader()
   {
      return managerClassLoader;
   }

   public void setManagerClassLoader(ClassLoader managerClassLoader)
   {
      this.managerClassLoader = managerClassLoader;
   }

}//end of class