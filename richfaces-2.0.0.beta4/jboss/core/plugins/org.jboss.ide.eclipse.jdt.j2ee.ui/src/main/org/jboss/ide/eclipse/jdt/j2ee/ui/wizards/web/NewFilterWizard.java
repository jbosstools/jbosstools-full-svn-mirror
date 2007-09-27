/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.web;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.ui.wizards.ClassWizardPage;
import org.jboss.ide.eclipse.jdt.ui.wizards.DOMClassWizard;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewFilterWizard extends DOMClassWizard
{
   /**Constructor for the NewFilterWizard object */
   public NewFilterWizard()
   {
      this.setWindowTitle(JDTJ2EEUIMessages.getString("NewFilterWizard.window.title"));//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param root  Description of the Parameter
    * @return      Description of the Return Value
    */
   protected ClassWizardPage createClassWizardPage(IWorkspaceRoot root)
   {
      return new NewFilterWizardPage(root);
   }
}
