/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.xml.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.j2ee.xml.ui.JDTJ2EEXMLUIMessages;
import org.jboss.ide.eclipse.jdt.j2ee.xml.ui.JDTJ2EEXMLUIPlugin;
import org.jboss.ide.eclipse.jdt.ui.wizards.NewFileWizardPage;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewEjbXml21PageWizardPage extends NewFileWizardPage
{
   private final static String PAGE_NAME = NewEjbXml21PageWizardPage.class.getName();


   /**
    *Constructor for the NewEjbXml21PageWizardPage object
    *
    * @param selection  Description of the Parameter
    */
   public NewEjbXml21PageWizardPage(IStructuredSelection selection)
   {
      super(PAGE_NAME, selection);
      this.setFileName("ejb-jar.xml");//$NON-NLS-1$
   }


   /**
    * Gets the plugin attribute of the NewEjbXml21PageWizardPage object
    *
    * @return   The plugin value
    */
   protected AbstractPlugin getPlugin()
   {
      return JDTJ2EEXMLUIPlugin.getDefault();
   }


   /**
    * Gets the resource attribute of the NewEjbXml21PageWizardPage object
    *
    * @return   The resource value
    */
   protected String getResource()
   {
      return "ejb-jar_2.1.xml";//$NON-NLS-1$
   }


   /**
    * Gets the wizardPageDescription attribute of the NewEjbXml21PageWizardPage object
    *
    * @return   The wizardPageDescription value
    */
   protected String getWizardPageDescription()
   {
      return JDTJ2EEXMLUIMessages.getString("NewEjbXml21PageWizardPage.description");//$NON-NLS-1$
   }


   /**
    * Gets the wizardPageTitle attribute of the NewEjbXml21PageWizardPage object
    *
    * @return   The wizardPageTitle value
    */
   protected String getWizardPageTitle()
   {
      return JDTJ2EEXMLUIMessages.getString("NewEjbXml21PageWizardPage.title");//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param filename  Description of the Parameter
    * @return          Description of the Return Value
    */
   protected boolean validateFileName(String filename)
   {
      if (!filename.equals("ejb-jar.xml")//$NON-NLS-1$
      )
      {
         this.setMessage(JDTJ2EEXMLUIMessages.getString("NewEjbXml21PageWizardPage.message.extension"));//$NON-NLS-1$
      }
      return true;
   }
}
