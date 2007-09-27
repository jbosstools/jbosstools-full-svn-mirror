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
public class NewConnectorXml10PageWizard extends NewFileWizard
{
   /**Constructor for the NewConnectorXml10PageWizard object */
   public NewConnectorXml10PageWizard() { }


   /**
    * Description of the Method
    *
    * @param selection  Description of the Parameter
    * @return           Description of the Return Value
    */
   protected NewFileWizardPage createNewFileWizardPage(IStructuredSelection selection)
   {
      return new NewConnectorXml10PageWizardPage(selection);
   }


   /**
    * Gets the wizardTitle attribute of the NewConnectorXml10PageWizard object
    *
    * @return   The wizardTitle value
    */
   protected String getWizardTitle()
   {
      return JDTJ2EEXMLUIMessages.getString("NewConnectorXml10PageWizard.window.title");//$NON-NLS-1$
   }
}
