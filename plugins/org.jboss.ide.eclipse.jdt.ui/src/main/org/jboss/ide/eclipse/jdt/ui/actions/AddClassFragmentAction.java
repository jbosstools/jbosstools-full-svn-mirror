/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.actions;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionDelegate;
import org.jboss.ide.eclipse.jdt.ui.wizards.ClassFragmentWizard;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class AddClassFragmentAction extends ActionDelegate implements IObjectActionDelegate
{
   /** Description of the Field */
   protected boolean enable = false;
   /** Description of the Field */
   protected IWorkbenchPart part = null;
   /** Description of the Field */
   protected ISelection selection = null;


   /**Constructor for the AddClassFragmentAction object */
   public AddClassFragmentAction() { }


   /**
    * Gets the selection attribute of the AddClassFragmentAction object
    *
    * @return   The selection value
    */
   public ISelection getSelection()
   {
      return this.selection;
   }


   /**
    * Main processing method for the AddClassFragmentAction object
    *
    * @param action  Description of the Parameter
    */
   public void run(IAction action)
   {
      // Gets the selection provider and extract what we need
      if (this.selection != null)
      {
         StructuredSelection structselection = (StructuredSelection) this.selection;
         if (structselection.size() == 1)
         {
            ClassFragmentWizard wizard = this.createFieldWizard();
            wizard.init(JavaPlugin.getDefault().getWorkbench(), structselection);

            // Create the wizard dialog
            WizardDialog dialog = new WizardDialog(JavaPlugin.getActiveWorkbenchShell(), wizard);
            // Open the wizard dialog
            dialog.open();
         }
      }
   }


   /**
    * Description of the Method
    *
    * @param action     Description of the Parameter
    * @param selection  Description of the Parameter
    */
   public void selectionChanged(IAction action, ISelection selection)
   {
      this.selection = selection;
      this.checkEnabled();
      action.setEnabled(isEnabled());
   }


   /**
    * Sets the activePart attribute of the AddClassFragmentAction object
    *
    * @param action      The new activePart value
    * @param targetPart  The new activePart value
    */
   public void setActivePart(IAction action, IWorkbenchPart targetPart)
   {
      this.part = targetPart;
   }


   /**
    * Sets the enable attribute of the AddClassFragmentAction object
    *
    * @param enable  The new enable value
    */
   public void setEnable(boolean enable)
   {
      this.enable = enable;
   }


   /** Description of the Method */
   protected abstract void checkEnabled();


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected abstract ClassFragmentWizard createFieldWizard();


   /**
    * Gets the enabled attribute of the AddClassFragmentAction object
    *
    * @return   The enabled value
    */
   protected boolean isEnabled()
   {
      return this.enable;
   }
}
