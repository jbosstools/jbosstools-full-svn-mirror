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

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.jboss.ide.eclipse.deployer.core.target.ITarget;
import org.jboss.ide.eclipse.deployer.ui.DeployerUIMessages;
import org.jboss.ide.eclipse.deployer.ui.util.TargetLabelProvider;
import org.jboss.ide.eclipse.ui.util.ListContentProvider;
import org.jboss.ide.eclipse.ui.util.StringViewSorter;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class TargetChoiceDialog extends Dialog
{
   private static Object lastUsed;

   /** Description of the Field */
   private List choices;

   /** Description of the Field */
   private ITarget data = null;

   /** Description of the Field */
   private StructuredViewer viewer;

   /**
    *Constructor for the DataChoiceDialog object
    *
    * @param parentShell  Description of the Parameter
    * @param choices      Description of the Parameter
    */
   public TargetChoiceDialog(Shell parentShell, List choices)
   {
      super(parentShell);
      this.data = null;
      this.choices = choices;
   }

   /**
    * Gets the xDocletData attribute of the DataChoiceDialog object
    *
    * @return   The xDocletData value
    */
   public ITarget getTarget()
   {
      return this.data;
   }

   /**
    * Description of the Method
    *
    * @param shell  Description of the Parameter
    */
   protected void configureShell(Shell shell)
   {
      super.configureShell(shell);
      shell.setText(DeployerUIMessages.getString("TargetChoiceDialog.title"));//$NON-NLS-1$
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

      Table dataList = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
      dataList.setLayoutData(new GridData(GridData.FILL_BOTH));

      this.viewer = new TableViewer(dataList);
      this.viewer.setContentProvider(new ListContentProvider());
      this.viewer.setLabelProvider(new TargetLabelProvider());
      this.viewer.setSorter(new StringViewSorter());
      this.viewer.setInput(this.choices);
      this.viewer.addSelectionChangedListener(new ISelectionChangedListener()
      {
         public void selectionChanged(SelectionChangedEvent event)
         {
            if (!viewer.getSelection().isEmpty())
            {
               getButton(OK).setEnabled(true);
            }
         }
      });
      this.viewer.addDoubleClickListener(new IDoubleClickListener()
      {
         public void doubleClick(DoubleClickEvent event)
         {
            okPressed();
         }
      });

      return composite;
   }

   protected void createButtonsForButtonBar(Composite parent)
   {
      super.createButtonsForButtonBar(parent);

      getButton(OK).setEnabled(false);
      setSelectedTarget(lastUsed);
   }

   /** Description of the Method */
   protected void okPressed()
   {
      IStructuredSelection selection = (IStructuredSelection) this.viewer.getSelection();
      if (!selection.isEmpty())
      {
         this.data = (ITarget) selection.getFirstElement();
      }

      lastUsed = selection.getFirstElement();

      super.okPressed();
   }

   private void setSelectedTarget(Object target)
   {
      if (choices != null && choices.size() > 0)
      {
         if (target != null && choices.contains(target))
         {
            viewer.setSelection(new StructuredSelection(target));
            getButton(OK).setEnabled(true);
         }
      }
   }
}
