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
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.methods;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsUtil;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewCreateMethodWizardPage extends EJBMethodWizardPage
{
   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup fEJBTypeButtons;

   private final static String PAGE_NAME = NewCreateMethodWizardPage.class.getName();

   /**Constructor for the NewCreateMethodWizardPage object */
   public NewCreateMethodWizardPage()
   {
      super(PAGE_NAME);
      this.setTitle(JDTJ2EEUIMessages.getString("NewCreateMethodWizardPage.title"));//$NON-NLS-1$
      this.setDescription(JDTJ2EEUIMessages.getString("NewCreateMethodWizardPage.description"));//$NON-NLS-1$
   }

   /**
    * Gets the entityBean attribute of the NewCreateMethodWizardPage object
    *
    * @return   The entityBean value
    */
   public boolean isForEntityBean()
   {
      return this.fEJBTypeButtons.isSelected(0);
   }

   /**
    * Gets the forMessageDrivenBean attribute of the NewCreateMethodWizardPage object
    *
    * @return   The forMessageDrivenBean value
    */
   public boolean isForMessageDrivenBean()
   {
      return this.fEJBTypeButtons.isSelected(2);
   }

   /**
    * Gets the forSessionBean attribute of the NewCreateMethodWizardPage object
    *
    * @return   The forSessionBean value
    */
   public boolean isForSessionBean()
   {
      return this.fEJBTypeButtons.isSelected(1);
   }

   /** Description of the Method */
   protected void createContent()
   {
      super.createContent();

      // Constructors and inherited must be left as first elements
      String[] buttonNames = new String[]
      {
            JDTJ2EEUIMessages.getString("NewCreateMethodWizardPage.bean.entity"), JDTJ2EEUIMessages.getString("NewCreateMethodWizardPage.bean.session"), JDTJ2EEUIMessages.getString("NewCreateMethodWizardPage.bean.mdb")};//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

      fEJBTypeButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO, buttonNames, 3);
      fEJBTypeButtons.setDialogFieldListener(this.getFieldsAdapter());
      fEJBTypeButtons.setLabelText(JDTJ2EEUIMessages.getString("NewCreateMethodWizardPage.label.bean"));//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createControl(Composite composite, int nColumns)
   {
      super.createControl(composite, nColumns);
      this.createSeparator(composite, nColumns);
      this.createEJBTypeSelectionControls(composite, nColumns);
   }

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createEJBTypeSelectionControls(Composite composite, int nColumns)
   {
      FieldsUtil.createSelectionButtonDialogFieldGroupControls(this.fEJBTypeButtons, composite, nColumns);
   }

   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   protected void handleFieldChanged(DialogField field)
   {
      super.handleFieldChanged(field);

      if (field == fEJBTypeButtons)
      {
         // Return type is enabled only for Entity Bean
         this.typeDialogField.setText("");//$NON-NLS-1$
         this.typeDialogField.setEnabled(this.isForEntityBean());

         // View type is not available for MDB
         this.accessButtons.setEnabled(!this.isForMessageDrivenBean());
      }
   }

   /** Description of the Method */
   protected void initContent()
   {
      super.initContent();

      // Method prefix
      this.nameDialogField.setText("ejbCreate");//$NON-NLS-1$

      // Creation for Entity Bean
      this.fEJBTypeButtons.setSelection(0, true);

      // Local by default
      this.accessButtons.setSelection(0, false);
      this.accessButtons.setSelection(1, true);

      this.exceptions.add("javax.ejb.CreateException");//$NON-NLS-1$
   }
}
