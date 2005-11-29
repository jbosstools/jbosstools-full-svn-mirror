/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.jboss.ide.eclipse.jdt.ui.wizards.NewFileWizard;
import org.jboss.ide.eclipse.jdt.ui.wizards.NewFileWizardPage;
import org.jboss.ide.eclipse.jdt.xml.ui.JDTXMLUIMessages;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewWMLPageWizard extends NewFileWizard
{
   /**Constructor for the NewWMLPageWizard object */
   public NewWMLPageWizard() { }


   /**
    * Description of the Method
    *
    * @param selection  Description of the Parameter
    * @return           Description of the Return Value
    */
   protected NewFileWizardPage createNewFileWizardPage(IStructuredSelection selection)
   {
      return new NewWMLPageWizardPage(selection);
   }


   /**
    * Gets the wizardTitle attribute of the NewWMLPageWizard object
    *
    * @return   The wizardTitle value
    */
   protected String getWizardTitle()
   {
      return JDTXMLUIMessages.getString("NewWMLPageWizard.window.title");//$NON-NLS-1$
   }
}
