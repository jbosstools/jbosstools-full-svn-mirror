/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb;

import java.util.Arrays;

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
import org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.*;
import org.jboss.ide.eclipse.jdt.ui.wizards.DOMClassWizardPage;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsUtil;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewSessionEJBWizardPage extends DOMClassWizardPage
{
   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup accessButtons;

   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup typeButtons;

   private final static String PAGE_NAME = NewSessionEJBWizardPage.class.getName();

   /**
    *Constructor for the NewSessionEJBWizardPage object
    *
    * @param root  Description of the Parameter
    */
   public NewSessionEJBWizardPage(IWorkspaceRoot root)
   {
      super(true, PAGE_NAME);
      this.setTitle(JDTJ2EEUIMessages.getString("NewSessionEJBWizardPage.title"));//$NON-NLS-1$
      this.setDescription(JDTJ2EEUIMessages.getString("NewSessionEJBWizardPage.description"));//$NON-NLS-1$
   }

   /**
    * Adds a feature to the Content attribute of the NewSessionEJBWizardPage object
    *
    * @param type      The feature to be added to the Content attribute
    * @param compUnit  The feature to be added to the Content attribute
    * @param dType     The feature to be added to the Content attribute
    * @param monitor   The feature to be added to the Content attribute
    */
   protected void addContent(IType type, IDOMCompilationUnit compUnit, IDOMType dType, IProgressMonitor monitor)
   {
      Templates manager = new Templates();
      String beanName = J2EENamingUtil.stripEJBSuffix(type.getElementName());

      String ejbType;
      if (isStateful())
      {
         ejbType = "Stateful";//$NON-NLS-1$
      }
      else
      {
         ejbType = "Stateless";//$NON-NLS-1$
      }

      // Add the custom comment on top of the Servlet
      String comment = manager.getString(
            "wizards.ejb.session.class.comment", new Object[]{beanName, ejbType, this.getAccessString()});//$NON-NLS-1$
      dType.setComment(comment);

      // ejbCreate method
      if (fMethodStubsButtons.isSelected(2))
      {
         if (isStateful())
         {
            IDOMMethod method = buildMethod(manager, "wizards.ejb.session.method.ejbCreate.parameters");//$NON-NLS-1$
            dType.addChild(method);
         }
         else
         {
            IDOMMethod method = buildMethod(manager, "wizards.ejb.session.method.ejbCreate");//$NON-NLS-1$
            dType.addChild(method);
         }
         addImport(compUnit, "javax.ejb.CreateException");//$NON-NLS-1$
      }
   }

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createAccessSelectionControls(Composite composite, int nColumns)
   {
      FieldsUtil.createSelectionButtonDialogFieldGroupControls(this.accessButtons, composite, nColumns);
   }

   /** Description of the Method */
   protected void createContent()
   {
      super.createContent();

      String[] buttonNames;

      // Constructors and inherited must be left as first elements
      buttonNames = new String[]
      {
            JDTJ2EEUIMessages.getString("NewSessionEJBWizardPage.access.remote"), JDTJ2EEUIMessages.getString("NewSessionEJBWizardPage.access.local"), JDTJ2EEUIMessages.getString("NewSessionEJBWizardPage.access.both")};//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

      accessButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO, buttonNames, 3);
      accessButtons.setLabelText(JDTJ2EEUIMessages.getString("NewSessionEJBWizardPage.label.access"));//$NON-NLS-1$

      // Constructors and inherited must be left as first elements
      buttonNames = new String[]
      {NewWizardMessages.NewClassWizardPage_methods_constructors, //$NON-NLS-1$
            NewWizardMessages.NewClassWizardPage_methods_inherited, //$NON-NLS-1$
            JDTJ2EEUIMessages.getString("NewSessionEJBWizardPage.stub.ejbCreate")};//$NON-NLS-1$

      fMethodStubsButtons = new SelectionButtonDialogFieldGroup(SWT.CHECK, buttonNames, 2);
      fMethodStubsButtons.setLabelText(NewWizardMessages.NewClassWizardPage_methods_label);//$NON-NLS-1$

      // Constructors and inherited must be left as first elements
      buttonNames = new String[]
      {
            JDTJ2EEUIMessages.getString("NewSessionEJBWizardPage.type.stateless"), JDTJ2EEUIMessages.getString("NewSessionEJBWizardPage.type.stateful")};//$NON-NLS-1$ //$NON-NLS-2$

      typeButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO, buttonNames, 2);
      typeButtons.setLabelText(JDTJ2EEUIMessages.getString("NewSessionEJBWizardPage.label.type"));//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createControls(Composite composite, int nColumns)
   {
      createContainerControls(composite, nColumns);
      createPackageControls(composite, nColumns);
      createSeparator(composite, nColumns);
      createTypeNameControls(composite, nColumns);
      createSuperClassControls(composite, nColumns);
      createSuperInterfacesControls(composite, nColumns);

      createSeparator(composite, nColumns);
      createTypeSelectionControls(composite, nColumns);
      createSeparator(composite, nColumns);
      createAccessSelectionControls(composite, nColumns);

      createSeparator(composite, nColumns);
      createMethodStubSelectionControls(composite, nColumns);

      setSuperInterfaces(Arrays.asList(new String[]
      {"javax.ejb.SessionBean"}), //$NON-NLS-1$
            true);
   }

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createMethodStubSelectionControls(Composite composite, int nColumns)
   {
      FieldsUtil.createSelectionButtonDialogFieldGroupControls(this.fMethodStubsButtons, composite, nColumns);
   }

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createTypeSelectionControls(Composite composite, int nColumns)
   {
      FieldsUtil.createSelectionButtonDialogFieldGroupControls(this.typeButtons, composite, nColumns);
   }

   /**
    * Gets the accessString attribute of the NewSessionEJBWizardPage object
    *
    * @return   The accessString value
    */
   protected String getAccessString()
   {
      String access = null;

      if (accessButtons.isSelected(0))
      {
         access = "remote";//$NON-NLS-1$
      }
      if (accessButtons.isSelected(1))
      {
         access = "local";//$NON-NLS-1$
      }
      if (accessButtons.isSelected(2))
      {
         access = "both";//$NON-NLS-1$
      }

      return access;
   }

   /** Description of the Method */
   protected void initContent()
   {
      super.initContent();

      // Constructor is checked
      fMethodStubsButtons.setSelection(0, true);
      // Inherited is checked
      fMethodStubsButtons.setSelection(1, true);
      // ejbCreate is unchecked
      fMethodStubsButtons.setSelection(2, false);

      // Stateless by default
      typeButtons.setSelection(0, true);

      // Remote access by default
      accessButtons.setSelection(0, true);
   }

   /**
    * Gets the stateful attribute of the NewSessionEJBWizardPage object
    *
    * @return   The stateful value
    */
   protected boolean isStateful()
   {
      return typeButtons.isSelected(1);
   }
}
