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
package org.jboss.ide.eclipse.packaging.ui.dialogs;

import java.util.Collection;

import org.eclipse.jdt.internal.ui.viewsupport.ListContentProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.packaging.core.model.PackagingArchive;
import org.jboss.ide.eclipse.packaging.ui.PackagingUIMessages;
import org.jboss.ide.eclipse.ui.util.StringViewSorter;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class DataChoiceDialog extends Dialog
{
   private Text nameText;

   /** Description of the Field */
   private Collection choices;

   /** Description of the Field */
   private PackagingArchive archive = null;

   /** Description of the Field */
   private ListViewer viewer;

   /**
    *Constructor for the DataChoiceDialog object
    *
    * @param parentShell  Description of the Parameter
    * @param choices      Description of the Parameter
    */
   public DataChoiceDialog(Shell parentShell, Collection choices)
   {
      super(parentShell);
      this.archive = null;
      this.choices = choices;
   }

   /**
    * Gets the xDocletData attribute of the DataChoiceDialog object
    *
    * @return   The xDocletData value
    */
   public PackagingArchive getPackagingArchive()
   {
      return this.archive;
   }

   /**
    * Description of the Method
    *
    * @param shell  Description of the Parameter
    */
   protected void configureShell(Shell shell)
   {
      super.configureShell(shell);
      shell.setText(PackagingUIMessages.getString("DataChoiceDialog.title"));//$NON-NLS-1$
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
      GridLayout gridLayout = new GridLayout(2, false);
      gridLayout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
      gridLayout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
      composite.setLayout(gridLayout);

      Label fileLabel = new Label(composite, SWT.NONE);
      fileLabel.setText(PackagingUIMessages.getString("ArchiveEditDialog.label.name"));//$NON-NLS-1$

      this.nameText = new Text(composite, SWT.BORDER);
      this.nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      this.nameText.setText("Untitled.jar");

      this.nameText.addModifyListener(new ModifyListener()
      {
         public void modifyText(ModifyEvent e)
         {
            enableButtons();
         }
      });

      List dataList = new List(composite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
      GridData layoutData = new GridData(GridData.FILL_BOTH);
      layoutData.horizontalSpan = 2;
      dataList.setLayoutData(layoutData);

      this.viewer = new ListViewer(dataList);
      this.viewer.setContentProvider(new ListContentProvider());
      this.viewer.setSorter(new StringViewSorter());
      this.viewer.setInput(this.choices);
      this.viewer.addSelectionChangedListener(new ISelectionChangedListener()
      {
         public void selectionChanged(SelectionChangedEvent event)
         {
            IStructuredSelection selection = (IStructuredSelection) event.getSelection();
            if (!selection.isEmpty())
            {
               PackagingArchive tempArchive = (PackagingArchive) selection.getFirstElement();
               String name = nameText.getText();
               String ext = tempArchive.getName().substring(tempArchive.getName().lastIndexOf('.'));

               int i = name.lastIndexOf('.');
               if (i == -1)
               {
                  name = name + ext;
               }
               else
               {
                  name = name.substring(0, i) + ext;
               }

               enableButtons();
               nameText.setText(name);
            }
         }
      });

      return composite;
   }

   protected Control createContents(Composite parent)
   {
      Control control = super.createContents(parent);
      enableButtons();

      getButton(IDialogConstants.OK_ID).setEnabled(false);
      return control;
   }

   protected Point getInitialSize()
   {
      return getShell().computeSize(220, 300, true);
   }

   /** Description of the Method */
   protected void okPressed()
   {
      IStructuredSelection selection = (IStructuredSelection) this.viewer.getSelection();
      if (!selection.isEmpty())
      {
         PackagingArchive tempArchive = (PackagingArchive) selection.getFirstElement();
         this.archive = (PackagingArchive) tempArchive.cloneData();
         this.archive.setName(this.nameText.getText().trim());
      }
      super.okPressed();
   }

   /**
    * 
    */
   private void enableButtons()
   {
      Button okButton = getButton(IDialogConstants.OK_ID);
      if (nameText.getText().trim().length() > 0 && !viewer.getSelection().isEmpty())
      {
         okButton.setEnabled(true);
      }
      else
      {
         okButton.setEnabled(false);
      }
   }
}
