/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.run.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunMessages;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletAttribute;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @created   18 mars 2003
 * @todo      Javadoc to complete
 */
public class AttributeEditDialog extends Dialog
{
   /** Description of the Field */
   private XDocletAttribute attribute;
   /** Description of the Field */
   private Text valueText;


   /**
    *Constructor for the AttributeEditDialog object
    *
    * @param parentShell  Description of the Parameter
    * @param attribute    Description of the Parameter
    */
   public AttributeEditDialog(Shell parentShell, XDocletAttribute attribute)
   {
      super(parentShell);
      this.attribute = attribute;
   }


   /**
    * Description of the Method
    *
    * @param newShell  Description of the Parameter
    */
   protected void configureShell(Shell newShell)
   {
      super.configureShell(newShell);
      newShell.setText(XDocletRunMessages.getString("AttributeEditDialog.title"));//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    * @return        Description of the Return Value
    * @see           org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
    */
   protected Control createDialogArea(Composite parent)
   {
      Composite composite = (Composite) super.createDialogArea(parent);

      GridLayout layout = new GridLayout();
      layout.numColumns = 2;
      layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
      layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
      composite.setLayout(layout);

      Label nameLabel = new Label(composite, SWT.NONE);
      nameLabel.setText(XDocletRunMessages.getString("AttributeEditDialog.xdoclet.attribute.value"));//$NON-NLS-1$
      this.valueText = new Text(composite, SWT.BORDER);
      this.valueText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      this.valueText.setText(this.attribute.getValue());

      return composite;
   }


   /** Description of the Method */
   protected void okPressed()
   {
      this.attribute.setValue(this.valueText.getText());
      this.attribute.setUsed(true);
      super.okPressed();
   }
}
