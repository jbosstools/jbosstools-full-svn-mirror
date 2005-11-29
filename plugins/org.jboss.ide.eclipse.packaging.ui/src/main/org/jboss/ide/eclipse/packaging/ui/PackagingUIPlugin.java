/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.packaging.ui;

import org.jboss.ide.eclipse.core.AbstractPlugin;

/**
 * The main plugin class to be used in the desktop.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class PackagingUIPlugin extends AbstractPlugin
{
   /** The shared instance. */
   private static PackagingUIPlugin plugin;


   /** The constructor. */
   public PackagingUIPlugin()
   {
      plugin = this;
   }


   /**
    * Returns the shared instance.
    *
    * @return   The default value
    */
   public static PackagingUIPlugin getDefault()
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
         return "org.jboss.ide.eclipse.packaging.ui";//$NON-NLS-1$
      }
      return getDefault().getBundle().getSymbolicName();
   }
}
