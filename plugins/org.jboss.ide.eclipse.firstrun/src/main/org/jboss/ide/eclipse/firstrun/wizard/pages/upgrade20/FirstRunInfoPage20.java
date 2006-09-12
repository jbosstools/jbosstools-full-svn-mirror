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
package org.jboss.ide.eclipse.firstrun.wizard.pages.upgrade20;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jboss.ide.eclipse.firstrun.FirstRunMessages;
import org.jboss.ide.eclipse.firstrun.FirstRunPlugin;
import org.jboss.ide.eclipse.firstrun.wizard.pages.AbstractFirstRunPage;

public class FirstRunInfoPage20 extends AbstractFirstRunPage {

   public FirstRunInfoPage20()
   {
      super(FirstRunMessages.getString("InfoPage20.title"), FirstRunMessages.getString("FirstRunWizard.title"),
            FirstRunPlugin.getImageDescriptor(FirstRunPlugin.ICON_JBOSSIDE_LOGO));
   }

   public void createControl(Composite parent)
   {
      setTitle(FirstRunMessages.getString("InfoPage20.title"));

      Composite main = new Composite(parent, SWT.NONE);
      main.setLayout(new FillLayout());

      Label label = new Label(main, SWT.WRAP);

      label.setText(FirstRunMessages.getString("InfoPage20.info"));

      setControl(main);
   }

   public void initialize() {
   }

   public void performFinish() {
   }

}
