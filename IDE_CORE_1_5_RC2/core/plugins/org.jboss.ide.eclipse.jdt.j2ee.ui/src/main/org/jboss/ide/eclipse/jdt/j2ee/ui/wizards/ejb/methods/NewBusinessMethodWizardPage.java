/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.methods;

import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewBusinessMethodWizardPage extends EJBMethodWizardPage
{
   private final static String PAGE_NAME = NewBusinessMethodWizardPage.class.getName();


   /**Constructor for the NewBusinessMethodWizardPage object */
   public NewBusinessMethodWizardPage()
   {
      super(PAGE_NAME);
      this.setTitle(JDTJ2EEUIMessages.getString("NewBusinessMethodWizardPage.title"));//$NON-NLS-1$
      this.setDescription(JDTJ2EEUIMessages.getString("NewBusinessMethodWizardPage.description"));//$NON-NLS-1$
   }
}
