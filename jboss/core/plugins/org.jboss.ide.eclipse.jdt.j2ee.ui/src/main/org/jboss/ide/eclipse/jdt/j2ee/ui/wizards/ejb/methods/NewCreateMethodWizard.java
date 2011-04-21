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
public class NewCreateMethodWizard extends EJBMethodWizard
{
   /**Constructor for the NewCreateMethodWizard object */
   public NewCreateMethodWizard()
   {
      this.setWindowTitle(JDTJ2EEUIMessages.getString("NewCreateMethodWizard.window.title"));//$NON-NLS-1$
   }


   /**
    * Adds a feature to the Content attribute of the NewCreateMethodWizard object
    *
    * @exception Exception  Description of the Exception
    */
   protected void addContent()
      throws Exception
   {
      String comment;
      Templates manager = new Templates();
      NewCreateMethodWizardPage realPage = (NewCreateMethodWizardPage) page;
      IDOMMethod method = this.buildMethod();

      String viewType = realPage.getViewType();
      boolean forEntity = realPage.isForEntityBean();

      if (!realPage.isForMessageDrivenBean())
      {
         comment = manager.getString("wizards.ejb.method.create.comment", new Object[]{viewType});//$NON-NLS-1$
         method.setComment(comment);
      }

      StringBuffer content = new StringBuffer();
      content.append("{\n");//$NON-NLS-1$
      content.append(manager.getString("wizards.ejb.method.create.body"));//$NON-NLS-1$
      content.append(this.computeReturnClause());
      content.append("}\n");//$NON-NLS-1$
      method.setBody(content.toString());

      this.getType().addChild(method);

      if (forEntity)
      {
         method = this.buildMethod();
         method.setReturnType("void");//$NON-NLS-1$
         String name = method.getName();
         name = "ejbPostCreate" + name.substring(9);//$NON-NLS-1$
         method.setName(name);

         comment = manager.getString("wizards.ejb.method.postCreate.comment", new Object[]{viewType});//$NON-NLS-1$
         method.setComment(comment);

         content = new StringBuffer();
         content.append("{\n");//$NON-NLS-1$
         content.append(manager.getString("wizards.ejb.method.postCreate.body"));//$NON-NLS-1$
         content.append("}\n");//$NON-NLS-1$
         method.setBody(content.toString());

         this.getType().addChild(method);
      }
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected MethodWizardPage createMethodWizardPage()
   {
      return new NewCreateMethodWizardPage();
   }
}
