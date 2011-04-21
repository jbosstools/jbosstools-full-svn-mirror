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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.action.IAction;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.launcher.core.LauncherPlugin;
import org.jboss.ide.eclipse.launcher.core.ServerLaunchManager;
import org.jboss.ide.eclipse.launcher.ui.util.ServerLaunchUIUtil;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class ShutdownDefaultServerActionDelegate extends AbstractDefaultServerActionDelegate
{

   /**
    * @param action  Description of the Parameter
    * @see           IWorkbenchWindowActionDelegate#run
    */
   public void run(IAction action)
   {
      try
      {
         ServerLaunchManager.getInstance().shutDown(ServerLaunchManager.getInstance().getDefaultLaunchConfiguration());
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
         LauncherPlugin.getDefault().showErrorMessage(e.getMessage());
      }
   }

   /**
    * Description of the Method
    *
    * @param configuration  Description of the Parameter
    * @param action         Description of the Parameter
    */
   protected void enableAction(ILaunchConfiguration configuration, IAction action)
   {
      try
      {
         ServerLaunchUIUtil.enableServerActions(configuration, null, action, null);
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
         LauncherPlugin.getDefault().showErrorMessage(e);
      }
   }
}
