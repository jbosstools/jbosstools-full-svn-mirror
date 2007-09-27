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
import org.eclipse.jdt.core.jdom.IDOMMethod;
import org.eclipse.jdt.core.jdom.IDOMType;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
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
public class NewServletWizardPage extends DOMClassWizardPage
{
   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup serviceMethodStubsButtons;

   private final static String PAGE_NAME = NewServletWizardPage.class.getName();
   private final static String[] SERVICE_METHODS = new String[]{"doGet", "doPost", "doPut", "doDelete", "doOptions", "doHead", "doTrace"};//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$


   /**
    *Constructor for the NewServletWizardPage object
    *
    * @param root  Description of the Parameter
    */
   public NewServletWizardPage(IWorkspaceRoot root)
   {
      super(true, PAGE_NAME);
      this.setTitle(JDTJ2EEUIMessages.getString("NewServletWizardPage.title"));//$NON-NLS-1$
      this.setDescription(JDTJ2EEUIMessages.getString("NewServletWizardPage.description"));//$NON-NLS-1$
   }


   /**
    * Adds a feature to the Content attribute of the NewServletWizardPage object
    *
    * @param type      The feature to be added to the Content attribute
    * @param compUnit  The feature to be added to the Content attribute
    * @param dType     The feature to be added to the Content attribute
    * @param monitor   The feature to be added to the Content attribute
    */
   protected void addContent(IType type, IDOMCompilationUnit compUnit, IDOMType dType, IProgressMonitor monitor)
   {
      Templates manager = new Templates();
      String servletName = J2EENamingUtil.stripServletSuffix(type.getElementName());

      // Add the custom comment on top of the Servlet
      String comment = manager.getString("wizards.web.servlet.class.comment", new Object[]{servletName});//$NON-NLS-1$
      dType.setComment(comment);

      // Init method
      if (fMethodStubsButtons.isSelected(2))
      {
         IDOMMethod method = this.buildMethod(manager, "wizards.web.servlet.method.init");//$NON-NLS-1$
         dType.addChild(method);
         this.addImport(compUnit, "javax.servlet.ServletException");//$NON-NLS-1$
         this.addImport(compUnit, "javax.servlet.ServletConfig");//$NON-NLS-1$
      }

      // Service method
      if (fMethodStubsButtons.isSelected(3))
      {
         IDOMMethod method = this.buildMethod(manager, "wizards.web.servlet.method.service");//$NON-NLS-1$
         dType.addChild(method);
         this.addImport(compUnit, "java.io.IOException");//$NON-NLS-1$
         this.addImport(compUnit, "javax.servlet.ServletException");//$NON-NLS-1$
         this.addImport(compUnit, "javax.servlet.http.HttpServletRequest");//$NON-NLS-1$
         this.addImport(compUnit, "javax.servlet.http.HttpServletResponse");//$NON-NLS-1$
      }

      // Destroy method
      if (fMethodStubsButtons.isSelected(4))
      {
         IDOMMethod method = this.buildMethod(manager, "wizards.web.servlet.method.destroy");//$NON-NLS-1$
         dType.addChild(method);
      }

      // GetServletInfo method
      if (fMethodStubsButtons.isSelected(5))
      {
         IDOMMethod method = this.buildMethod(manager, "wizards.web.servlet.method.getServletInfo");//$NON-NLS-1$
         dType.addChild(method);
      }

      // Service methods
      for (int i = 0; i < SERVICE_METHODS.length; i++)
      {
         String name = SERVICE_METHODS[i];
         if (this.serviceMethodStubsButtons.isSelected(i))
         {
            IDOMMethod method = this.buildMethod(manager, "wizards.web.servlet.method." + name);//$NON-NLS-1$
            dType.addChild(method);
            this.addImport(compUnit, "java.io.IOException");//$NON-NLS-1$
            this.addImport(compUnit, "javax.servlet.ServletException");//$NON-NLS-1$
            this.addImport(compUnit, "javax.servlet.http.HttpServletRequest");//$NON-NLS-1$
            this.addImport(compUnit, "javax.servlet.http.HttpServletResponse");//$NON-NLS-1$
         }
      }
   }


   /** Description of the Method */
   protected void createContent()
   {
      // Constructors and inherited must be left as first elements
      String[] buttonNames = new String[]{NewWizardMessages.NewClassWizardPage_methods_constructors, //$NON-NLS-1$
      NewWizardMessages.NewClassWizardPage_methods_inherited, //$NON-NLS-1$
      JDTJ2EEUIMessages.getString("NewServletWizardPage.method.init"), JDTJ2EEUIMessages.getString("NewServletWizardPage.method.service"), JDTJ2EEUIMessages.getString("NewServletWizardPage.method.destroy"), JDTJ2EEUIMessages.getString("NewServletWizardPage.method.getServletInfo")};//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

      this.fMethodStubsButtons = new SelectionButtonDialogFieldGroup(SWT.CHECK, buttonNames, 2);
      this.fMethodStubsButtons.setLabelText(NewWizardMessages.NewClassWizardPage_methods_label);//$NON-NLS-1$

      // Constructors and inherited must be left as first elements
      buttonNames = new String[SERVICE_METHODS.length];
      for (int i = 0; i < SERVICE_METHODS.length; i++)
      {
         buttonNames[i] = SERVICE_METHODS[i] + JDTJ2EEUIMessages.getString("NewServletWizardPage.method.generic.extension");//$NON-NLS-1$
      }

      this.serviceMethodStubsButtons = new SelectionButtonDialogFieldGroup(SWT.CHECK, buttonNames, 2);
      this.serviceMethodStubsButtons.setLabelText(JDTJ2EEUIMessages.getString("NewServletWizardPage.label.service.method"));//$NON-NLS-1$
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
      this.createServiceMethodStubSelectionControls(composite, nColumns);
      this.setSuperClass("javax.servlet.http.HttpServlet", true);//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createServiceMethodStubSelectionControls(Composite composite, int nColumns)
   {
      FieldsUtil.createSelectionButtonDialogFieldGroupControls(this.serviceMethodStubsButtons, composite, nColumns);
   }


   /** Description of the Method */
   protected void initContent()
   {
      // Constructor is checked
      this.fMethodStubsButtons.setSelection(0, true);
      // Inherited is checked
      this.fMethodStubsButtons.setSelection(1, true);
      // init is unchecked
      this.fMethodStubsButtons.setSelection(2, false);
   }
}
