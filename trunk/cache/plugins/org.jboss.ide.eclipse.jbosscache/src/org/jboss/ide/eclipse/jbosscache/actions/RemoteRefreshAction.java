/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.actions;

import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.dialogs.RefreshDialog;
import org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView;

public class RemoteRefreshAction extends AbstractCacheAction
{
   public RemoteRefreshAction(TreeCacheView view,String id){
      super(view,id);
      setText("Refresh Content");
      setToolTipText("Refresh Content");
      setImageDescriptor(JBossCachePlugin.getDefault().getImageRegistry().getDescriptor(ICacheConstants.IMAGE_KEY_REFRESH_NAV_ACTION));
   }
   
   public void run(){
      RefreshDialog ref = new RefreshDialog(getTreeViewer().getShell(),getTreeViewer());
      ref.open();
   }

}
