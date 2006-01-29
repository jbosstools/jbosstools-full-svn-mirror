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
package org.jboss.ide.eclipse.ejb3.wizards.ui.wizards.pages;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.jboss.ide.eclipse.ejb3.wizards.core.EJB3WizardsCorePlugin;
import org.jboss.ide.eclipse.launcher.core.ServerLaunchManager;
import org.jboss.ide.eclipse.launcher.ui.views.ServerNavigatorContentProvider;
import org.jboss.ide.eclipse.launcher.ui.views.ServerNavigatorLabelProvider;

public class JBossSelectionPage extends WizardPage
{

   protected TableViewer configurations;

   protected ILaunchConfiguration configuration;

   protected Button newConfiguration, editConfiguration;

   public JBossSelectionPage()
   {
      super("JBoss Server Selection");
   }

   public void createControl(Composite parent)
   {

      Composite main = new Composite(parent, SWT.NONE);
      main.setLayout(new GridLayout());

      Composite configurationComposite = new Composite(main, SWT.NONE);
      configurationComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

      configurationComposite.setLayout(new FillLayout());

      configurations = new TableViewer(configurationComposite);
      configurations.setContentProvider(new ServerNavigatorContentProvider());
      configurations.setLabelProvider(new ServerNavigatorLabelProvider());
      configurations.addSelectionChangedListener(new ISelectionChangedListener()
      {
         public void selectionChanged(SelectionChangedEvent event)
         {
            configurationSelected();
         }
      });

      refreshConfigurations();

      Composite links = new Composite(main, SWT.NONE);
      links.setLayout(new RowLayout());

      newConfiguration = new Button(links, SWT.NONE);
      newConfiguration.setText("Create a JBoss configuration");
      newConfiguration.addSelectionListener(new SelectionListener()
      {
         public void widgetDefaultSelected(SelectionEvent e)
         {
            widgetSelected(e);
         }

         public void widgetSelected(SelectionEvent e)
         {
            createJBossConfiguration();
         }
      });

      editConfiguration = new Button(links, SWT.NONE);
      editConfiguration.setText("Edit JBoss Configuration");
      editConfiguration.addSelectionListener(new SelectionListener()
      {
         public void widgetDefaultSelected(SelectionEvent e)
         {
            widgetSelected(e);
         }

         public void widgetSelected(SelectionEvent e)
         {
            editJBossConfiguration();
         }
      });

      editConfiguration.setEnabled(false);

      setControl(main);
   }

   private void refreshConfigurations()
   {
      try
      {
         configurations.setInput(ServerLaunchManager.getInstance().getServerConfigurations());
      }
      catch (CoreException e)
      {
         e.printStackTrace();
      }
   }

   private void createJBossConfiguration()
   {
      int status = DebugUITools.openLaunchConfigurationDialogOnGroup(DebugUIPlugin.getShell(),
            new StructuredSelection(), IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);

      refreshConfigurations();
   }

   private void editJBossConfiguration()
   {
      int status = DebugUITools.openLaunchConfigurationDialogOnGroup(DebugUIPlugin.getShell(), new StructuredSelection(
            configuration), IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);

      refreshConfigurations();
   }

   protected void configurationSelected()
   {
      IStructuredSelection selection = (IStructuredSelection) configurations.getSelection();
      configuration = (ILaunchConfiguration) selection.getFirstElement();

      getWizard().getContainer().updateButtons();
      EJB3WizardsCorePlugin.getDefault().setSelectedLaunchConfiguration(configuration);
      editConfiguration.setEnabled(true);
   }

   public ILaunchConfiguration getLaunchConfiguration()
   {
      return configuration;
   }
}
