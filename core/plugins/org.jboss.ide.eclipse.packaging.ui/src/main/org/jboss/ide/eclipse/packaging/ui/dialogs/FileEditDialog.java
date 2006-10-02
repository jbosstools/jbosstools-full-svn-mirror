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
package org.jboss.ide.eclipse.packaging.ui.dialogs;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.packaging.core.model.PackagingArchive;
import org.jboss.ide.eclipse.packaging.core.model.PackagingFile;
import org.jboss.ide.eclipse.packaging.ui.PackagingUIMessages;
import org.jboss.ide.eclipse.ui.dialogs.FileSelectionDialog;
import org.jboss.ide.eclipse.ui.util.ProjectContentProvider;
import org.jboss.ide.eclipse.ui.util.ProjectLabelProvider;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class FileEditDialog extends Dialog
{
   private PackagingArchive archive;

   private Button externalBrowseButton;

   private PackagingFile file;

   private Text filenameText;

   private Button localBrowseButton;

   private Text prefixText;

   private PackagingFile tempFile;

   /**
    *Constructor for the AttributeEditDialog object
    *
    * @param parentShell  Description of the Parameter
    * @param file         Description of the Parameter
    * @param archive      Description of the Parameter
    */
   public FileEditDialog(Shell parentShell, PackagingArchive archive, PackagingFile file)
   {
      super(parentShell);
      this.archive = archive;
      this.file = file;
      this.tempFile = (PackagingFile) this.file.clone();
   }

   /**
    * Gets the data attribute of the FileEditDialog object
    *
    * @return   The data value
    */
   public PackagingFile getPackagingFile()
   {
      return this.file;
   }

   /** Description of the Method */
   protected void assign()
   {
      this.localBrowseButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            addSelectLocal();
         }
      });

      this.externalBrowseButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            addSelectExternal();
         }
      });

      this.prefixText.addModifyListener(new ModifyListener()
      {
         public void modifyText(ModifyEvent e)
         {
            tempFile.setPrefix(((Text) e.widget).getText());
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
      super.configureShell(shell);
      shell.setText(PackagingUIMessages.getString("FileEditDialog.title"));//$NON-NLS-1$
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
      descLabel.setText(PackagingUIMessages.getString("FileEditDialog.description"));//$NON-NLS-1$
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.horizontalSpan = 3;
      descLabel.setLayoutData(layoutData);

      Label fileLabel = new Label(global, SWT.NONE);
      fileLabel.setText(PackagingUIMessages.getString("FileEditDialog.label.file"));//$NON-NLS-1$
      layoutData = new GridData();
      layoutData.horizontalAlignment = SWT.END;
      fileLabel.setLayoutData(layoutData);

      this.filenameText = new Text(global, SWT.BORDER);
      this.filenameText.setEditable(false);
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.grabExcessHorizontalSpace = true;
      this.filenameText.setLayoutData(layoutData);

      this.localBrowseButton = new Button(global, SWT.PUSH);
      this.localBrowseButton.setText(PackagingUIMessages.getString("FileEditDialog.button.local.browse"));//$NON-NLS-1$
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      this.localBrowseButton.setLayoutData(layoutData);

      // Spacers
      new Label(global, SWT.NONE);
      new Label(global, SWT.NONE);

      this.externalBrowseButton = new Button(global, SWT.PUSH);
      this.externalBrowseButton.setText(PackagingUIMessages.getString("FileEditDialog.button.external.browser"));//$NON-NLS-1$
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      this.externalBrowseButton.setLayoutData(layoutData);

      Label prefixLabel = new Label(global, SWT.NONE);
      prefixLabel.setText(PackagingUIMessages.getString("FileEditDialog.label.prefix"));//$NON-NLS-1$
      layoutData = new GridData();
      layoutData.horizontalAlignment = SWT.RIGHT;
      prefixLabel.setLayoutData(layoutData);

      this.prefixText = new Text(global, SWT.BORDER);
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.horizontalSpan = 2;
      layoutData.grabExcessHorizontalSpace = true;
      this.prefixText.setLayoutData(layoutData);

      this.refresh();
      this.assign();

      return parent;
   }

   /** Description of the Method */
   protected void okPressed()
   {
      // Copy modified content
      if (this.tempFile.isLocal())
      {
         this.file.setProject(this.tempFile.getProject());
         this.file.setProjectLocation(this.tempFile.getProjectLocation());
      }
      else
      {
         this.file.setLocation(this.tempFile.getLocation());
      }
      this.file.setPrefix(this.tempFile.getPrefix());

      super.okPressed();
   }

   /** Adds a feature to the SelectExternalFile attribute of the FileEditDialog object */
   private void addSelectExternal()
   {
      FileDialog dialog = new FileDialog(AbstractPlugin.getShell(), SWT.OPEN);
      String result = dialog.open();
      if (result != null)
      {
         IPath path = new Path(result);
         this.tempFile.setLocation(path.toString());
         this.refresh();
      }
   }

   /** Adds a feature to the SelectLocalFile attribute of the FileEditDialog object */
   private void addSelectLocal()
   {
      FileSelectionDialog dialog = new FileSelectionDialog(AbstractPlugin.getShell(), new ProjectLabelProvider(),
            new ProjectContentProvider());

      // Select all projects as input
      dialog.setInput(ProjectUtil.getAllOpenedProjects());

      // If there is a selection, use it
      if (this.tempFile.resolve(this.archive) != null)
      {
         dialog.setInitialSelection(this.tempFile.resolve(this.archive));
      }

      // Open the dialog and wait for the result
      if (dialog.open() == IDialogConstants.OK_ID)
      {
         IResource resource = (IResource) dialog.getFirstResult();
         if (resource.getProject().equals(this.archive.getProject()))
         {
            this.tempFile.setProject("");//$NON-NLS-1$
         }
         else
         {
            this.tempFile.setProject(resource.getProject().getName());
         }
         this.tempFile.setProjectLocation(resource.getProjectRelativePath().toString());
         this.refresh();
      }
   }

   /** Description of the Method */
   private void refresh()
   {
      if (tempFile.isLocal())
      {
         this.filenameText.setText(this.tempFile.resolve(this.archive).getFullPath().toString());
      }
      else
      {
         this.filenameText.setText(this.tempFile.getLocation());
      }
      this.prefixText.setText(this.tempFile.getPrefix());
   }
}
