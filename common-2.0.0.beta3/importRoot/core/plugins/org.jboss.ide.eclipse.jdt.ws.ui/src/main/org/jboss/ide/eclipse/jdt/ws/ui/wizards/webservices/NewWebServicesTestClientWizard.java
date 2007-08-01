/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ws.ui.wizards.webservices;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.jdt.ui.wizards.ClassWizard;
import org.jboss.ide.eclipse.jdt.ui.wizards.ClassWizardPage;
import org.jboss.ide.eclipse.jdt.ws.core.generation.WSDL2JavaGenerationEngine;
import org.jboss.ide.eclipse.jdt.ws.ui.JDTWSUIMessages;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewWebServicesTestClientWizard extends ClassWizard
{
   /**Constructor for the NewWebServicesTestClientWizard object */
   public NewWebServicesTestClientWizard()
   {
      this.setWindowTitle(JDTWSUIMessages.getString("NewWebServicesTestClientWizard.window.title"));//$NON-NLS-1$
   }


   /** Adds a feature to the Pages attribute of the NewWebServicesTestClientWizard object */
   public void addPages()
   {
      super.addPages();

      WSDL2JavaGenerationEngine engine = new WSDL2JavaGenerationEngine();
      this.setEngine(engine);
   }


   /**
    * Description of the Method
    *
    * @param root  Description of the Parameter
    * @return      Description of the Return Value
    */
   protected ClassWizardPage createClassWizardPage(IWorkspaceRoot root)
   {
      return new NewWebServicesTestClientWizardPage(root);
   }


   /**
    * Description of the Method
    *
    * @param monitor                   Description of the Parameter
    * @exception InterruptedException  Description of the Exception
    * @exception CoreException         Description of the Exception
    */
   protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException
   {
      this.page.generate(this.getEngine(), monitor);
   }
}
