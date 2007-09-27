/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletConfiguration;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @created   18 mars 2003
 * @todo      Javadoc to complete
 */
public class ConfigurationEditDialog extends Dialog
{
   /** Description of the Field */
   private XDocletConfiguration configuration = null;

   /** Description of the Field */
   private Text nameText;

   /**
    *Constructor for the ConfigurationEditDialog object
    *
    * @param parentShell  Description of the Parameter
    */
   public ConfigurationEditDialog(Shell parentShell)
   {
      super(parentShell);
   }

   /**
    *Constructor for the AttributeEditDialog object
    *
    * @param parentShell    Description of the Parameter
    * @param configuration  Description of the Parameter
    */
   public ConfigurationEditDialog(Shell parentShell, XDocletConfiguration configuration)
   {
      super(parentShell);
      this.configuration = configuration;
   }

   /**
    * Gets the xDocletConfiguration attribute of the ConfigurationEditDialog object
    *
    * @return   The xDocletConfiguration value
    */
   public XDocletConfiguration getXDocletConfiguration()
   {
      return this.configuration;
   }

   /**
    * Description of the Method
    *
    * @param newShell  Description of the Parameter
    */
   protected void configureShell(Shell newShell)
   {
      super.configureShell(newShell);
      newShell.setText(XDocletRunMessages.getString("ConfigurationEditDialog.title"));//$NON-NLS-1$
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
      nameLabel.setText(XDocletRunMessages.getString("ConfigurationEditDialog.xdoclet.configuration.name"));//$NON-NLS-1$

      this.nameText = new Text(composite, SWT.BORDER);
      this.nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      if (this.configuration != null)
      {
         this.nameText.setText(this.configuration.getName());
      }

      return composite;
   }

   /** Description of the Method */
   protected void okPressed()
   {
      if (this.configuration == null && this.nameText.getText().length() > 0)
      {
         this.configuration = new XDocletConfiguration();
         this.configuration.setName(this.nameText.getText());
         this.configuration.setUsed(true);
      }
      else
      {
         this.configuration.setName(this.nameText.getText());
      }

      super.okPressed();
   }
   
   public Text getNameText ()
   {
      return nameText;
   }
}
