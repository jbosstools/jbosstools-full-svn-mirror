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

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.packaging.core.model.PackagingArchive;
import org.jboss.ide.eclipse.packaging.ui.PackagingUIMessages;
import org.jboss.ide.eclipse.ui.dialogs.FolderSelectionDialog;
import org.jboss.ide.eclipse.ui.util.ProjectContentProvider;
import org.jboss.ide.eclipse.ui.util.ProjectLabelProvider;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ArchiveEditDialog extends Dialog
{
   private PackagingArchive archive;

   private Button browseButton;

   private Text destinationText;

   private Button explodeButton;

   private Text nameText;

   private IProject project;

   private PackagingArchive tempArchive;

   /**
    *Constructor for the AttributeEditDialog object
    *
    * @param parentShell  Description of the Parameter
    * @param archive      Description of the Parameter
    * @param project      Description of the Parameter
    */
   public ArchiveEditDialog(Shell parentShell, IProject project, PackagingArchive archive)
   {
      super(parentShell);
      this.project = project;
      this.archive = archive;
      this.tempArchive = new PackagingArchive();
      this.tempArchive.setName(this.archive.getName());
      this.tempArchive.setDestination(this.archive.getDestination());
      this.tempArchive.setExploded(this.archive.isExploded());
   }

   /** Description of the Method */
   protected void assign()
   {
      this.nameText.addModifyListener(new ModifyListener()
      {
         public void modifyText(ModifyEvent e)
         {
            tempArchive.setName(((Text) e.widget).getText());
         }
      });

      this.destinationText.addModifyListener(new ModifyListener()
      {
         public void modifyText(ModifyEvent e)
         {
            tempArchive.setDestination(((Text) e.widget).getText());
         }
      });

      this.browseButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            addSelectLocal();
         }
      });

      this.explodeButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            tempArchive.setExploded(((Button) e.widget).getSelection());
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
      shell.setText(PackagingUIMessages.getString("ArchiveEditDialog.title"));//$NON-NLS-1$
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
      descLabel.setText(PackagingUIMessages.getString("ArchiveEditDialog.description"));//$NON-NLS-1$
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.horizontalSpan = 3;
      descLabel.setLayoutData(layoutData);

      Label fileLabel = new Label(global, SWT.NONE);
      fileLabel.setText(PackagingUIMessages.getString("ArchiveEditDialog.label.name"));//$NON-NLS-1$

      this.nameText = new Text(global, SWT.BORDER);
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.horizontalSpan = 2;
      layoutData.grabExcessHorizontalSpace = true;
      this.nameText.setLayoutData(layoutData);

      Label explodeLabel = new Label(global, SWT.NONE);
      explodeLabel.setText(PackagingUIMessages.getString("ArchiveEditDialog.label.exploded"));//$NON-NLS-1$

      this.explodeButton = new Button(global, SWT.CHECK);
      layoutData = new GridData(GridData.BEGINNING);
      layoutData.horizontalSpan = 2;
      layoutData.grabExcessHorizontalSpace = true;
      this.explodeButton.setLayoutData(layoutData);

      Label includesLabel = new Label(global, SWT.NONE);
      includesLabel.setText(PackagingUIMessages.getString("ArchiveEditDialog.label.destination"));//$NON-NLS-1$

      this.destinationText = new Text(global, SWT.BORDER);
      this.destinationText.setEditable(false);
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.grabExcessHorizontalSpace = true;
      destinationText.setLayoutData(layoutData);

      this.browseButton = new Button(global, SWT.PUSH);
      this.browseButton.setText(PackagingUIMessages.getString("ArchiveEditDialog.button.folder"));//$NON-NLS-1$
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      this.browseButton.setLayoutData(layoutData);

      this.refresh();
      this.assign();

      return parent;
   }

   /** Description of the Method */
   protected void okPressed()
   {
      // Copy modified content
      this.archive.setName(this.tempArchive.getName());
      this.archive.setDestination(this.tempArchive.getDestination());
      this.archive.setExploded(this.tempArchive.isExploded());

      super.okPressed();
   }

   /** Adds a feature to the SelectLocalFile attribute of the FileEditDialog object */
   private void addSelectLocal()
   {
      FolderSelectionDialog dialog = new FolderSelectionDialog(AbstractPlugin.getShell(), new ProjectLabelProvider(),
            new ProjectContentProvider());

      // Select the current project as input
      ArrayList list = new ArrayList();
      list.add(this.project);
      dialog.setInput(list);

      // If there is a selection, use it
      IResource initial = ProjectUtil.getResource(IPath.SEPARATOR + this.project.getName() + IPath.SEPARATOR
            + this.tempArchive.getDestination());
      if (initial != null)
      {
         dialog.setInitialSelection(initial);
      }

      // Open the dialog and wait for the result
      if (dialog.open() == IDialogConstants.OK_ID)
      {
         IResource resource = (IResource) dialog.getFirstResult();
         this.tempArchive.setDestination(resource.getProjectRelativePath().toString());
         this.refresh();
      }
   }

   /** Description of the Method */
   private void refresh()
   {
      this.nameText.setText(this.tempArchive.getName());
      this.destinationText.setText(this.tempArchive.getDestination());
      this.explodeButton.setSelection(this.tempArchive.isExploded());
      this.explodeButton.redraw();
   }
}
