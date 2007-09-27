/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.projects;

import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.ui.wizards.ProjectWizard;
import org.jboss.ide.eclipse.jdt.ui.wizards.ProjectWizardPage;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class J2EE14ProjectCreationWizard extends ProjectWizard
{
   /**Constructor for the J2EE14ProjectCreationWizard object */
   public J2EE14ProjectCreationWizard()
   {
      this.setWindowTitle(JDTJ2EEUIMessages.getString("J2EE14ProjectCreationWizard.window.title"));//$NON-NLS-1$
   }


   /** Adds a feature to the Pages attribute of the J2EE13ProjectCreationWizard object */
   public void addPages()
   {
      super.addPages();

      this.fMainPage.setTitle(JDTJ2EEUIMessages.getString("J2EE14ProjectCreationWizardPage.title"));//$NON-NLS-1$
      this.fMainPage.setDescription(JDTJ2EEUIMessages.getString("J2EE14ProjectCreationWizardPage.description"));//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param mainPage  Description of the Parameter
    * @return          Description of the Return Value
    */
   protected ProjectWizardPage createProjectWizardPage(WizardNewProjectCreationPage mainPage)
   {
      return new J2EE14ProjectCreationWizardPage(mainPage);
   }
}
