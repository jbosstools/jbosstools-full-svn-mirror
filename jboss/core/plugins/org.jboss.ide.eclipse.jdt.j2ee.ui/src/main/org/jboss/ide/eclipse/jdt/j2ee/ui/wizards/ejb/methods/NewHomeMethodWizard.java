/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.methods;

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
public class NewHomeMethodWizard extends EJBMethodWizard
{
   /**Constructor for the NewBusinessMethodWizard object */
   public NewHomeMethodWizard()
   {
      this.setWindowTitle(JDTJ2EEUIMessages.getString("NewHomeMethodWizard.window.title"));//$NON-NLS-1$
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
      NewHomeMethodWizardPage realPage = (NewHomeMethodWizardPage) page;
      IDOMMethod method = this.buildMethod();

      String viewType = realPage.getViewType();

      String comment = manager.getString("wizards.ejb.method.home.comment", new Object[]{viewType});//$NON-NLS-1$
      method.setComment(comment);

      StringBuffer content = new StringBuffer();
      content.append("{\n");//$NON-NLS-1$
      content.append(manager.getString("wizards.ejb.method.home.body"));//$NON-NLS-1$
      content.append(this.computeReturnClause());
      content.append("}\n");//$NON-NLS-1$
      method.setBody(content.toString());

      this.getType().addChild(method);
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected MethodWizardPage createMethodWizardPage()
   {
      return new NewHomeMethodWizardPage();
   }
}
