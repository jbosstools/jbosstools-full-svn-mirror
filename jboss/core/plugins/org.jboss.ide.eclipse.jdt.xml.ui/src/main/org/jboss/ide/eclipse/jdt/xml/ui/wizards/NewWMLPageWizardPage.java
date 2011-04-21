/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.ui.wizards.NewFileWizardPage;
import org.jboss.ide.eclipse.jdt.xml.core.JDTXMLCorePlugin;
import org.jboss.ide.eclipse.jdt.xml.ui.JDTXMLUIMessages;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewWMLPageWizardPage extends NewFileWizardPage
{
   private final static String PAGE_NAME = NewWMLPageWizardPage.class.getName();


   /**
    *Constructor for the NewWMLPageWizardPage object
    *
    * @param selection  Description of the Parameter
    */
   public NewWMLPageWizardPage(IStructuredSelection selection)
   {
      super(PAGE_NAME, selection);
   }


   /**
    * Gets the plugin attribute of the NewWMLPageWizardPage object
    *
    * @return   The plugin value
    */
   protected AbstractPlugin getPlugin()
   {
      return JDTXMLCorePlugin.getDefault();
   }


   /**
    * Gets the resource attribute of the NewWMLPageWizardPage object
    *
    * @return   The resource value
    */
   protected String getResource()
   {
      return "template.wml";//$NON-NLS-1$
   }


   /**
    * Gets the wizardPageDescription attribute of the NewWMLPageWizardPage object
    *
    * @return   The wizardPageDescription value
    */
   protected String getWizardPageDescription()
   {
      return JDTXMLUIMessages.getString("NewWMLPageWizardPage.description");//$NON-NLS-1$
   }


   /**
    * Gets the wizardPageTitle attribute of the NewWMLPageWizardPage object
    *
    * @return   The wizardPageTitle value
    */
   protected String getWizardPageTitle()
   {
      return JDTXMLUIMessages.getString("NewWMLPageWizardPage.title");//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param filename  Description of the Parameter
    * @return          Description of the Return Value
    */
   protected boolean validateFileName(String filename)
   {
      if (!filename.endsWith(".wml")//$NON-NLS-1$
      )
      {
         this.setMessage(JDTXMLUIMessages.getString("NewWMLPageWizardPage.message.extension"));//$NON-NLS-1$
      }
      return true;
   }
}
