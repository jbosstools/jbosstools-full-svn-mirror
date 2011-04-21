/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.wizards;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.jboss.ide.eclipse.jdt.ui.JDTUIMessages;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class FieldWizard extends ClassFragmentWizard
{
   /** Description of the Field */
   protected FieldWizardPage page;


   /**Constructor for the FieldWizard object */
   public FieldWizard()
   {
      // Hum, hum, missing banner for field. Replaced with new method one.
      this.setDefaultPageImageDescriptor(JavaPluginImages.DESC_WIZBAN_NEWMETH);
      this.setWindowTitle(JDTUIMessages.getString("FieldWizard.window.title"));//$NON-NLS-1$
   }


   /** Adds a feature to the Pages attribute of the FieldWizard object */
   public void addPages()
   {
      super.addPages();

      IWorkspace workspace = JavaPlugin.getWorkspace();
      this.page = this.createFieldWizardPage();
      this.addPage(this.page);
      this.page.init(this.getSelection());
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected abstract FieldWizardPage createFieldWizardPage();
}
