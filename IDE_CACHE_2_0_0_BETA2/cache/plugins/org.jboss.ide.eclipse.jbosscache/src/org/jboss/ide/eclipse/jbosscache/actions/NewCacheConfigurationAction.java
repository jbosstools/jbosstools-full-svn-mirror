/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.part.ViewPart;
import org.jboss.ide.eclipse.jbosscache.wizards.NewCacheConfigurationWizard;

/**
 * This actions pops up the new configuration wizard to configure the cache
 * @author Gurkaner
 *
 */
public class NewCacheConfigurationAction extends Action
{

   /*Which view part that action belongs to*/
   private ViewPart viewPart;

   public NewCacheConfigurationAction()
   {
      super();
   }

   /**
    * Constructor
    * @param viewPart
    * @param name
    */
   public NewCacheConfigurationAction(ViewPart viewPart, String name)
   {
      super(name);
      this.viewPart = viewPart;
   }

   /**
    * Action execute process
    */
   public void run()
   {
      NewCacheConfigurationWizard wizard = new NewCacheConfigurationWizard(viewPart);
      WizardDialog dialog = new WizardDialog(viewPart.getSite().getShell(), wizard);
      dialog.open();
   }//end of method

}