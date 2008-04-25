/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.model.cache;

import org.jboss.cache.Fqn;
import org.jboss.cache.TreeCache;
import org.jboss.cache.TreeCacheListener;
import org.jboss.ide.eclipse.jbosscache.model.internal.ITreeCacheManagerExtendListener;
import org.jboss.ide.eclipse.jbosscache.model.internal.ITreeCacheManagerListener;
import org.jgroups.View;

/**
 * Related with TreeCacheManager Listener
 * Abstract implementation of this listener
 * @author Owner
 */
public abstract class AbstractTreeCacheManagerListener
      implements
         ITreeCacheManagerListener,
         ITreeCacheManagerExtendListener
{

   private ICacheRootInstance rootInstance;

   /**
    * Cosntructor
    * @param cacheManager
    */
   protected AbstractTreeCacheManagerListener(ICacheRootInstance rootInstance)
   {
      this.rootInstance = rootInstance;
      this.rootInstance.setCacheViewNodeListener(this);
   }

   /**
    * Called when a node is created
    * @param fqn
    */
   public void nodeCreated(Fqn fqn)
   {

   }

   /**
    * Called when a node is removed.
    * @param fqn
    */
   public void nodeRemoved(Fqn fqn)
   {

   }

   /**
    * Called when a node is loaded into memory via the CacheLoader. This is not the same
    * as {@link #nodeCreated(Fqn)}.
    */
   public void nodeLoaded(Fqn fqn)
   {

   }

   /**
    * Called when a node is evicted (not the same as remove()).
    * @param fqn
    */
   public void nodeEvicted(Fqn fqn)
   {

   }

   /**
    * Called when a node is modified, e.g., one (key, value) pair
    * in the internal map storage has been modified.
    * @param fqn
    */
   public void nodeModified(Fqn fqn)
   {

   }

   /**
    * Called when a node is visisted, i.e., get().
    * @param fqn
    */
   public void nodeVisited(Fqn fqn)
   {

   }

   /**
    * Called when the cache is started.
    * @param cache
    */
   public void cacheStarted(TreeCache cache)
   {

   }

   /**
    * Called when the cache is stopped.
    * @param cache
    */
   public void cacheStopped(TreeCache cache)
   {

   }

   public void viewChange(View new_view)
   {

   }

   /**
    * @see TreeCacheListener#nodeEvicted(Fqn)
    */
   public void nodeEvict(Fqn fqn, boolean pre)
   {

   }

   /**
    * @see TreeCacheListener#nodeRemoved(Fqn)
    */
   public void nodeRemove(Fqn fqn, boolean pre, boolean isLocal)
   {

   }

   /**
    * @see TreeCacheListener#nodeModified(Fqn)
    */
   public void nodeModify(Fqn fqn, boolean pre, boolean isLocal)
   {

   }

   /**
    */
   public void nodeActivate(Fqn fqn, boolean pre)
   {

   }

   /**
    */
   public void nodePassivate(Fqn fqn, boolean pre)
   {

   }

   public ICacheRootInstance getICacheRootInstance()
   {
      return this.rootInstance;
   }

}//end of class