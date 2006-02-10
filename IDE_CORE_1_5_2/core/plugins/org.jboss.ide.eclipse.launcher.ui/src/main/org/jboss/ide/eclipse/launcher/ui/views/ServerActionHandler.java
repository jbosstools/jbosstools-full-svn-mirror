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
package org.jboss.ide.eclipse.launcher.ui.views;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.action.IAction;
import org.jboss.ide.eclipse.launcher.core.event.IServerDebugEventListener;
import org.jboss.ide.eclipse.launcher.ui.util.ActionType;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class ServerActionHandler implements IServerDebugEventListener
{
   /** Description of the Field */
   protected ArrayList shutdownActions = new ArrayList();

   /** Description of the Field */
   protected ArrayList startActions = new ArrayList();

   /** Description of the Field */
   protected ArrayList terminateActions = new ArrayList();

   /**
    * Adds a feature to the Action attribute of the ServerActionHandler object
    *
    * @param action  The feature to be added to the Action attribute
    * @param type    The feature to be added to the Action attribute
    */
   public void addAction(IAction action, ActionType type)
   {
      if (type == ActionType.START)
      {
         startActions.add(action);
      }
      else if (type == ActionType.SHUTDOWN)
      {
         shutdownActions.add(action);
      }
      else if (type == ActionType.TERMINATE)
      {
         terminateActions.add(action);
      }
   }

   /**
    * Description of the Method
    *
    * @param action  Description of the Parameter
    * @param type    Description of the Parameter
    */
   public void remove(IAction action, ActionType type)
   {
      if (type == ActionType.START)
      {
         startActions.remove(action);
      }
      else if (type == ActionType.SHUTDOWN)
      {
         shutdownActions.remove(action);
      }
      else if (type == ActionType.TERMINATE)
      {
         terminateActions.remove(action);
      }
   }

   /**
    * @param event          Description of the Parameter
    * @param configuration  Description of the Parameter
    * @see                  org.rocklet.launcher.ui.IServerDebugEventListener#serverEvent(DebugEvent, ILaunchConfiguration)
    */
   public void serverEvent(DebugEvent event, ILaunchConfiguration configuration)
   {
      switch (event.getKind())
      {
         case DebugEvent.CREATE :
            setEnablement(startActions, false);
            setEnablement(shutdownActions, true);
            setEnablement(terminateActions, true);
            break;
         case DebugEvent.TERMINATE :
            setEnablement(startActions, true);
            setEnablement(shutdownActions, false);
            setEnablement(terminateActions, false);
         default :
            break;
      }
   }

   /**
    * Sets the enablement attribute of the ServerActionHandler object
    *
    * @param actions     The new enablement value
    * @param enablement  The new enablement value
    */
   public void setEnablement(ArrayList actions, boolean enablement)
   {
      for (Iterator iter = actions.iterator(); iter.hasNext();)
      {
         ((IAction) iter.next()).setEnabled(enablement);
      }
   }

}
