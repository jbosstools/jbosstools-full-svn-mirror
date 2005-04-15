/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
