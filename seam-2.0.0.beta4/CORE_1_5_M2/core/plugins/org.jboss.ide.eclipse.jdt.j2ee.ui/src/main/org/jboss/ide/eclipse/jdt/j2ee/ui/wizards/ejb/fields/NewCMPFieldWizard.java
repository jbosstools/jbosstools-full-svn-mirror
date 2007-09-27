/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.fields;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.jdom.IDOMField;
import org.eclipse.jdt.core.jdom.IDOMMethod;
import org.jboss.ide.eclipse.jdt.core.util.ClassNamingUtil;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.Templates;
import org.jboss.ide.eclipse.jdt.ui.wizards.FieldWizard;
import org.jboss.ide.eclipse.jdt.ui.wizards.FieldWizardPage;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewCMPFieldWizard extends FieldWizard
{
   /**Constructor for the NewCMPFieldWizard object */
   public NewCMPFieldWizard()
   {
      this.setWindowTitle(JDTJ2EEUIMessages.getString("NewCMPFieldWizard.window.title"));//$NON-NLS-1$
   }


   /**
    * Adds a feature to the Content attribute of the NewCMPFieldWizard object
    *
    * @exception Exception  Description of the Exception
    */
   protected void addContent()
      throws Exception
   {
      String content;
      String comment;
      String methodName;
      String pkTag = "";//$NON-NLS-1$
      IDOMMethod method;

      Templates manager = new Templates();
      NewCMPFieldWizardPage realPage = (NewCMPFieldWizardPage) page;

      String name = page.getFragmentName();
      String type = page.getFragmentType();
      String view = realPage.getViewType();

      if (realPage.isPrimaryKey())
      {
         pkTag = manager.getString("wizards.ejb.field.pk.tag");//$NON-NLS-1$
      }

      if (realPage.isCMP2x())
      {
         // Getter
         methodName = ClassNamingUtil.getterName(name);
         content = manager.getString("wizards.ejb.field.cmp2x.getter.body", new Object[]{type, methodName});//$NON-NLS-1$
         method = this.getDOMFactory().createMethod(content);
         comment = manager.getString("wizards.ejb.field.cmp2x.getter.comment", new Object[]{name, pkTag, view});//$NON-NLS-1$
         method.setComment(comment);
         this.getType().addChild(method);

         //	Setter
         methodName = ClassNamingUtil.setterName(name);
         content = manager.getString("wizards.ejb.field.cmp2x.setter.body", new Object[]{type, methodName});//$NON-NLS-1$
         method = this.getDOMFactory().createMethod(content);
         comment = manager.getString("wizards.ejb.field.cmp2x.setter.comment", new Object[]{name, view});//$NON-NLS-1$
         method.setComment(comment);
         this.getType().addChild(method);

         // Make sure the class is abstract
         this.getType().setFlags(this.getType().getFlags() | Flags.AccAbstract);
      }
      else
      {
         IDOMField field = this.getDOMFactory().createField();

         field.setName(name);
         field.setType(type);
         field.setFlags(Flags.AccPublic);

         comment = manager.getString("wizards.ejb.cmp11.field.comment", new Object[]{name});//$NON-NLS-1$
         field.setComment(comment);
         this.getType().addChild(field);

         // Getter
         methodName = ClassNamingUtil.getterName(name);
         content = manager.getString("wizards.ejb.cmp11.field.getter.body", new Object[]{type, methodName, name});//$NON-NLS-1$
         method = this.getDOMFactory().createMethod(content);
         comment = manager.getString("wizards.ejb.cmp11.field.getter.comment", new Object[]{name, pkTag});//$NON-NLS-1$
         method.setComment(comment);
         this.getType().addChild(method);

         //	Setter
         methodName = ClassNamingUtil.setterName(name);
         content = manager.getString("wizards.ejb.cmp11.field.setter.body", new Object[]{type, methodName, name});//$NON-NLS-1$
         method = this.getDOMFactory().createMethod(content);
         comment = manager.getString("wizards.ejb.cmp11.field.setter.comment", new Object[]{name});//$NON-NLS-1$
         method.setComment(comment);
         this.getType().addChild(method);
      }
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected FieldWizardPage createFieldWizardPage()
   {
      return new NewCMPFieldWizardPage();
   }
}
