/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class NewConfigurationFileWizard extends Wizard implements INewWizard
{
   private WizardNewFileCreationPage page;
   
   

   public boolean performFinish()
   {
      return false;
   }

   public void init(IWorkbench workbench, IStructuredSelection selection)
   {
      // FIXME init
      
   }

}
