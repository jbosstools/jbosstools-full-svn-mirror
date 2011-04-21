/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.JDTJ2EEJSPCorePlugin;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.JDTJ2EEJSPUIMessages;
import org.jboss.ide.eclipse.jdt.ui.wizards.NewFileWizardPage;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewJSPPageWizardPage extends NewFileWizardPage
{
   private final static String PAGE_NAME = NewJSPPageWizardPage.class.getName();


   /**
    *Constructor for the NewJSPPageWizardPage object
    *
    * @param selection  Description of the Parameter
    */
   public NewJSPPageWizardPage(IStructuredSelection selection)
   {
      super(PAGE_NAME, selection);
   }


   /**
    * Gets the plugin attribute of the NewJSPPageWizardPage object
    *
    * @return   The plugin value
    */
   protected AbstractPlugin getPlugin()
   {
      return JDTJ2EEJSPCorePlugin.getDefault();
   }


   /**
    * Gets the resource attribute of the NewJSPPageWizardPage object
    *
    * @return   The resource value
    */
   protected String getResource()
   {
      return "template.jsp";//$NON-NLS-1$
   }


   /**
    * Gets the wizardPageDescription attribute of the NewJSPPageWizardPage object
    *
    * @return   The wizardPageDescription value
    */
   protected String getWizardPageDescription()
   {
      return JDTJ2EEJSPUIMessages.getString("NewJSPPageWizardPage.description");//$NON-NLS-1$
   }


   /**
    * Gets the wizardPageTitle attribute of the NewJSPPageWizardPage object
    *
    * @return   The wizardPageTitle value
    */
   protected String getWizardPageTitle()
   {
      return JDTJ2EEJSPUIMessages.getString("NewJSPPageWizardPage.title");//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param filename  Description of the Parameter
    * @return          Description of the Return Value
    */
   protected boolean validateFileName(String filename)
   {
      if (!filename.endsWith(".jsp") && !filename.endsWith(".jspx") && !filename.endsWith(".jspf")//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      )
      {
         this.setMessage(JDTJ2EEJSPUIMessages.getString("NewJSPPageWizardPage.message.extension"));//$NON-NLS-1$
      }
      return true;
   }
}
