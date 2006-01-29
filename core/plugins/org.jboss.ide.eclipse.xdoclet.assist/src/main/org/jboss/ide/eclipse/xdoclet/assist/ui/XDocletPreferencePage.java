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
package org.jboss.ide.eclipse.xdoclet.assist.ui;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.ide.eclipse.xdoclet.assist.XDocletAssistMessages;
import org.jboss.ide.eclipse.xdoclet.assist.XDocletAssistPlugin;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class XDocletPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
   /**Constructor for the XDocletPreferencePage object */
   public XDocletPreferencePage()
   {
      super();
      this.setDescription(XDocletAssistMessages
            .getString("XDocletPreferencePage.Allows_to_refresh_the_XTags_from_the_XDoclet_modules_1"));//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param workbench  Description of the Parameter
    */
   public void init(IWorkbench workbench)
   {
   }

   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    * @return        Description of the Return Value
    */
   protected Control createContents(Composite parent)
   {
      Composite top = new Composite(parent, SWT.NONE);
      top.setLayout(new GridLayout(1, true));

      Button button = new Button(top, SWT.CENTER);
      button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      button.setText(XDocletAssistMessages.getString("XDocletPreferencePage.Refresh_XDoclet_Data_1"));//$NON-NLS-1$
      button.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            XDocletAssistPlugin.getDefault().getRefreshedDocletTree();
         }
      });

      return top;
   }
}
