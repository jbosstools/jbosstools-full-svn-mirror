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
package org.jboss.ide.eclipse.xdoclet.run.ui.properties;

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
import org.jboss.ide.eclipse.ui.util.UIUtil;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunMessages;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunPlugin;
import org.jboss.ide.eclipse.xdoclet.run.builder.XDocletRunBuilder;
import org.jboss.ide.eclipse.xdoclet.run.configuration.ProjectConfigurations;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletConfiguration;
import org.jboss.ide.eclipse.xdoclet.run.ui.ConfigurationListViewer;
import org.jboss.ide.eclipse.xdoclet.run.ui.ConfigurationViewer;
import org.jboss.ide.eclipse.xdoclet.run.ui.ElementViewer;

/**
 * @author    Laurent Etiemble
 * @version   $Revision: 1420 $
 * @created   17 mars 2003
 * @todo      Javadoc to complete
 */
public class ConfigurationPropertyPage extends PropertyPage
{
   public static final int BUTTON_ADD = 0;
   public static final int BUTTON_ADD_STANDARD = 1;
   public static final int BUTTON_RENAME = 2;
   public static final int BUTTON_REMOVE = 3;
   public static final int BUTTON_MOVE_DOWN = 4;
   public static final int BUTTON_MOVE_UP = 5;
   public static final int BUTTON_ENABLE_XDOCLET = 6;
   
   /** Description of the Field */
   private ConfigurationListViewer configListViewer;

   /** Description of the Field */
   private ConfigurationViewer configViewer;

   /** Description of the Field */
   private Button addButton;

   /** Description of the Field */
   private Button addStandardButton;

   /** Description of the Field */
   private Button renameButton;

   /** Description of the Field */
   private Button removeButton;

   /** Description of the Field */
   private Button moveDownButton;

   /** Description of the Field */
   private Button moveUpButton;

   /** Description of the Field */
   private Button enableXDocletButton;

   private Composite parentComposite;

   private IJavaProject project;

   /** Description of the Field */
   private ProjectConfigurations projectConfigurations;

   /** Description of the Field */
   private ElementViewer properiesViewer;

   /**Constructor for the ConfigurationPropertyPage object */
   public ConfigurationPropertyPage()
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
         XDocletRunPlugin.getDefault().enableXDocletBuilder(this.project, enableXDocletButton.getSelection());

         //XDocletRunPlugin.getDefault().createBuildFile(this.project);
         return super.performOk();
      }
      catch (CoreException ce)
      {
         this.openErrorDialog(XDocletRunMessages.getString("ConfigurationPropertyPage.failed.save.configuration"), ce);//$NON-NLS-1$
      }
      //      catch (IOException e)
      //      {
      //         this.openErrorDialog(XDocletRunMessages.getString("ConfigurationPropertyPage.failed.save.configuration"), e);//$NON-NLS-1$
      //      }
      //      catch (TransformerException e)
      //      {
      //         this.openErrorDialog(XDocletRunMessages.getString("ConfigurationPropertyPage.failed.save.configuration"), e);//$NON-NLS-1$
      //      }
      return false;
   }

   /** Description of the Method */
   protected void assign()
   {
      // Change configuration according to choices
      this.configListViewer.addSelectionChangeListener(new ISelectionChangedListener()
      {
         public void selectionChanged(SelectionChangedEvent event)
         {
            enableButtons();
            ConfigurationPropertyPage.this.configViewer
                  .setConfiguration(ConfigurationPropertyPage.this.configListViewer.getCurrent());
         }
      });

      // Change elements according to choices
      this.configViewer.addSelectionChangedListener(new ISelectionChangedListener()
      {
         public void selectionChanged(SelectionChangedEvent event)
         {
            ConfigurationPropertyPage.this.properiesViewer.setElement(ConfigurationPropertyPage.this.configViewer
                  .getCurrent());
         }
      });

      this.addButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            ConfigurationPropertyPage.this.configListViewer.doAdd();
         }
      });

      this.addStandardButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            ConfigurationPropertyPage.this.configListViewer.doAddStandard();
         }
      });

      this.renameButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            ConfigurationPropertyPage.this.configListViewer.doRename();
         }
      });

      this.removeButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            ConfigurationPropertyPage.this.configListViewer.doRemove();
         }
      });

      moveUpButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            doMoveUp();
         }
      });

      moveDownButton.addSelectionListener(new SelectionAdapter()
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

      // The buttons
      addButton = new Button(buttons, SWT.PUSH);
      addButton.setText(XDocletRunMessages.getString("ConfigurationPropertyPage.button.add"));//$NON-NLS-1$
      addButton.setToolTipText(XDocletRunMessages.getString("ConfigurationPropertyPage.button.add.tip"));//$NON-NLS-1$
      addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      addStandardButton = new Button(buttons, SWT.PUSH);
      addStandardButton.setText(XDocletRunMessages.getString("ConfigurationPropertyPage.button.add.standard"));//$NON-NLS-1$
      addStandardButton.setToolTipText(XDocletRunMessages
            .getString("ConfigurationPropertyPage.button.add.standard.tip"));//$NON-NLS-1$
      addStandardButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      renameButton = new Button(buttons, SWT.PUSH);
      renameButton.setText(XDocletRunMessages.getString("ConfigurationPropertyPage.button.rename"));//$NON-NLS-1$
      renameButton.setToolTipText(XDocletRunMessages.getString("ConfigurationPropertyPage.button.rename.tip"));//$NON-NLS-1$
      renameButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      removeButton = new Button(buttons, SWT.PUSH);
      removeButton.setText(XDocletRunMessages.getString("ConfigurationPropertyPage.button.remove"));//$NON-NLS-1$
      removeButton.setToolTipText(XDocletRunMessages.getString("ConfigurationPropertyPage.button.remove.tip"));//$NON-NLS-1$
      removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      moveUpButton = new Button(buttons, SWT.PUSH);
      moveUpButton.setText(XDocletRunMessages.getString("ConfigurationPropertyPage.button.up"));//$NON-NLS-1$
      moveUpButton.setToolTipText(XDocletRunMessages.getString("ConfigurationPropertyPage.button.up.tip"));//$NON-NLS-1$
      moveUpButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      moveDownButton = new Button(buttons, SWT.PUSH);
      moveDownButton.setText(XDocletRunMessages.getString("ConfigurationPropertyPage.button.down"));//$NON-NLS-1$
      moveDownButton.setToolTipText(XDocletRunMessages.getString("ConfigurationPropertyPage.button.down.tip"));//$NON-NLS-1$
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
         this.projectConfigurations = XDocletRunPlugin.getDefault().getProjectConfigurations(this.project);
         this.projectConfigurations.loadConfigurations();

         // The configuration list
         GridLayout layout = new GridLayout(1, false);
         parentComposite.setLayout(layout);

         enableXDocletButton = new Button(parentComposite, SWT.CHECK);
         enableXDocletButton.setText(XDocletRunMessages.getString("ConfigurationPropertyPage.enableXDocletLabel"));

         enableXDocletButton.addSelectionListener(new SelectionListener()
         {
            public void widgetDefaultSelected(SelectionEvent e)
            {
               widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e)
            {
               boolean enabled = enableXDocletButton.getSelection();
               UIUtil.setEnabledRecursive(parentComposite, enabled);
               enableXDocletButton.setEnabled(true);
               if (enabled)
               {
                  enableButtons();
               }
            }
         });

         boolean hasXDoclet = ProjectUtil.projectHasBuilder(this.project.getProject(), XDocletRunBuilder.BUILDER_ID);
         UIUtil.setEnabledRecursive(parentComposite, hasXDoclet);
         enableXDocletButton.setEnabled(true);
         enableXDocletButton.setSelection(hasXDoclet);

         Label description = new Label(parentComposite, SWT.NONE);
         description.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
         description.setText(XDocletRunMessages.getString("ConfigurationPropertyPage.description"));//$NON-NLS-1$

         // The configuration list
         Composite list = new Composite(parentComposite, SWT.NONE);
         layout = new GridLayout(2, false);
         layout.marginHeight = 0;
         layout.marginWidth = 0;
         list.setLayout(layout);
         list.setLayoutData(new GridData(GridData.FILL_BOTH));

         this.configListViewer = new ConfigurationListViewer(list, this.projectConfigurations.getConfigurations());
         createButtons(list);

         // The configuration detail
         Composite detail = new Composite(parentComposite, SWT.NONE);
         GridLayout detailLayout = new GridLayout(2, true);
         detailLayout.marginHeight = 0;
         detailLayout.marginWidth = 0;
         detail.setLayout(detailLayout);
         detail.setLayoutData(new GridData(GridData.FILL_BOTH));

         this.configViewer = new ConfigurationViewer(detail);
         this.properiesViewer = new ElementViewer(detail);

         assign();
         enableButtons();
      }
      catch (CoreException ce)
      {
         openErrorDialog(XDocletRunMessages.getString("ConfigurationPropertyPage.failed.load.configuration"), ce);//$NON-NLS-1$
      }
      return parentComposite;
   }

   /** Description of the Method */
   private void doMoveDown()
   {
      XDocletConfiguration data = this.configListViewer.getCurrent();
      if (data != null)
      {
         this.projectConfigurations.moveDown(data);
         this.configListViewer.refresh();
      }
   }

   /** Description of the Method */
   private void doMoveUp()
   {
      XDocletConfiguration data = this.configListViewer.getCurrent();
      if (data != null)
      {
         this.projectConfigurations.moveUp(data);
         this.configListViewer.refresh();
      }
   }

   /** Description of the Method */
   private void enableButtons()
   {
      XDocletConfiguration data = this.configListViewer.getCurrent();
      if (data != null)
      {
         this.renameButton.setEnabled(true);
         this.removeButton.setEnabled(true);
         this.moveUpButton.setEnabled(true);
         this.moveDownButton.setEnabled(true);
      }
      else
      {
         this.renameButton.setEnabled(false);
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
      IStatus status = new Status(IStatus.ERROR, "xdoclet.ide.eclipse", 0, msg, null);//$NON-NLS-1$
      if (e != null && e instanceof CoreException)
      {
         status = ((CoreException) e).getStatus();
      }
      ErrorDialog.openError(this.getShell(), msg, null, status);
   }

   public Button getButton (int buttonId)
   {
      switch (buttonId) {
         case BUTTON_ADD: return addButton;
         case BUTTON_ADD_STANDARD: return addStandardButton;
         case BUTTON_ENABLE_XDOCLET: return enableXDocletButton;
         case BUTTON_MOVE_DOWN: return moveDownButton;
         case BUTTON_MOVE_UP: return moveUpButton;
         case BUTTON_REMOVE: return removeButton;
         case BUTTON_RENAME: return renameButton;
      }
      return null;
   }
   
   
   public ConfigurationListViewer getConfigurationListViewer ()
   {
      return configListViewer;
   }
   
   public ConfigurationViewer getConfigurationViewer ()
   {
      return configViewer;
   }
   
   public ProjectConfigurations getProjectConfigurations()
   {
      return projectConfigurations;
   }
}
