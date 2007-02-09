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
package org.jboss.ide.eclipse.ejb3.ui.wizards.pages;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.ui.ServerUICore;
import org.eclipse.wst.server.ui.internal.ServerUIPlugin;
import org.eclipse.wst.server.ui.internal.actions.NewServerAction;
import org.jboss.ide.eclipse.as.core.server.JBossServer;
import org.jboss.ide.eclipse.ejb3.core.EJB3WizardsCorePlugin;

public class JBossSelectionPage extends WizardPage
{

   protected TableViewer servers;

   protected JBossServer jbossServer;

   protected Button newServer;

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

      servers = new TableViewer(configurationComposite);
      
      servers.setContentProvider(new ArrayContentProvider());
      servers.setLabelProvider(ServerUICore.getLabelProvider());
      servers.addSelectionChangedListener(new ISelectionChangedListener()
      {
         public void selectionChanged(SelectionChangedEvent event)
         {
            serverSelected();
         }
      });

      refreshConfigurations();

      Composite links = new Composite(main, SWT.NONE);
      links.setLayout(new RowLayout());

      newServer = new Button(links, SWT.NONE);
      newServer.setText("Create a JBoss Server");
      newServer.addSelectionListener(new SelectionListener()
      {
         public void widgetDefaultSelected(SelectionEvent e)
         {
            widgetSelected(e);
         }

         public void widgetSelected(SelectionEvent e)
         {
            createJBossServer();
         }
      });

//      editServer = new Button(links, SWT.NONE);
//      editServer.setText("Edit JBoss Server");
//      editServer.addSelectionListener(new SelectionListener()
//      {
//         public void widgetDefaultSelected(SelectionEvent e)
//         {
//            widgetSelected(e);
//         }
//
//         public void widgetSelected(SelectionEvent e)
//         {
//            editJBossServer();
//         }
//      });

//      editServer.setEnabled(false);

      setControl(main);
   }

   private void refreshConfigurations()
   {
	   servers.setInput(ServerCore.getServers());
   }

   private void createJBossServer()
   {
	   NewServerAction action = new NewServerAction();
	   action.init(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
	   action.run(null);
	   
//      int status = DebugUITools.openLaunchConfigurationDialogOnGroup(DebugUIPlugin.getShell(),
//            new StructuredSelection(), IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);

      refreshConfigurations();
   }

   private void editJBossServer()
   {
	   
	   ServerUIPlugin.editServer(jbossServer.getServer());
//      int status = DebugUITools.openLaunchConfigurationDialogOnGroup(DebugUIPlugin.getShell(), new StructuredSelection(
//            configuration), IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);

      refreshConfigurations();
   }

   protected void serverSelected()
   {
      IStructuredSelection selection = (IStructuredSelection) servers.getSelection();
      IServer server = (IServer) selection.getFirstElement();
      jbossServer = (JBossServer) server.getAdapter(JBossServer.class);
      
      getWizard().getContainer().updateButtons();
      EJB3WizardsCorePlugin.getDefault().setSelectedServer(jbossServer);
//      editServer.setEnabled(true);
   }

   public JBossServer getServer()
   {
      return jbossServer;
   }
}
