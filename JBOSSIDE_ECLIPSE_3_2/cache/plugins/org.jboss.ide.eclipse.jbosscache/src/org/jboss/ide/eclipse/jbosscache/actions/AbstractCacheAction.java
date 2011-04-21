/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.actions;

import org.eclipse.jface.action.Action;
import org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView;

/**
 * This is the abstract class for actions in the cache
 */
public abstract class AbstractCacheAction extends Action
{

   /**Related TreeCacheView part*/
   private TreeCacheView treeView;

   /**
    * Constructor
    */
   protected AbstractCacheAction(TreeCacheView treeView, String actionId)
   {
      super();
      this.treeView = treeView;
      setId(actionId);
   }

   /**
    * Returns the task list viewer.
    */
   protected TreeCacheView getTreeViewer()
   {
      return treeView;
   }

}
