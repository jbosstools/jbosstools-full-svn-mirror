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
package org.jboss.ide.eclipse.jdt.ui.dialogs;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jboss.ide.eclipse.jdt.ui.JDTUIMessages;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsAdapter;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsAdapterListener;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsUtil;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.TypeChooser;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ExceptionEditDialog extends Dialog implements FieldsAdapterListener
{
   private TypeChooser chooser;

   private String type = "";//$NON-NLS-1$

   private StringButtonDialogField typeDialogField;

   /**
    *Constructor for the ExceptionEditDialog object
    *
    * @param parentShell  Description of the Parameter
    * @param chooser      Description of the Parameter
    */
   public ExceptionEditDialog(Shell parentShell, TypeChooser chooser)
   {
      super(parentShell);
      this.chooser = chooser;
   }

   /**
    * Gets the type attribute of the ExceptionEditDialog object
    *
    * @return   The type value
    */
   public String getType()
   {
      return this.type;
   }

   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   public void pageChangeControlPressed(DialogField field)
   {
      IType type = this.chooser.chooseType(this.type);
      this.type = type.getFullyQualifiedName();
      this.typeDialogField.setText(this.type);
   }

   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   public void pageDialogFieldChanged(DialogField field)
   {
      this.type = this.typeDialogField.getText();
   }

   /**
    * Description of the Method
    *
    * @param newShell  Description of the Parameter
    */
   protected void configureShell(Shell newShell)
   {
      super.configureShell(newShell);
      newShell.setText(JDTUIMessages.getString("ExceptionEditDialog.title"));//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    * @return        Description of the Return Value
    */
   protected Control createDialogArea(Composite parent)
   {
      Composite composite = (Composite) super.createDialogArea(parent);

      int nColumns = 4;
      GridLayout layout = new GridLayout();
      layout.numColumns = nColumns;
      composite.setLayout(layout);

      FieldsAdapter adapter = new FieldsAdapter(this);
      this.typeDialogField = new StringButtonDialogField(adapter);
      this.typeDialogField.setDialogFieldListener(adapter);
      this.typeDialogField.setLabelText(JDTUIMessages.getString("ExceptionEditDialog.label.type"));//$NON-NLS-1$
      this.typeDialogField.setButtonLabel(JDTUIMessages.getString("ExceptionEditDialog.button.browse"));//$NON-NLS-1$

      FieldsUtil.createStringButtonDialogFieldControls(this.typeDialogField, composite, nColumns);

      return composite;
   }

   /** Description of the Method */
   protected void okPressed()
   {
      if (this.type != null && !"".equals(this.type)//$NON-NLS-1$
      )
      {

         super.okPressed();
      }
   }
}
