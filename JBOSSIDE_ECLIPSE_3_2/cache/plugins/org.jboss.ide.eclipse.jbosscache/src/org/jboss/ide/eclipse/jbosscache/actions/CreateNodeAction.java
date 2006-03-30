/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.actions;

import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.dialogs.AddNewNodeDialog;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.internal.TreeCacheManager;
import org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView;

/**
 * Creates new node in the cache
 * @author Owner
 *
 */
public class CreateNodeAction extends AbstractCacheAction
{

   /**
    * Constructor
    * @param view
    * @param id
    */
   public CreateNodeAction(TreeCacheView view, String id)
   {
      super(view, id);
   }

   /**
    * Real action
    */
   public void run()
   {
      Object selection = getTreeViewer().getSelection();
      TreeCacheManager manager = null;
      AddNewNodeDialog addNewDialog = null;
      ICacheRootInstance rootInstance = null;
      ICacheInstance cacheInstance = null;

      /*New node as tree cache root node below instance root*/
      if (selection instanceof ICacheRootInstance)
      {
         rootInstance = (ICacheRootInstance) selection;
         manager = rootInstance.getTreeCacheManager();
         addNewDialog = new AddNewNodeDialog(getTreeViewer().getShell(), getTreeViewer());
         addNewDialog.setParentFqn(ICacheConstants.SEPERATOR);
         addNewDialog.open();

      }/*New node as tree cache node below any other tree cache node*/
      else if (selection instanceof ICacheInstance)
      {
         cacheInstance = (ICacheInstance) selection;
         manager = cacheInstance.getRootInstance().getTreeCacheManager();
         addNewDialog = new AddNewNodeDialog(getTreeViewer().getShell(), getTreeViewer());
         addNewDialog.setParentFqn(cacheInstance.getFqnName());
         addNewDialog.open();
      }/*not known object*/
      else
      {
         return;
      }

   }

}//end of class
