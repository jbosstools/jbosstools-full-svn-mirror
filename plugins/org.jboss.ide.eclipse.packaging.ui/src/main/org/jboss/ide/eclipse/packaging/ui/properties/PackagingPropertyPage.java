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
package org.jboss.ide.eclipse.packaging.ui.properties;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PropertyPage;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.packaging.core.PackagingCorePlugin;
import org.jboss.ide.eclipse.packaging.core.builder.PackagingBuilder;
import org.jboss.ide.eclipse.packaging.core.configuration.ProjectConfigurations;
import org.jboss.ide.eclipse.packaging.core.model.PackagingArchive;
import org.jboss.ide.eclipse.packaging.core.model.PackagingData;
import org.jboss.ide.eclipse.packaging.ui.PackagingUIMessages;
import org.jboss.ide.eclipse.packaging.ui.PackagingUIPlugin;
import org.jboss.ide.eclipse.packaging.ui.util.ConfigurationViewer;
import org.jboss.ide.eclipse.ui.util.UIUtil;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class PackagingPropertyPage extends PropertyPage
{
   /** Description of the Field */
   private ConfigurationViewer configViewer;

   /** Description of the Field */
   private Button addArchiveButton;

   /** Description of the Field */
   private Button addStandardArchiveButton;

   /** Description of the Field */
   private Button editButton;

   /** Description of the Field */
   private Button removeButton;

   /** Description of the Field */
   private Button moveDownButton;

   /** Description of the Field */
   private Button moveUpButton;

   /** Description of the Field */
   private IJavaProject project;

   /** Description of the Field */
   private ProjectConfigurations projectConfigurations;

   private Button enablePackagingButton;

   private Composite parentComposite;

   /**Constructor for the ConfigurationPropertyPage object */
   public PackagingPropertyPage()
   {
      super();
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    * @see      org.eclipse.jface.preference.IPreferencePage#performOk()
    */
   public boolean performOk()
   {
      try
      {
         // Save the configurations
         this.projectConfigurations.storeConfigurations();

         PackagingCorePlugin.getDefault().enablePackagingBuilder(this.project, enablePackagingButton.getSelection());
         //PackagingCorePlugin.getDefault().createBuildFile(this.project.getProject());
         return super.performOk();
      }
      catch (CoreException ce)
      {
         this.openErrorDialog(PackagingUIMessages.getString("PackagingPropertyPage.save.configuration.failed"), ce);//$NON-NLS-1$
      }
      return false;
   }

   /** Description of the Method */
   protected void assign()
   {
      // Change elements according to choices
      this.configViewer.addSelectionChangedListener(new ISelectionChangedListener()
      {
         public void selectionChanged(SelectionChangedEvent event)
         {
            enableButtons();
         }
      });

      this.addArchiveButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            PackagingPropertyPage.this.configViewer.doAddArchive();
         }
      });

      this.addStandardArchiveButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            PackagingPropertyPage.this.configViewer.doAddStandardArchive();
         }
      });

      this.editButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            PackagingPropertyPage.this.configViewer.doEdit();
         }
      });

      this.removeButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            PackagingPropertyPage.this.configViewer.doRemove();
         }
      });

      // Up ordering
      this.moveUpButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            doMoveUp();
         }
      });

      // Down ordering
      this.moveDownButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            doMoveDown();
         }
      });
   }

   /**
    * Description of the Method
    *
    * @param ancestor  Description of the Parameter
    * @return          Description of the Return Value
    */
   protected Composite createButtons(Composite ancestor)
   {
      // The buttons composite
      Composite buttons = new Composite(ancestor, SWT.NONE);
      GridLayout layout = new GridLayout(1, false);
      layout.marginHeight = 0;
      layout.marginWidth = 0;
      buttons.setLayout(layout);
      buttons.setLayoutData(new GridData(GridData.FILL_VERTICAL));

      addArchiveButton = new Button(buttons, SWT.PUSH);
      addArchiveButton.setText(PackagingUIMessages.getString("PackagingPropertyPage.button.add.archive"));//$NON-NLS-1$
      addArchiveButton.setToolTipText(PackagingUIMessages.getString("PackagingPropertyPage.button.add.archive.tip"));//$NON-NLS-1$
      addArchiveButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      addStandardArchiveButton = new Button(buttons, SWT.PUSH);
      addStandardArchiveButton.setText(PackagingUIMessages
            .getString("PackagingPropertyPage.button.add.standard.archive"));//$NON-NLS-1$
      addStandardArchiveButton.setToolTipText(PackagingUIMessages
            .getString("PackagingPropertyPage.button.add.standard.archive.tip"));//$NON-NLS-1$
      addStandardArchiveButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      editButton = new Button(buttons, SWT.PUSH);
      editButton.setText(PackagingUIMessages.getString("PackagingPropertyPage.button.edit"));//$NON-NLS-1$
      editButton.setToolTipText(PackagingUIMessages.getString("PackagingPropertyPage.button.edit.tip"));//$NON-NLS-1$
      editButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      removeButton = new Button(buttons, SWT.PUSH);
      removeButton.setText(PackagingUIMessages.getString("PackagingPropertyPage.button.remove"));//$NON-NLS-1$
      removeButton.setToolTipText(PackagingUIMessages.getString("PackagingPropertyPage.button.remove.tip"));//$NON-NLS-1$
      removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      moveUpButton = new Button(buttons, SWT.PUSH);
      moveUpButton.setText(PackagingUIMessages.getString("PackagingPropertyPage.button.up"));//$NON-NLS-1$
      moveUpButton.setToolTipText(PackagingUIMessages.getString("PackagingPropertyPage.button.up.tip"));//$NON-NLS-1$
      moveUpButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      moveDownButton = new Button(buttons, SWT.PUSH);
      moveDownButton.setText(PackagingUIMessages.getString("PackagingPropertyPage.button.down"));//$NON-NLS-1$
      moveDownButton.setToolTipText(PackagingUIMessages.getString("PackagingPropertyPage.button.down.tip"));//$NON-NLS-1$
      moveDownButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      return buttons;
   }

   /**
    * Description of the Method
    *
    * @param ancestor  Description of the Parameter
    * @return          Description of the Return Value
    * @see             org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
    */
   protected Control createContents(Composite ancestor)
   {
      parentComposite = new Composite(ancestor, SWT.NONE);

      try
      {
         // Grab the project
         this.project = (IJavaProject) this.getElement();
         this.projectConfigurations = PackagingCorePlugin.getDefault().getProjectConfigurations(this.project);
         this.projectConfigurations.loadConfigurations();

         GridLayout layout = new GridLayout(1, false);
         parentComposite.setLayout(layout);

         enablePackagingButton = new Button(parentComposite, SWT.CHECK);
         enablePackagingButton.setText(PackagingUIMessages.getString("PackagingPropertyPage.enablePackagingLabel"));

         enablePackagingButton.addSelectionListener(new SelectionListener()
         {
            public void widgetDefaultSelected(SelectionEvent e)
            {
               widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e)
            {
               boolean enabled = enablePackagingButton.getSelection();
               UIUtil.setEnabledRecursive(parentComposite, enabled);
               enablePackagingButton.setEnabled(true);
               if (enabled)
               {
                  enableButtons();
               }
            }
         });

         boolean hasXDoclet = ProjectUtil.projectHasBuilder(this.project.getProject(), PackagingBuilder.BUILDER_ID);
         UIUtil.setEnabledRecursive(parentComposite, hasXDoclet);
         enablePackagingButton.setEnabled(true);
         enablePackagingButton.setSelection(hasXDoclet);

         // Description label
         Label description = new Label(parentComposite, SWT.NONE);
         description.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
         description.setText(PackagingUIMessages.getString("PackagingPropertyPage.description"));//$NON-NLS-1$

         // The viewer composite (configuration list + buttons)
         Composite viewer = new Composite(parentComposite, SWT.NONE);
         layout = new GridLayout(2, false);
         layout.marginHeight = 0;
         layout.marginWidth = 0;
         viewer.setLayout(layout);
         viewer.setLayoutData(new GridData(GridData.FILL_BOTH));

         // The configuration list
         this.configViewer = new ConfigurationViewer(viewer, this.project.getProject(), this.projectConfigurations
               .getConfigurations());

         // The buttons composite
         createButtons(viewer);

         assign();
         enableButtons();
      }
      catch (CoreException ce)
      {
         openErrorDialog(PackagingUIMessages.getString("PackagingPropertyPage.load.configuration.failed"), ce);//$NON-NLS-1$
      }
      return parentComposite;
   }

   /** Description of the Method */
   private void doMoveDown()
   {
      PackagingData data = this.configViewer.getCurrent();
      if ((data != null) && (data instanceof PackagingArchive))
      {
         this.projectConfigurations.moveDown(data);
         this.configViewer.refresh();
      }
   }

   /** Description of the Method */
   private void doMoveUp()
   {
      PackagingData data = this.configViewer.getCurrent();
      if ((data != null) && (data instanceof PackagingArchive))
      {
         this.projectConfigurations.moveUp(data);
         this.configViewer.refresh();
      }
   }

   /** Description of the Method */
   private void enableButtons()
   {
      PackagingData data = this.configViewer.getCurrent();
      if (data != null)
      {
         this.editButton.setEnabled(true);
         this.removeButton.setEnabled(true);
         if (data instanceof PackagingArchive)
         {
            this.moveUpButton.setEnabled(true);
            this.moveDownButton.setEnabled(true);
         }
         else
         {
            this.moveUpButton.setEnabled(false);
            this.moveDownButton.setEnabled(false);
         }
      }
      else
      {
         this.editButton.setEnabled(false);
         this.removeButton.setEnabled(false);
         this.moveUpButton.setEnabled(false);
         this.moveDownButton.setEnabled(false);
      }
   }

   /**
    * Description of the Method
    *
    * @param msg  Description of the Parameter
    * @param e    Description of the Parameter
    */
   private void openErrorDialog(String msg, Exception e)
   {
      IStatus status = new Status(IStatus.ERROR, PackagingUIPlugin.getUniqueIdentifier(), 0, msg, null);
      if (e != null && e instanceof CoreException)
      {
         status = ((CoreException) e).getStatus();
      }
      ErrorDialog.openError(this.getShell(), msg, null, status);
   }
}
