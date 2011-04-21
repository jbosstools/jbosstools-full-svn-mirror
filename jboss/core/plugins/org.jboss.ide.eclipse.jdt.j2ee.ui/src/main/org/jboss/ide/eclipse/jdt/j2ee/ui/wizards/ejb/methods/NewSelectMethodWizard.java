/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.methods;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.jdom.IDOMMethod;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.Templates;
import org.jboss.ide.eclipse.jdt.ui.wizards.MethodWizardPage;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewSelectMethodWizard extends EJBMethodWizard
{
   /**Constructor for the NewBusinessMethodWizard object */
   public NewSelectMethodWizard()
   {
      this.setWindowTitle(JDTJ2EEUIMessages.getString("NewSelectMethodWizard.window.title"));//$NON-NLS-1$
   }


   /**
    * Adds a feature to the Content attribute of the NewBusinessMethodWizard object
    *
    * @exception Exception  Description of the Exception
    */
   protected void addContent()
      throws Exception
   {
      Templates manager = new Templates();
      NewSelectMethodWizardPage realPage = (NewSelectMethodWizardPage) page;
      IDOMMethod method = this.buildMethod();
      method.setFlags(method.getFlags() | Flags.AccAbstract);

      String viewType = realPage.getViewType();
      String query = realPage.getSelectQuery();

      String comment = manager.getString("wizards.ejb.method.select.comment", new Object[]{query});//$NON-NLS-1$
      method.setComment(comment);

      method.setBody(null);

      this.getType().addChild(method);
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected MethodWizardPage createMethodWizardPage()
   {
      return new NewSelectMethodWizardPage();
   }
}
