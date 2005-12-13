/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.actions;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.internal.TreeCacheManager;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;
import org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView;

/**
 * This class represents action that is related with cache configuration remove
 * @author Owner
 */
public class DeleteConfigurationAction extends AbstractCacheAction
{

   public DeleteConfigurationAction()
   {
      this(null, null);
   }

   public DeleteConfigurationAction(TreeCacheView view, String actionId)
   {
      super(view, actionId);
   }

   /**
    * Delete configuration action
    */
   public void run()
   {
      MessageDialog msgDialog = new MessageDialog(getTreeViewer().getShell(), CacheUtil
            .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_DELETE_CONFIGURATION_DIALOG_TITLE), null, CacheUtil
            .getResourceBundleValue(ICacheConstants.TREECACHEVIEW_DELETE_CONFIGURATION_DIALOG_MESSAGE),
            MessageDialog.QUESTION, new String[]
            {IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL}, 1);

      int delete = msgDialog.open();

      if (delete == IDialogConstants.OK_ID)
      {
         ICacheRootInstance rootInstance = (ICacheRootInstance) getTreeViewer().getSelection();
         if (rootInstance.isConnected())
         {
            TreeCacheManager manager = rootInstance.getTreeCacheManager();
            TreeCacheManager.stopService_(manager);
            TreeCacheManager.destroyService_(manager);
         }
         rootInstance.deleteRoot();
      }
   }

}
