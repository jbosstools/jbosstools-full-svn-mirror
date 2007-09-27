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
package org.jboss.ide.eclipse.launcher.ui.configuration.jboss;

import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;
import org.jboss.ide.eclipse.launcher.core.constants.IServerLaunchConfigurationConstants;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIMessages;
import org.jboss.ide.eclipse.launcher.ui.configuration.JDKTab;
import org.jboss.ide.eclipse.launcher.ui.configuration.LogFileTab;
import org.jboss.ide.eclipse.launcher.ui.configuration.ServerLaunchArgumentsTab;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class JBoss32xTabGroup extends JBoss30xTabGroup
{
   /**
    * @param dialog  Description of the Parameter
    * @param mode    Description of the Parameter
    * @see           org.eclipse.debug.ui.ILaunchConfigurationTabGroup#createTabs(ILaunchConfigurationDialog, String)
    */
   public void createTabs(ILaunchConfigurationDialog dialog, String mode)
   {
      ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[]
      {
            new JBoss32xHomeTab(),
            new ServerLaunchArgumentsTab(IServerLaunchConfigurationConstants.ATTR_USER_PROGRAM_ARGS,
                  IServerLaunchConfigurationConstants.ATTR_USER_VM_ARGS, LauncherUIMessages
                        .getString("JBoss32xTabGroup.start.tab")), //$NON-NLS-1$
            new ServerLaunchArgumentsTab(IServerLaunchConfigurationConstants.ATTR_USER_SHUTDOWN_PROGRAM_ARGS,
                  IServerLaunchConfigurationConstants.ATTR_USER_SHUTDOWN_VM_ARGS, LauncherUIMessages
                        .getString("JBoss32xTabGroup.shutdown.tab")), //$NON-NLS-1$
            new LogFileTab(), new SourceLookupTab(), new JavaClasspathTab(), new JDKTab()};

      setTabs(tabs);
   }

   /**
    * Gets the shutdownProgramArgs attribute of the JBoss32xTabGroup object
    *
    * @return   The shutdownProgramArgs value
    */
   protected String getShutdownProgramArgs()
   {
      return "-S";//$NON-NLS-1$
   }
}
