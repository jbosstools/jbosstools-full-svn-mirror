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
package org.jboss.ide.eclipse.launcher.core.event;

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.swt.widgets.Display;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.launcher.core.ServerLaunchManager;
import org.jboss.ide.eclipse.launcher.core.constants.IServerLaunchConfigurationConstants;

/**
 * This class informs about life cycle events of a server launch and keeps a
 * list of which process was terminated last for a particular
 * LaunchConfiguration.
 *
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class ServerDebugEventHandler implements IDebugEventSetListener, ILaunchConfigurationListener
{
   /** Description of the Field */
   protected ListenerList listenerList = new ListenerList();

   private HashMap lastTerminatedProcesses = new HashMap();

   /**
    * Adds a feature to the Listener attribute of the ServerDebugEventHandler
    * object
    *
    * @param l  The feature to be added to the Listener attribute
    */
   public void addListener(IServerDebugEventListener l)
   {
      listenerList.add(l);
   }

   /**
    * Method getLastTerminatedLaunch.
    *
    * @param configuration
    * @return               the process of the last terminated launch or null if no such
    *         process exists or the configuration is null
    */
   public IProcess getProcessOfLastTerminatedLaunch(ILaunchConfiguration configuration)
   {
      if (configuration == null)
      {
         return null;
      }
      return (IProcess) lastTerminatedProcesses.get(configuration.getName());
   }

   /**
    * @param events  Description of the Parameter
    * @see           org.eclipse.debug.core.IDebugEventSetListener#handleDebugEvents(DebugEvent[])
    */
   public void handleDebugEvents(final DebugEvent[] events)
   {
      Runnable r = new Runnable()
      {
         public void run()
         {
            IDebugTarget target;
            for (int i = 0; i < events.length; i++)
            {
               DebugEvent event = events[i];
               // All our launches launch a debug-target. This is the parent
               // for other elements
               // like threads. We are only interested in events concerning the
               // whole target.
               // In our case a launch has zero ore one debug-targets.
               // The create-event is handled by the launch-listener as it
               // differentiates beetween
               // a new launch and an existing launch where a debug-target has
               // been started.
               // For some reason only starting can be listened to via the
               // launch-listener.
               // So the other events have to be handled by the
               // DebugEventListener
               if (event.getSource() instanceof IDebugTarget)
               {
                  target = (IDebugTarget) event.getSource();
                  ILaunch launch = target.getLaunch();
                  try
                  {
                     if (!ServerLaunchManager.getInstance().isServerConfiguration(launch.getLaunchConfiguration())
                           || launch.getAttribute(IServerLaunchConfigurationConstants.ATTR_LAUNCH_SHUTDOWN) != null)
                     {
                        return;
                     }
                  }
                  catch (CoreException e)
                  {
                     AbstractPlugin.log(e);
                  }
                  switch (event.getKind())
                  {
                     case DebugEvent.TERMINATE :
                        lastTerminatedProcesses.put(launch.getLaunchConfiguration().getName(), target.getProcess());
                        break;
                  }
                  Object[] listeners = listenerList.getListeners();
                  for (int j = 0; j < listeners.length; j++)
                  {
                     ((IServerDebugEventListener) listeners[j]).serverEvent(event, launch.getLaunchConfiguration());
                  }
               }
            }
         }
      };
      Display.getDefault().asyncExec(r);
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
   }

   /**
    * @param configuration  Description of the Parameter
    * @see                  org.eclipse.debug.core.ILaunchConfigurationListener#launchConfigurationRemoved(ILaunchConfiguration)
    */
   public void launchConfigurationRemoved(ILaunchConfiguration configuration)
   {
      lastTerminatedProcesses.put(configuration.getName(), null);
   }

   /**
    * Description of the Method
    *
    * @param l  Description of the Parameter
    */
   public void removeListener(IServerDebugEventListener l)
   {
      listenerList.remove(l);
   }
}
