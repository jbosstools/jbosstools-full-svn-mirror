/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.xml.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.jboss.ide.eclipse.jdt.j2ee.xml.ui.JDTJ2EEXMLUIMessages;
import org.jboss.ide.eclipse.jdt.ui.wizards.NewFileWizard;
import org.jboss.ide.eclipse.jdt.ui.wizards.NewFileWizardPage;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewConnectorXml15PageWizard extends NewFileWizard
{
   /**Constructor for the NewConnectorXml15PageWizard object */
   public NewConnectorXml15PageWizard() { }


   /**
    * Description of the Method
    *
    * @param selection  Description of the Parameter
    * @return           Description of the Return Value
    */
   protected NewFileWizardPage createNewFileWizardPage(IStructuredSelection selection)
   {
      return new NewConnectorXml15PageWizardPage(selection);
   }


   /**
    * Gets the wizardTitle attribute of the NewConnectorXml15PageWizard object
    *
    * @return   The wizardTitle value
    */
   protected String getWizardTitle()
   {
      return JDTJ2EEXMLUIMessages.getString("NewConnectorXml15PageWizard.window.title");//$NON-NLS-1$
   }
}
