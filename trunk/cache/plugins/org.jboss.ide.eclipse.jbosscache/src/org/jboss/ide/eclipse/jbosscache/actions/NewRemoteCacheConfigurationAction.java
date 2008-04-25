/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.actions;

import org.eclipse.jface.wizard.WizardDialog;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.wizards.RemoteConfigurationWizard;


public class NewRemoteCacheConfigurationAction extends AbstractCacheAction
{ 
   /**
    * Constructor
    * @param viewPart
    * @param name
    */
   public NewRemoteCacheConfigurationAction(org.jboss.ide.eclipse.jbosscache.views.config.TreeCacheView viewPart, String id)
   {
      super(viewPart,id);
      setToolTipText("Create New Remote Cache Instance");
      setText("Create New Remote Cache Instance");
      setImageDescriptor(JBossCachePlugin.getDefault().getImageRegistry().getDescriptor(ICacheConstants.IMAGE_KEY_SERVER_NAVIGATOR));
   }

   /**
    * Action execute process
    */
   public void run()
   {
      RemoteConfigurationWizard wizard = new RemoteConfigurationWizard();
      WizardDialog dialog = new WizardDialog(getTreeViewer().getShell(), wizard);
      dialog.open();
   }//end of method
}
