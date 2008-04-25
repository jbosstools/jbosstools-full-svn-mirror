/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.actions;

import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.dialogs.ImportActionDialog;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView;

/**
 * This class represents import action related with cache instances
 * @author Gurkaner
 */
public class ImportAction extends AbstractCacheAction
{

   public ImportAction(TreeCacheView view, String id)
   {
      super(view, id);
      setImageDescriptor(JBossCachePlugin.getDefault().getImageRegistry().getDescriptor(
            ICacheConstants.IMAGE_KEY_IMPORT_WIZ));
      setText(CacheMessages.TreeCacheView_importAction);
      setToolTipText(CacheMessages.TreeCacheView_importAction);
   }

   public void run()
   {
      ImportActionDialog actionDialog = new ImportActionDialog(getTreeViewer().getShell());
      actionDialog.open();
   }
}
