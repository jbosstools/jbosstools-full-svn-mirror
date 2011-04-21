/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ws.ui.dialog;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsAdapter;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsAdapterListener;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsUtil;
import org.jboss.ide.eclipse.jdt.ws.ui.JDTWSUIMessages;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class MappingEditDialog extends Dialog implements FieldsAdapterListener
{//$NON-NLS-1$
   private StringDialogField nameDialogField;
   private String namespace = "";//$NON-NLS-1$
   private String pakkage = "";//$NON-NLS-1$
   private StringDialogField typeDialogField;


   /**
    *Constructor for the ExceptionEditDialog object
    *
    * @param parentShell  Description of the Parameter
    */
   public MappingEditDialog(Shell parentShell)
   {
      super(parentShell);
   }


   /**
    * Gets the name attribute of the ParameterEditDialog object
    *
    * @return   The name value
    */
   public String getName()
   {
      return this.namespace;
   }


   /**
    * Gets the type attribute of the ParameterEditDialog object
    *
    * @return   The type value
    */
   public String getPakkage()
   {
      return this.pakkage;
   }


   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   public void pageChangeControlPressed(DialogField field)
   {
   }


   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   public void pageDialogFieldChanged(DialogField field)
   {
      this.namespace = this.nameDialogField.getText();
      this.pakkage = this.typeDialogField.getText();
   }


   /**
    * Description of the Method
    *
    * @param newShell  Description of the Parameter
    */
   protected void configureShell(Shell newShell)
   {
      super.configureShell(newShell);
      newShell.setText(JDTWSUIMessages.getString("MappingEditDialog.title"));//$NON-NLS-1$
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

      this.nameDialogField = new StringDialogField();
      this.nameDialogField.setDialogFieldListener(adapter);
      this.nameDialogField.setLabelText(JDTWSUIMessages.getString("MappingEditDialog.label.namesapce"));//$NON-NLS-1$

      this.typeDialogField = new StringDialogField();
      this.typeDialogField.setDialogFieldListener(adapter);
      this.typeDialogField.setLabelText(JDTWSUIMessages.getString("MappingEditDialog.label.package"));//$NON-NLS-1$

      FieldsUtil.createStringDialogFieldControls(this.nameDialogField, composite, nColumns);
      FieldsUtil.createStringDialogFieldControls(this.typeDialogField, composite, nColumns);

      return composite;
   }


   /** Description of the Method */
   protected void okPressed()
   {
      if (this.namespace != null && this.pakkage != null && !"".equals(this.namespace) && !"".equals(this.pakkage)//$NON-NLS-1$ //$NON-NLS-2$
      )
      {
         super.okPressed();
      }
   }
}
