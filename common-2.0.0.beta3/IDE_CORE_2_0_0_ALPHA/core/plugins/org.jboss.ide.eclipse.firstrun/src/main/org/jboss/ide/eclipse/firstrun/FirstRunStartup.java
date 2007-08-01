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
package org.jboss.ide.eclipse.firstrun;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.jboss.ide.eclipse.firstrun.wizard.FirstRunWizard;
import org.jboss.ide.eclipse.packaging.core.PackagingCorePlugin;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunPlugin;

public class FirstRunStartup implements IStartup
{

   public void earlyStartup()
   {

      try
      {
         Display.getDefault().asyncExec(new Runnable()
         {
            public void run()
            {
               try
               {
                  // force initialization
                  PackagingCorePlugin.getDefault();
                  XDocletRunPlugin.getDefault();

                  IPreferenceStore store = FirstRunPlugin.getDefault().getPreferenceStore();

                  if (!store.contains(FirstRunPlugin.FIRST_RUN_PROPERTY)
                        || !store.getBoolean(FirstRunPlugin.FIRST_RUN_PROPERTY))
                  {
                     FirstRunWizard wizard = new FirstRunWizard();
                     WizardDialog dialog = new WizardDialog(null, wizard);

                     dialog.open();

                     // TODO UNCOMMENT ME
                     store.setValue(FirstRunPlugin.FIRST_RUN_PROPERTY, true);
                  }
               }
               catch (Exception e)
               {
                  e.printStackTrace();
               }
            }
         });
      }
      catch (Exception e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

}
