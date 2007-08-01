/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class NewFileWizard extends BaseWizard
{
   /** Description of the Field */
   protected NewFileWizardPage page;


   /**Constructor for the NewFilterWizard object */
   public NewFileWizard()
   {
      this.setWindowTitle(this.getWizardTitle());
   }


   /** Adds a feature to the Pages attribute of the NewHTMLPageWizard object */
   public void addPages()
   {
      super.addPages();
      this.page = this.createNewFileWizardPage(this.getSelection());
      this.addPage(this.page);
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean performFinish()
   {
      IFile file = this.page.createNewFile();
      if (file == null)
      {
         return false;
      }

      this.selectAndReveal(file);

      return true;
   }


   /**
    * Description of the Method
    *
    * @param selection  Description of the Parameter
    * @return           Description of the Return Value
    */
   protected abstract NewFileWizardPage createNewFileWizardPage(IStructuredSelection selection);


   /**
    * Gets the wizardTitle attribute of the NewFileWizard object
    *
    * @return   The wizardTitle value
    */
   protected abstract String getWizardTitle();
}
