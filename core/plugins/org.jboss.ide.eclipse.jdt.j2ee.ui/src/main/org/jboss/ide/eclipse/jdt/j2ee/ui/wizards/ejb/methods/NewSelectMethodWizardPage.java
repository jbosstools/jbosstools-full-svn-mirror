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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.dialogs.StatusUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsUtil;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewSelectMethodWizardPage extends EJBMethodWizardPage
{

   /** Description of the Field */
   protected StringDialogField selectQueryField;

   /** Description of the Field */
   protected IStatus selectQueryStatus;

   private final static String PAGE_NAME = NewSelectMethodWizardPage.class.getName();

   /**Constructor for the NewSelectMethodWizardPage object */
   public NewSelectMethodWizardPage()
   {
      super(PAGE_NAME);
      this.setTitle(JDTJ2EEUIMessages.getString("NewSelectMethodWizardPage.title"));//$NON-NLS-1$
      this.setDescription(JDTJ2EEUIMessages.getString("NewSelectMethodWizardPage.description"));//$NON-NLS-1$

      this.selectQueryStatus = new StatusInfo();
   }

   /**
    * Gets the query attribute of the NewSelectMethodWizardPage object
    *
    * @return   The query value
    */
   public String getSelectQuery()
   {
      return this.selectQueryField.getText();
   }

   /**
    * Description of the Method
    *
    * @param selection  Description of the Parameter
    */
   public void init(IStructuredSelection selection)
   {
      super.init(selection);

      this.selectQueryStatus = this.selectQueryChanged();
      this.updateStatus(this.findMostSevereStatus());
   }

   /** Description of the Method */
   protected void createContent()
   {
      super.createContent();

      this.selectQueryField = new StringDialogField();
      this.selectQueryField.setDialogFieldListener(this.getFieldsAdapter());
      this.selectQueryField.setLabelText(JDTJ2EEUIMessages.getString("NewSelectMethodWizardPage.label.query"));//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createControl(Composite composite, int nColumns)
   {
      this.createMethodNameControls(composite, nColumns);
      this.createReturnTypeControls(composite, nColumns);
      this.createSeparator(composite, nColumns);
      this.createParametersControls(composite, nColumns);
      this.createSeparator(composite, nColumns);
      this.createExceptionsControls(composite, nColumns);
      this.createSeparator(composite, nColumns);
      this.createQuerySelectionControls(composite, nColumns);

      this.selectQueryStatus = this.selectQueryChanged();
   }

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createQuerySelectionControls(Composite composite, int nColumns)
   {
      FieldsUtil.createStringDialogFieldControls(this.selectQueryField, composite, nColumns);
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected IStatus findMostSevereStatus()
   {
      return StatusUtil.getMostSevere(new IStatus[]
      {this.nameStatus, this.typeStatus, this.selectQueryStatus});
   }

   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   protected void handleFieldChanged(DialogField field)
   {
      super.handleFieldChanged(field);

      if (field == this.selectQueryField)
      {
         this.selectQueryStatus = this.selectQueryChanged();
      }
   }

   /** Description of the Method */
   protected void initContent()
   {
      super.initContent();

      // Prefix
      this.nameDialogField.setText("ejbSelect");//$NON-NLS-1$

      // Mandatory exception
      this.exceptions.add("javax.ejb.FinderException");//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected IStatus selectQueryChanged()
   {
      StatusInfo status = new StatusInfo();

      String query = this.getSelectQuery();
      // must not be empty
      if (query.length() == 0)
      {
         status.setError(JDTJ2EEUIMessages.getString("NewSelectMethodWizardPage.error.status.empty"));//$NON-NLS-1$
         return status;
      }

      return status;
   }
}
