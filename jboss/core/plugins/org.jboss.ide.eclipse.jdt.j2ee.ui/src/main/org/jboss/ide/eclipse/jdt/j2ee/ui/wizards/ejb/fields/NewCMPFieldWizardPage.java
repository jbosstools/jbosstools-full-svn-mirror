/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.fields;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.ui.wizards.FieldWizardPage;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsUtil;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewCMPFieldWizardPage extends FieldWizardPage
{
   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup accessButtons;
   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup cMPVersionButtons;
   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup primatyKeyButtons;

   private final static String PAGE_NAME = NewCMPFieldWizardPage.class.getName();


   /**Constructor for the NewCMPFieldWizardPage object */
   public NewCMPFieldWizardPage()
   {
      super(PAGE_NAME);
      this.setTitle(JDTJ2EEUIMessages.getString("NewCMPFieldWizardPage.title"));//$NON-NLS-1$
      this.setDescription(JDTJ2EEUIMessages.getString("NewCMPFieldWizardPage.description"));//$NON-NLS-1$
   }


   /**
    * Gets the viewType attribute of the NewCMPFieldWizardPage object
    *
    * @return   The viewType value
    */
   public String getViewType()
   {
      if (this.accessButtons.isSelected(0))
      {
         return "remote";//$NON-NLS-1$
      }
      if (this.accessButtons.isSelected(1))
      {
         return "local";//$NON-NLS-1$
      }
      if (this.accessButtons.isSelected(2))
      {
         return "both";//$NON-NLS-1$
      }
      return null;
   }


   /**
    * Gets the cMP2x attribute of the NewCMPFieldWizardPage object
    *
    * @return   The cMP2x value
    */
   public boolean isCMP2x()
   {
      return this.cMPVersionButtons.isSelected(1);
   }


   /**
    * Gets the primaryKey attribute of the NewCMPFieldWizardPage object
    *
    * @return   The primaryKey value
    */
   public boolean isPrimaryKey()
   {
      return this.primatyKeyButtons.isSelected(0);
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


   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createCMPVersionSelectionControls(Composite composite, int nColumns)
   {
      FieldsUtil.createSelectionButtonDialogFieldGroupControls(this.cMPVersionButtons, composite, nColumns);
   }


   /** Description of the Method */
   protected void createContent()
   {
      super.createContent();
      String[] buttonNames;

      buttonNames = new String[]{JDTJ2EEUIMessages.getString("NewCMPFieldWizardPage.primkey.enable")};//$NON-NLS-1$
      this.primatyKeyButtons = new SelectionButtonDialogFieldGroup(SWT.CHECK, buttonNames, 1);
      this.primatyKeyButtons.setDialogFieldListener(this.getFieldsAdapter());
      this.primatyKeyButtons.setLabelText(JDTJ2EEUIMessages.getString("NewCMPFieldWizardPage.label.primkey"));//$NON-NLS-1$

      buttonNames = new String[]{JDTJ2EEUIMessages.getString("NewCMPFieldWizardPage.cmp.11"), JDTJ2EEUIMessages.getString("NewCMPFieldWizardPage.cmp.2x")};//$NON-NLS-1$ //$NON-NLS-2$
      this.cMPVersionButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO, buttonNames, 2);
      this.cMPVersionButtons.setDialogFieldListener(this.getFieldsAdapter());
      this.cMPVersionButtons.setLabelText(JDTJ2EEUIMessages.getString("NewCMPFieldWizardPage.label.cmp"));//$NON-NLS-1$

      buttonNames = new String[]{JDTJ2EEUIMessages.getString("NewCMPFieldWizardPage.access.remote"), JDTJ2EEUIMessages.getString("NewCMPFieldWizardPage.access.local"), JDTJ2EEUIMessages.getString("NewCMPFieldWizardPage.access.both")};//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      this.accessButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO, buttonNames, 3);
      this.accessButtons.setDialogFieldListener(this.getFieldsAdapter());
      this.accessButtons.setLabelText(JDTJ2EEUIMessages.getString("NewCMPFieldWizardPage.label.access"));//$NON-NLS-1$
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
      this.createPrimaryKeySelectionControls(composite, nColumns);
      this.createSeparator(composite, nColumns);
      this.createCMPVersionSelectionControls(composite, nColumns);
      this.createSeparator(composite, nColumns);
      this.createAccessSelectionControls(composite, nColumns);
   }


   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createPrimaryKeySelectionControls(Composite composite, int nColumns)
   {
      FieldsUtil.createSelectionButtonDialogFieldGroupControls(this.primatyKeyButtons, composite, nColumns);
   }


   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   protected void handleFieldChanged(DialogField field)
   {
      super.handleFieldChanged(field);

      if (field == this.cMPVersionButtons)
      {
         this.accessButtons.setEnabled(isCMP2x());
      }
   }


   /** Description of the Method */
   protected void initContent()
   {
      super.initContent();

      // CMP 2.x by default
      this.cMPVersionButtons.setSelection(1, true);

      // Local by default
      this.accessButtons.setSelection(1, true);
   }
}
