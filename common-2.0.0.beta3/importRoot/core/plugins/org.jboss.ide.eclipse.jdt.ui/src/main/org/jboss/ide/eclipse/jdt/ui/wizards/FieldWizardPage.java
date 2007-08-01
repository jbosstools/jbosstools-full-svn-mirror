/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.wizards;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.ui.JDTUIMessages;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsUtil;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class FieldWizardPage extends ClassFragmentWizardPage
{
   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup accessorStubsButtons;
   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup modifiersButtons;


   /**
    *Constructor for the FieldWizardPage object
    *
    * @param pageName  Description of the Parameter
    */
   public FieldWizardPage(String pageName)
   {
      super(pageName);
      this.setTitle(JDTUIMessages.getString("FieldWizardPage.title"));//$NON-NLS-1$
      this.setDescription(JDTUIMessages.getString("FieldWizardPage.description"));//$NON-NLS-1$
   }


   /**
    * Gets the getterGenerated attribute of the FieldWizardPage object
    *
    * @return   The getterGenerated value
    */
   public boolean isGetterGenerated()
   {
      return accessorStubsButtons.isSelected(0);
   }


   /**
    * Gets the private attribute of the FieldWizardPage object
    *
    * @return   The private value
    */
   public boolean isPrivate()
   {
      return modifiersButtons.isSelected(3);
   }


   /**
    * Gets the protected attribute of the FieldWizardPage object
    *
    * @return   The protected value
    */
   public boolean isProtected()
   {
      return modifiersButtons.isSelected(2);
   }


   /**
    * Gets the public attribute of the FieldWizardPage object
    *
    * @return   The public value
    */
   public boolean isPublic()
   {
      return modifiersButtons.isSelected(0);
   }


   /**
    * Gets the setterGenerated attribute of the FieldWizardPage object
    *
    * @return   The setterGenerated value
    */
   public boolean isSetterGenerated()
   {
      return accessorStubsButtons.isSelected(0);
   }


   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   public void pageChangeControlPressed(DialogField field)
   {
      super.pageChangeControlPressed(field);

      if (field == typeDialogField)
      {
         IType type = chooseType(getFragmentType());
         if (type != null)
         {
            typeDialogField.setText(JavaModelUtil.getFullyQualifiedName(type));
         }
      }
   }


   /** Description of the Method */
   protected void createContent()
   {
      String[] buttonNames;

      this.nameDialogField = new StringDialogField();
      this.nameDialogField.setDialogFieldListener(this.getFieldsAdapter());
      this.nameDialogField.setLabelText(JDTUIMessages.getString("FieldWizardPage.label.name"));//$NON-NLS-1$

      this.typeDialogField = new StringButtonDialogField(this.getFieldsAdapter());
      this.typeDialogField.setDialogFieldListener(this.getFieldsAdapter());
      this.typeDialogField.setLabelText(JDTUIMessages.getString("FieldWizardPage.label.type"));//$NON-NLS-1$
      this.typeDialogField.setButtonLabel(JDTUIMessages.getString("FieldWizardPage.button.browse"));//$NON-NLS-1$

      buttonNames = new String[]{JDTUIMessages.getString("FieldWizardPage.modifier.public"), JDTUIMessages.getString("FieldWizardPage.modifier.package"), JDTUIMessages.getString("FieldWizardPage.modifier.protected"), JDTUIMessages.getString("FieldWizardPage.modifier.private")};//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      this.modifiersButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO, buttonNames, 4);
      this.modifiersButtons.setLabelText(JDTUIMessages.getString("FieldWizardPage.label.modifiers"));//$NON-NLS-1$

      buttonNames = new String[]{JDTUIMessages.getString("FieldWizardPage.method.getter"), JDTUIMessages.getString("FieldWizardPage.method.setter")};//$NON-NLS-1$ //$NON-NLS-2$
      this.accessorStubsButtons = new SelectionButtonDialogFieldGroup(SWT.CHECK, buttonNames, 2);
      this.accessorStubsButtons.setLabelText(JDTUIMessages.getString("FieldWizardPage.label.accessor"));//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createControl(Composite composite, int nColumns)
   {
      this.createFieldNameControls(composite, nColumns);
      this.createFieldTypeControls(composite, nColumns);
      this.createSeparator(composite, nColumns);
      this.createModifierSelectionControls(composite, nColumns);
      this.createSeparator(composite, nColumns);
      this.createMethodStubSelectionControls(composite, nColumns);
   }


   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createFieldNameControls(Composite composite, int nColumns)
   {
      FieldsUtil.createStringDialogFieldControls(this.nameDialogField, composite, nColumns);
   }


   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createFieldTypeControls(Composite composite, int nColumns)
   {
      FieldsUtil.createStringButtonDialogFieldControls(this.typeDialogField, composite, nColumns);
   }


   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createMethodStubSelectionControls(Composite composite, int nColumns)
   {
      FieldsUtil.createSelectionButtonDialogFieldGroupControls(this.accessorStubsButtons, composite, nColumns);
   }


   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createModifierSelectionControls(Composite composite, int nColumns)
   {
      FieldsUtil.createSelectionButtonDialogFieldGroupControls(this.modifiersButtons, composite, nColumns);
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected IStatus fragmentNameChanged()
   {
      StatusInfo status = new StatusInfo();

      String name = getFragmentName();
      // must not be empty
      if (name.length() == 0)
      {
         status.setError(JDTUIMessages.getString("FieldWizardPage.error.field.name.empty"));//$NON-NLS-1$
         return status;
      }
      IStatus val = JavaConventions.validateFieldName(name);
      if (val.getSeverity() == IStatus.ERROR)
      {
         status.setError(MessageFormat.format(JDTUIMessages.getString("FieldWizardPage.error.field.name.invalid"), new Object[]{val.getMessage()}));//$NON-NLS-1$
         return status;
      }
      else if (val.getSeverity() == IStatus.WARNING)
      {
         status.setWarning(MessageFormat.format(JDTUIMessages.getString("FieldWizardPage.error.field.name.discouraged"), new Object[]{val.getMessage()}));//$NON-NLS-1$
      }

      return status;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected IStatus fragmentTypeChanged()
   {
      StatusInfo status = new StatusInfo();
      IPackageFragmentRoot root = this.getPackageFragmentRoot();

      String ftype = this.getFragmentType();
      // must not be empty
      if (ftype.length() == 0)
      {
         status.setError(JDTUIMessages.getString("FieldWizardPage.error.field.type.empty"));//$NON-NLS-1$
         return status;
      }
      IStatus val = JavaConventions.validateJavaTypeName(ftype);
      if (val.getSeverity() == IStatus.ERROR)
      {
         status.setError(JDTUIMessages.getString("FieldWizardPage.error.field.type.invalid"));//$NON-NLS-1$
         return status;
      }
      if (root != null)
      {
         try
         {
            IType type = resolveTypeName(root.getJavaProject(), ftype);
            if (type == null)
            {
               status.setWarning(JDTUIMessages.getString("FieldWizardPage.error.field.type.inexistant"));//$NON-NLS-1$
               return status;
            }
            else
            {
            }
         }
         catch (JavaModelException e)
         {
            status.setError(JDTUIMessages.getString("FieldWizardPage.error.field.type.invalid"));//$NON-NLS-1$
            AbstractPlugin.logError("Error while checking field type", e);//$NON-NLS-1$
         }
      }
      else
      {
         status.setError("");//$NON-NLS-1$
      }
      return status;
   }


   /** Description of the Method */
   protected void initContent()
   {
      // Protected access
      this.modifiersButtons.setSelection(2, true);

      // Getter and setter generated
      this.accessorStubsButtons.setSelection(0, true);
      this.accessorStubsButtons.setSelection(1, true);
   }
}
