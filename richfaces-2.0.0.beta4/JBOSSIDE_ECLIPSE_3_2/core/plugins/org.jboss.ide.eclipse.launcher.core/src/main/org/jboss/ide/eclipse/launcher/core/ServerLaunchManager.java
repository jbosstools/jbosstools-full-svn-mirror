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
package org.jboss.ide.eclipse.launcher.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.ui.DebugUITools;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.launcher.core.configuration.IServerLaunchConfigurationDelegate;
import org.jboss.ide.eclipse.launcher.core.constants.ILauncherConstants;
import org.jboss.ide.eclipse.launcher.core.constants.IServerLaunchConfigurationConstants;
import org.jboss.ide.eclipse.launcher.core.event.ServerDebugEventHandler;
import org.jboss.ide.eclipse.launcher.core.logfiles.LogFile;
import org.jboss.ide.eclipse.launcher.core.util.LaunchStatus;

/**
 * @author    Hans Dockter
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class ServerLaunchManager
{
   /** Description of the Field */
   protected ServerDebugEventHandler debugEventHandler = null;

   /** Description of the Field */
   protected ILaunchManager manager;

   /** Description of the Field */
   private static ServerLaunchManager instance = new ServerLaunchManager();

   /** Constructor for the ServerLaunchManager object */
   private ServerLaunchManager()
   {
   }

   /**
    * Gets the debugEventHandler attribute of the ServerLaunchManager object
    *
    * @return   The debugEventHandler value
    */
   public ServerDebugEventHandler getDebugEventHandler()
   {
      if (debugEventHandler == null)
      {
         debugEventHandler = new ServerDebugEventHandler();
         DebugPlugin.getDefault().addDebugEventListener(debugEventHandler);
         DebugPlugin.getDefault().getLaunchManager().addLaunchConfigurationListener(debugEventHandler);
      }
      return debugEventHandler;
   }

   /**
    * Method getDefaultLaunchConfiguration.
    *
    * @return   the default configuration or null if none exists
    */
   public ILaunchConfiguration getDefaultLaunchConfiguration()
   {
      String defaultServerMemento = LauncherPlugin.getDefault().getPreferenceStore().getString(
            ILauncherConstants.ATTR_DEFAULT_LAUNCH_CONFIGURATION_MEMENTO);
      if (defaultServerMemento.equals(IServerLaunchConfigurationConstants.EMPTY_STRING))
      {
         return null;
      }
      ILaunchConfiguration configuration = null;
      try
      {
         configuration = DebugPlugin.getDefault().getLaunchManager().getLaunchConfiguration(defaultServerMemento);
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
         LauncherPlugin.getDefault().showErrorMessage(e);
      }
      return configuration;
   }

   /**
    * Gets the lastStartProcess attribute of the ServerLaunchManager object
    *
    * @param configuration      Description of the Parameter
    * @return                   The lastStartProcess value
    * @exception CoreException  Description of the Exception
    */
   public IProcess getLastStartProcess(ILaunchConfiguration configuration) throws CoreException
   {
      ILaunch launch = getRunningStartLaunch(configuration);
      try
      {
         synchronized (launch)
         {
            return launch.getDebugTarget().getProcess();
         }
      }
      catch (NullPointerException ignore)
      {
      }
      return ServerLaunchManager.getInstance().getDebugEventHandler().getProcessOfLastTerminatedLaunch(configuration);
   }

   /**
    * Method getLogFiles.
    *
    * @param configuration
    * @return                LogFile[] the existing logfiles or an empty array if none exists
    *         or an exception occurs
    * @throws CoreException
    */
   public LogFile[] getLogFiles(ILaunchConfiguration configuration) throws CoreException
   {
      List logFiles = new ArrayList();
      logFiles.addAll(getDefaultLogFiles(configuration));
      logFiles.addAll(getUserLogFiles(configuration));
      return (LogFile[]) logFiles.toArray(new LogFile[logFiles.size()]);
   }

   /**
    * Method getLogFiles.
    *
    * @param configuration
    * @return                LogFile[] the existing logfiles or an empty array if none exists
    *         or an exception occurs
    * @throws CoreException
    */
   public List getUserLogFiles(ILaunchConfiguration configuration) throws CoreException
   {
      List logFiles = new ArrayList();
      if (configuration != null)
      {
         Map map = configuration
               .getAttribute(IServerLaunchConfigurationConstants.ATTR_LOG_FILES, Collections.EMPTY_MAP);
         String fileName;
         for (Iterator iter = map.keySet().iterator(); iter.hasNext();)
         {
            fileName = (String) iter.next();
            logFiles.add(new LogFile(configuration, fileName, Integer.parseInt((String) map.get(fileName))));
         }
      }
      return logFiles;
   }

   /**
    * Method getLogFiles.
    *
    * @param configuration
    * @return                LogFile[] the existing logfiles or an empty array if none exists
    *         or an exception occurs
    * @throws CoreException
    */
   public List getDefaultLogFiles(ILaunchConfiguration configuration) throws CoreException
   {
      List logFiles = new ArrayList();
      if (configuration != null)
      {
         Map map = configuration.getAttribute(IServerLaunchConfigurationConstants.ATTR_DEFAULT_LOG_FILES,
               Collections.EMPTY_MAP);
         String fileName;
         for (Iterator iter = map.keySet().iterator(); iter.hasNext();)
         {
            fileName = (String) iter.next();
            logFiles.add(new LogFile(configuration, fileName, Integer.parseInt((String) map.get(fileName))));
         }
      }
      return logFiles;
   }

   /**
    * Method getRunningStartLaunch.
    *
    * @param configuration
    * @return                    ILaunch the running launch or null if there are none
    * @exception DebugException  Description of the Exception
    */
   public ILaunch getRunningStartLaunch(ILaunchConfiguration configuration) throws DebugException
   {
      ILaunch[] launchs = manager.getLaunches();
      // it's not specified whether null is a valid return-value so better
      // check for it
      if (launchs == null)
      {
         return null;
      }
      for (int i = 0; i < launchs.length; i++)
      {
         if (launchs[i].getLaunchConfiguration().equals(configuration)
               && (launchs[i].getAttribute(IServerLaunchConfigurationConstants.ATTR_LAUNCH_SHUTDOWN) == null)
               && (getStatus(launchs[i]) == LaunchStatus.RUNNING))
         {
            return launchs[i];
         }
      }
      return null;
   }

   /**
    * Gets the serverConfigurations attribute of the ServerLaunchManager object
    *
    * @return                   The serverConfigurations value
    * @exception CoreException  Description of the Exception
    */
   public ILaunchConfiguration[] getServerConfigurations() throws CoreException
   {
      ILaunchConfiguration[] configurations = manager.getLaunchConfigurations();
      ArrayList serverConfigurations = new ArrayList();
      for (int i = 0; i < configurations.length; i++)
      {
         if (isServerConfiguration(configurations[i]))
         {
            serverConfigurations.add(configurations[i]);
         }
      }
      return (ILaunchConfiguration[]) serverConfigurations
            .toArray(new ILaunchConfiguration[serverConfigurations.size()]);
   }

   /**
    * Gets the status attribute of the ServerLaunchManager object
    *
    * @param launch              Description of the Parameter
    * @return                    The status value
    * @exception DebugException  Description of the Exception
    */
   public LaunchStatus getStatus(ILaunch launch) throws DebugException
   {
      if (launch == null || launch.getDebugTarget() == null || !launch.getDebugTarget().hasThreads())
      {
         return LaunchStatus.NOT_RUNNING;
      }
      return LaunchStatus.RUNNING;
   }

   /**
    * Gets the statusForStartLaunch attribute of the ServerLaunchManager object
    *
    * @param configuration       Description of the Parameter
    * @return                    The statusForStartLaunch value
    * @exception DebugException  Description of the Exception
    */
   public LaunchStatus getStatusForStartLaunch(ILaunchConfiguration configuration) throws DebugException
   {
      return getStatus(getRunningStartLaunch(configuration));
   }

   /**
    * Description of the Method
    *
    * @param configuration      Description of the Parameter
    * @return                   Description of the Return Value
    * @exception CoreException  Description of the Exception
    */
   public boolean hasShutdown(ILaunchConfiguration configuration) throws CoreException
   {
      return ((IServerLaunchConfigurationDelegate) configuration.getType().getDelegate(ILaunchManager.DEBUG_MODE))
            .hasShutdown();
   }

   /**
    * Gets the serverConfiguration attribute of the ServerLaunchManager object
    *
    * @param configuration      Description of the Parameter
    * @return                   The serverConfiguration value
    * @exception CoreException  Description of the Exception
    */
   public boolean isServerConfiguration(ILaunchConfiguration configuration) throws CoreException
   {
      // The prefered perspective for each mode is hold
      // by a private configuration
      if (!DebugUITools.isPrivate(configuration)
            && configuration.getType().supportsMode(ILaunchManager.DEBUG_MODE)
            && configuration.getType().getDelegate(ILaunchManager.DEBUG_MODE) instanceof IServerLaunchConfigurationDelegate)
      {
         return true;
      }
      return false;
   }

   /**
    * Gets the valid attribute of the ServerLaunchManager object
    *
    * @param configuration      Description of the Parameter
    * @return                   The valid value
    * @exception CoreException  Description of the Exception
    */
   public boolean isValid(ILaunchConfiguration configuration) throws CoreException
   {
      return !configuration.getAttribute(IServerLaunchConfigurationConstants.ATTR_CONFIGURATION_ERROR, true);
   }

   /**
    * Sets the launchManager attribute of the ServerLaunchManager object
    *
    * @param manager  The new launchManager value
    */
   public void setLaunchManager(ILaunchManager manager)
   {
      this.manager = manager;
   }

   /**
    * Description of the Method
    *
    * @param configuration      Description of the Parameter
    * @exception CoreException  Description of the Exception
    */
   public void shutDown(ILaunchConfiguration configuration) throws CoreException
   {
      ((IServerLaunchConfigurationDelegate) configuration.getType().getDelegate(ILaunchManager.DEBUG_MODE)).shutdown(
            configuration, null);
   }

   /**
    * Description of the Method
    *
    * @param configuration      Description of the Parameter
    * @exception CoreException  Description of the Exception
    */
   public void start(ILaunchConfiguration configuration) throws CoreException
   {
      if (configuration == null)
      {
         return;
      }
      configuration.launch(ILaunchManager.DEBUG_MODE, null);
   }

   /**
    * Description of the Method
    *
    * @param configuration      Description of the Parameter
    * @exception CoreException  Description of the Exception
    */
   public void terminate(ILaunchConfiguration configuration) throws CoreException
   {
      if (configuration == null)
      {
         return;
      }
      ILaunch launch = ServerLaunchManager.getInstance().getRunningStartLaunch(configuration);
      if (launch != null)
      {
         launch.terminate();
      }
   }

   /**
    * Gets the instance attribute of the ServerLaunchManager class
    *
    * @return   The instance value
    */
   public static ServerLaunchManager getInstance()
   {
      return instance;
   }
}
