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
package org.jboss.ide.eclipse.xdoclet.core.ui;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.ide.eclipse.xdoclet.core.XDocletCoreMessages;
import org.jboss.ide.eclipse.xdoclet.core.XDocletCorePlugin;

/**
 * Preference page for XDoclet Core plugin. Allows to refresh the modules list.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @created   24 mars 2003
 */
public class MainPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
   private Button version32Button;

   private Button version40Button;

   /** Default constructor */
   public MainPreferencePage()
   {
      super();
      //      this.setTitle(XDocletCoreMessages.getString("MainPreferencePage.title"));//$NON-NLS-1$
      this.setDescription(XDocletCoreMessages.getString("MainPreferencePage.description"));//$NON-NLS-1$
   }

   /**
    * Initialization of the apge
    *
    * @param workbench  Workbench of the page
    * @see              org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
    */
   public void init(IWorkbench workbench)
   {
   }

   /**
    * Creates the contents of the preference page
    *
    * @param parent  Parent control to nest the page
    * @return        A custom control with the content of the page
    * @see           org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
    */
   protected Control createContents(Composite parent)
   {
      Composite top = new Composite(parent, SWT.NONE);
      top.setLayout(new GridLayout(1, true));

      Group group = new Group(top, SWT.NONE);
      group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      group.setText(XDocletCoreMessages.getString("MainPreferencePage.group.jboss.net.version"));//$NON-NLS-1$
      group.setLayout(new GridLayout(1, true));

      this.version40Button = new Button(group, SWT.RADIO);
      this.version40Button.setText(XDocletCoreMessages.getString("MainPreferencePage.button.version40"));//$NON-NLS-1$
      this.version32Button = new Button(group, SWT.RADIO);
      this.version32Button.setText(XDocletCoreMessages.getString("MainPreferencePage.button.version32"));//$NON-NLS-1$

      Button refreshButton = new Button(top, SWT.CENTER);
      refreshButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      refreshButton.setText(XDocletCoreMessages.getString("MainPreferencePage.button.refresh.modules"));//$NON-NLS-1$
      refreshButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            XDocletCorePlugin.getDefault().refreshModules();
         }
      });

      setDefaults();

      return top;
   }

   public boolean performOk()
   {
      String version = this.version32Button.getSelection()
            ? XDocletCorePlugin.JBOSS_NET_VERSION_3_2
            : XDocletCorePlugin.JBOSS_NET_VERSION_4_0;

      XDocletCorePlugin plugin = XDocletCorePlugin.getDefault();
      if (!plugin.getJBossNetVersion().equals(version))
      {
         plugin.setJBossNetVersion(version);
         plugin.refreshModules();
      }

      return super.performOk();
   }

   protected void performDefaults()
   {
      setDefaults();
      super.performDefaults();
   }

   private void setDefaults()
   {
      String version = XDocletCorePlugin.getDefault().getJBossNetVersion();
      this.version32Button.setSelection(XDocletCorePlugin.JBOSS_NET_VERSION_3_2.equals(version));
      this.version40Button.setSelection(XDocletCorePlugin.JBOSS_NET_VERSION_4_0.equals(version));
   }
}
