/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.actions;

import org.jboss.ide.eclipse.jbosscache.dialogs.RenameDialog;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView;

/**
 * Action related with rename of the cache instance configuration
 * @author Owner
 *
 */
public class RenameAction extends AbstractCacheAction
{

   TreeCacheView viewPart;

   public RenameAction(TreeCacheView view, String id)
   {
      super(view, id);
      setText(CacheMessages.RenameAction_Action_Name);
      setToolTipText(CacheMessages.RenameAction_Action_Name);
      this.viewPart = view;
   }

   public void run()
   {
      RenameDialog renameDialog = new RenameDialog(viewPart.getShell(), viewPart);
      renameDialog.open();
   }

}
