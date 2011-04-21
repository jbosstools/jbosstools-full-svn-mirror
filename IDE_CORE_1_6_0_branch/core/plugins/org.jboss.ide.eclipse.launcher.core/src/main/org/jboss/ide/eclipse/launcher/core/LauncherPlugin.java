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

import org.eclipse.debug.core.DebugPlugin;
import org.jboss.ide.eclipse.core.AbstractPlugin;

/**
 * The main plugin class
 *
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class LauncherPlugin extends AbstractPlugin
{
   /** The shared instance */
   private static LauncherPlugin plugin;

   /** The constructor. */
   public LauncherPlugin()
   {
      plugin = this;
      ServerLaunchManager.getInstance().setLaunchManager(DebugPlugin.getDefault().getLaunchManager());
   }

   /**
    * Returns the shared instance.
    *
    * @return   The default value
    */
   public static LauncherPlugin getDefault()
   {
      return plugin;
   }

   /**
    * Convenience method which returns the unique identifier of this plugin.
    *
    * @return   The unique indentifier value
    */
   public static String getUniqueIdentifier()
   {
      if (getDefault() == null)
      {
         // If the default instance is not yet initialized,
         // return a static identifier. This identifier must
         // match the plugin id defined in plugin.xml
         return "org.jboss.ide.eclipse.launcher.core";//$NON-NLS-1$
      }
      return getDefault().getBundle().getSymbolicName();
   }
}
