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

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.launcher.core.constants.IJBossConstants;
import org.jboss.ide.eclipse.launcher.core.constants.IServerLaunchConfigurationConstants;
import org.jboss.ide.eclipse.launcher.core.util.ServerLaunchUtil;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIMessages;
import org.jboss.ide.eclipse.launcher.ui.configuration.HomeTab;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class JBoss30xHomeTab extends HomeTab
{
   /** Constructor for JBoss3xHomeTab */
   public JBoss30xHomeTab()
   {
      this(LauncherUIMessages.getString("JBoss30xHomeTab.JBoss_3.0.x_Home_Directory_1"));//$NON-NLS-1$
   }

   /**
    *Constructor for the JBoss30xHomeTab object
    *
    * @param title  Description of the Parameter
    */
   public JBoss30xHomeTab(String title)
   {
      super(title);
   }

   /**
    * @param launchConfig  Description of the Parameter
    * @return              The valid value
    * @see                 org.eclipse.debug.ui.ILaunchConfigurationTab#isValid(ILaunchConfiguration)
    */
   public boolean isValid(ILaunchConfiguration launchConfig)
   {
      setErrorMessage(null);
      setMessage(null);
      try
      {
         List invalidEntries = ServerLaunchUtil.getInvalidEntries(launchConfig, homedirText.getText(),
               IServerLaunchConfigurationConstants.ATTR_RELATIVE_TO_HOMEDIR_CLASSPATH);

         if (invalidEntries.size() > 0)
         {
            setErrorMessage(getMissingEntryErrorMessage((String) invalidEntries.get(0)));
            return false;
         }

         invalidEntries = ServerLaunchUtil.getInvalidEntries(launchConfig, homedirText.getText(),
               IServerLaunchConfigurationConstants.ATTR_RELATIVE_TO_HOMEDIR_SHUTDOWN_CLASSPATH);

         if (invalidEntries.size() > 0)
         {
            setErrorMessage(getMissingEntryErrorMessage((String) invalidEntries.get(0)));
            return false;
         }
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
      }
      return true;
   }

   protected String getMissingEntryErrorMessage(String entry)
   {
      if (entry.equals(IJBossConstants.RUN_JAR_RELATIVE_TO_JBOSS_HOME))
      {
         return LauncherUIMessages.getString("JBoss30xHomeTab.Missing_Run_Jar");
      }
      else if (entry.equals(IJBossConstants.SHUTDOWN_JAR_RELATIVE_TO_JBOSS_HOME_3X))
      {
         return LauncherUIMessages.getString("JBoss30xHomeTab.Missing_Shutdown_Jar");
      }

      return null;
   }
}
