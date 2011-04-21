/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.web;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.jdom.IDOMCompilationUnit;
import org.eclipse.jdt.core.jdom.IDOMType;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.J2EENamingUtil;
import org.jboss.ide.eclipse.jdt.ui.wizards.DOMClassWizardPage;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsUtil;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewJSPTagHandlerWizardPage extends DOMClassWizardPage
{
   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup bodySupportButtons;

   private final static String PAGE_NAME = NewJSPTagHandlerWizardPage.class.getName();


   /**
    *Constructor for the NewJSPTagHandlerWizardPage object
    *
    * @param root  Description of the Parameter
    */
   public NewJSPTagHandlerWizardPage(IWorkspaceRoot root)
   {
      super(true, PAGE_NAME);
      this.setTitle(JDTJ2EEUIMessages.getString("NewJSPTagHandlerWizardPage.title"));//$NON-NLS-1$
      this.setDescription(JDTJ2EEUIMessages.getString("NewJSPTagHandlerWizardPage.description"));//$NON-NLS-1$
   }


   /**
    * Gets the bodySupport attribute of the NewJSPTagHandlerWizardPage object
    *
    * @return   The bodySupport value
    */
   public String getBodySupport()
   {
      String result = "JSP";//$NON-NLS-1$
      if (this.bodySupportButtons.isSelected(0))
      {
         result = "empty";//$NON-NLS-1$
      }
      return result;
   }


   /**
    * Adds a feature to the Content attribute of the NewFilterWizardPage object
    *
    * @param type      The feature to be added to the Content attribute
    * @param compUnit  The feature to be added to the Content attribute
    * @param dType     The feature to be added to the Content attribute
    * @param monitor   The feature to be added to the Content attribute
    */
   protected void addContent(IType type, IDOMCompilationUnit compUnit, IDOMType dType, IProgressMonitor monitor)
   {
      Templates manager = new Templates();
      String tagName = J2EENamingUtil.stripTagSuffix(type.getElementName());
      String content = this.getBodySupport();

      String comment = manager.getString("wizards.web.taghandler.class.comment", new Object[]{tagName, content});//$NON-NLS-1$
      dType.setComment(comment);
   }


   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createBodySupportSelectionControls(Composite composite, int nColumns)
   {
      FieldsUtil.createSelectionButtonDialogFieldGroupControls(this.bodySupportButtons, composite, nColumns);
   }


   /** Description of the Method */
   protected void createContent()
   {
      super.createContent();

      String[] buttonNames = new String[]{JDTJ2EEUIMessages.getString("NewJSPTagHandlerWizardPage.label.body.without"), JDTJ2EEUIMessages.getString("NewJSPTagHandlerWizardPage.label.body.with")};//$NON-NLS-1$ //$NON-NLS-2$
      this.bodySupportButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO, buttonNames, 1);
      this.bodySupportButtons.setDialogFieldListener(this.getFieldsAdapter());
      this.bodySupportButtons.setLabelText(JDTJ2EEUIMessages.getString("NewJSPTagHandlerWizardPage.label.body"));//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createControls(Composite composite, int nColumns)
   {
      super.createControls(composite, nColumns);
      this.createSeparator(composite, nColumns);
      this.createBodySupportSelectionControls(composite, nColumns);
      this.setSuperClass("javax.servlet.jsp.tagext.TagSupport", true);//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   protected void handleFieldChanged(DialogField field)
   {
      super.handleFieldChanged(field);

      if (field == this.bodySupportButtons)
      {
         if (this.bodySupportButtons.isSelected(0))
         {
            this.setSuperClass("javax.servlet.jsp.tagext.TagSupport", true);//$NON-NLS-1$
         }
         else
         {
            this.setSuperClass("javax.servlet.jsp.tagext.BodyTagSupport", true);//$NON-NLS-1$
         }
      }
   }


   /** Description of the Method */
   protected void initContent()
   {
      super.initContent();

      // Tag without body support
      this.bodySupportButtons.setSelection(0, true);
   }
}
