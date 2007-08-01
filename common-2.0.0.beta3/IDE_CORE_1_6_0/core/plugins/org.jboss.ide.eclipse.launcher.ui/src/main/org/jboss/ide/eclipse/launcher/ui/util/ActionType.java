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
package org.jboss.ide.eclipse.launcher.ui.util;

import org.jboss.ide.eclipse.core.util.NamedType;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIMessages;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class ActionType extends NamedType
{
   /** Description of the Field */
   public final static ActionType SHUTDOWN = new ActionType(LauncherUIMessages.getString("ActionType.shutdown_2"));//$NON-NLS-1$

   /** Description of the Field */
   public final static ActionType START = new ActionType(LauncherUIMessages.getString("ActionType.start_1"));//$NON-NLS-1$

   /** Description of the Field */
   public final static ActionType TERMINATE = new ActionType(LauncherUIMessages.getString("ActionType.terminate_3"));//$NON-NLS-1$

   /**
    *Constructor for the ActionType object
    *
    * @param name  Description of the Parameter
    */
   private ActionType(String name)
   {
      super(name);
   }
}
