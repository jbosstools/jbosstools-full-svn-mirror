/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.actions;

import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheInstance;
import org.jboss.ide.eclipse.jbosscache.model.internal.TreeCacheManager;
import org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView;

/**
 * Delete node from the cache
 * @author Owner
 *
 */
public class DeleteNodeAction extends AbstractCacheAction
{

   /**
    * Constructor
    * @param view
    * @param id
    */
   public DeleteNodeAction(TreeCacheView view, String id)
   {
      super(view, id);
   }

   /**
    * Real action
    */
   public void run()
   {
      ICacheInstance cacheInstance = (ICacheInstance) (getTreeViewer().getSelection());
      TreeCacheManager manager = cacheInstance.getRootInstance().getTreeCacheManager();
      try
      {
         TreeCacheManager.remove_(manager, cacheInstance.getFqnName());
      }
      catch (Exception e)
      {
         //TODO Exception
      }
   }
}//end of class