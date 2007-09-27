/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.methods;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.ui.wizards.MethodWizardPage;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsUtil;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class EJBMethodWizardPage extends MethodWizardPage
{
   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup accessButtons;


   /**
    *Constructor for the EJBMethodWizardPage object
    *
    * @param name  Description of the Parameter
    */
   public EJBMethodWizardPage(String name)
   {
      super(name);
   }


   /**
    * Gets the viewType attribute of the NewBusinessMethodWizardPage object
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

      buttonNames = new String[]{JDTJ2EEUIMessages.getString("EJBMethodWizardPage.access.remote"), JDTJ2EEUIMessages.getString("EJBMethodWizardPage.access.local"), JDTJ2EEUIMessages.getString("EJBMethodWizardPage.access.both")};//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      this.accessButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO, buttonNames, 3);
      this.accessButtons.setDialogFieldListener(this.getFieldsAdapter());
      this.accessButtons.setLabelText(JDTJ2EEUIMessages.getString("EJBMethodWizardPage.label.access"));//$NON-NLS-1$
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
      this.createAccessSelectionControls(composite, nColumns);
   }


   /** Description of the Method */
   protected void initContent()
   {
      // Remote by default
      this.accessButtons.setSelection(0, true);
   }
}
