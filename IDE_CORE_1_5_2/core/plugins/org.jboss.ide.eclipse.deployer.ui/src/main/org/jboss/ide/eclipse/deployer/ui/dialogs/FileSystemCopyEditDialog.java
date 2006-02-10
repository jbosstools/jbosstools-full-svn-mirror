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
package org.jboss.ide.eclipse.deployer.ui.dialogs;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.deployer.core.target.FileSystemCopy;
import org.jboss.ide.eclipse.deployer.core.target.ITarget;
import org.jboss.ide.eclipse.deployer.ui.DeployerUIMessages;
import org.jboss.ide.eclipse.deployer.ui.DeployerUIPlugin;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class FileSystemCopyEditDialog extends TargetEditDialog
{
   private Button browseButton;

   private Text nameText;

   private Text pathText;

   private FileSystemCopy target;

   private FileSystemCopy tempTarget;

   /**
    *Constructor for the AttributeEditDialog object
    *
    * @param parentShell  Description of the Parameter
    * @param target       Description of the Parameter
    */
   public FileSystemCopyEditDialog(Shell parentShell, ITarget target)
   {
      super(parentShell);
      this.setStatusLineAboveButtons(true);
      this.target = (FileSystemCopy) target;
      this.tempTarget = (FileSystemCopy) this.target.clone();
   }

   /** Description of the Method */
   protected void assign()
   {
      this.nameText.addModifyListener(new ModifyListener()
      {
         public void modifyText(ModifyEvent e)
         {
            tempTarget.setName(((Text) e.widget).getText());
            validate();
         }
      });

      this.browseButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            addSelectExternal();
            refresh();
            validate();
         }
      });
   }

   /**
    * Description of the Method
    *
    * @param shell  Description of the Parameter
    */
   protected void configureShell(Shell shell)
   {
      this.setTitle(DeployerUIMessages.getString("FileSystemCopyEditDialog.title"));//$NON-NLS-1$
      super.configureShell(shell);
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
      GridData layoutData;
      Composite global = (Composite) super.createDialogArea(parent);
      GridLayout gridLayout = new GridLayout(3, false);
      gridLayout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
      gridLayout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
      global.setLayout(gridLayout);

      Label descLabel = new Label(global, SWT.NONE);
      descLabel.setText(DeployerUIMessages.getString("FileSystemCopyEditDialog.description"));//$NON-NLS-1$
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.horizontalSpan = 3;
      descLabel.setLayoutData(layoutData);

      Label fileLabel = new Label(global, SWT.NONE);
      fileLabel.setText(DeployerUIMessages.getString("FileSystemCopyEditDialog.label.name"));//$NON-NLS-1$
      layoutData = new GridData();
      layoutData.horizontalAlignment = SWT.END;
      fileLabel.setLayoutData(layoutData);

      this.nameText = new Text(global, SWT.BORDER);
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.horizontalSpan = 2;
      layoutData.grabExcessHorizontalSpace = true;
      this.nameText.setLayoutData(layoutData);

      Label pathLabel = new Label(global, SWT.NONE);
      pathLabel.setText(DeployerUIMessages.getString("FileSystemCopyEditDialog.label.path"));//$NON-NLS-1$
      layoutData = new GridData();
      layoutData.horizontalAlignment = SWT.END;
      pathLabel.setLayoutData(layoutData);

      this.pathText = new Text(global, SWT.BORDER);
      this.pathText.setEditable(false);
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.grabExcessHorizontalSpace = true;
      this.pathText.setLayoutData(layoutData);

      this.browseButton = new Button(global, SWT.PUSH);
      this.browseButton.setText(DeployerUIMessages.getString("FileSystemCopyEditDialog.button.browse"));//$NON-NLS-1$
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      this.browseButton.setLayoutData(layoutData);

      this.assign();
      this.refresh();

      return parent;
   }

   /** Description of the Method */
   protected void okPressed()
   {
      // Copy modified content
      this.target.setName(this.tempTarget.getName());
      this.target.setURL(this.tempTarget.getURL());

      super.okPressed();
   }

   /** Description of the Method */
   protected void validate()
   {
      IStatus status = null;
      if (this.tempTarget.getURL() != null)
      {
         status = new Status(IStatus.OK, DeployerUIPlugin.getUniqueIdentifier(), 0, "", null);//$NON-NLS-1$
      }
      else
      {
         status = new Status(IStatus.ERROR, DeployerUIPlugin.getUniqueIdentifier(), 0, DeployerUIMessages
               .getString("FileSystemCopyEditDialog.status.path.not.valid.message"), null);//$NON-NLS-1$
      }
      this.updateStatus(status);
   }

   /** Adds a feature to the SelectLocalFile attribute of the FileEditDialog object */
   private void addSelectExternal()
   {
      DirectoryDialog dialog = new DirectoryDialog(AbstractPlugin.getShell(), SWT.OPEN);
      String result = dialog.open();
      if (result != null)
      {
         try
         {
            File path = new File(result);
            URL url = path.toURL();
            this.tempTarget.setURL(url);
            this.refresh();
         }
         catch (MalformedURLException mfue)
         {
            AbstractPlugin.logError("Error while converting path to URL (" + result + ")", mfue);//$NON-NLS-1$ //$NON-NLS-2$
         }
      }
   }

   /** Description of the Method */
   private void refresh()
   {
      this.nameText.setText(this.tempTarget.getName());
      if (this.tempTarget.getURL() != null)
      {
         this.pathText.setText(this.tempTarget.getURL().toString());
      }
   }
}
