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
package org.jboss.ide.eclipse.launcher.ui.actions;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jboss.ide.eclipse.launcher.core.LauncherPlugin;
import org.jboss.ide.eclipse.launcher.core.ServerLaunchManager;
import org.jboss.ide.eclipse.launcher.core.event.IServerDebugEventListener;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public abstract class AbstractDefaultServerActionDelegate
      implements
         IWorkbenchWindowActionDelegate,
         IServerDebugEventListener,
         ILaunchConfigurationListener,
         IPropertyChangeListener
{
   IAction action;

   /**
    * We can use this method to dispose of any system
    * resources we previously allocated.
    *
    * @see   IWorkbenchWindowActionDelegate#dispose
    */
   public void dispose()
   {
      ServerLaunchManager.getInstance().getDebugEventHandler().removeListener(this);
   }

   /**
    * We will cache window object in order to
    * be able to provide parent shell for the message dialog.
    *
    * @param window  Description of the Parameter
    * @see           IWorkbenchWindowActionDelegate#init
    */
   public void init(IWorkbenchWindow window)
   {
      ServerLaunchManager.getInstance().getDebugEventHandler().addListener(this);
      DebugPlugin.getDefault().getLaunchManager().addLaunchConfigurationListener(this);
      LauncherPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);
   }

   /**
    * @param configuration  Description of the Parameter
    * @see                  org.eclipse.debug.core.ILaunchConfigurationListener#launchConfigurationAdded(ILaunchConfiguration)
    */
   public void launchConfigurationAdded(ILaunchConfiguration configuration)
   {
   }

   /**
    * @param configuration  Description of the Parameter
    * @see                  org.eclipse.debug.core.ILaunchConfigurationListener#launchConfigurationChanged(ILaunchConfiguration)
    */
   public void launchConfigurationChanged(ILaunchConfiguration configuration)
   {
      enableAction(ServerLaunchManager.getInstance().getDefaultLaunchConfiguration(), action);
   }

   /**
    * @param configuration  Description of the Parameter
    * @see                  org.eclipse.debug.core.ILaunchConfigurationListener#launchConfigurationRemoved(ILaunchConfiguration)
    */
   public void launchConfigurationRemoved(ILaunchConfiguration configuration)
   {
      enableAction(ServerLaunchManager.getInstance().getDefaultLaunchConfiguration(), action);
   }

   /**
    * @param event  Description of the Parameter
    * @see          org.eclipse.jface.util.IPropertyChangeListener#propertyChange(PropertyChangeEvent)
    */
   public void propertyChange(PropertyChangeEvent event)
   {
      enableAction(ServerLaunchManager.getInstance().getDefaultLaunchConfiguration(), action);
   }

   /**
    * @param action     Description of the Parameter
    * @param selection  Description of the Parameter
    * @see              org.eclipse.ui.IActionDelegate#selectionChanged(IAction, ISelection)
    */
   public void selectionChanged(IAction action, ISelection selection)
   {
      this.action = action;
      enableAction(ServerLaunchManager.getInstance().getDefaultLaunchConfiguration(), action);
   }

   /**
    * @param event          Description of the Parameter
    * @param configuration  Description of the Parameter
    * @see                  org.rocklet.launcher.ui.IServerDebugEventListener#serverEvent(DebugEvent, ILaunchConfiguration)
    */
   public void serverEvent(DebugEvent event, ILaunchConfiguration configuration)
   {
      enableAction(configuration, action);
   }

   /**
    * Description of the Method
    *
    * @param configuration  Description of the Parameter
    * @param action         Description of the Parameter
    */
   protected abstract void enableAction(ILaunchConfiguration configuration, IAction action);
}
