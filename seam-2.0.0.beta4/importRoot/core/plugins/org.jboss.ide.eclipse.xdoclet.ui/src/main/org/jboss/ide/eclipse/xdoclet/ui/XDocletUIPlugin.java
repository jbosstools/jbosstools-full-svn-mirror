/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.ui;

import org.jboss.ide.eclipse.core.AbstractPlugin;

/**
 * Plugin dedicated to the UI management for XDoclet plugins.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @created   24 juin 2003
 */
public class XDocletUIPlugin extends AbstractPlugin
{
   /** The shared instance */
   private static XDocletUIPlugin plugin;


   /** The constructor. */
   public XDocletUIPlugin()
   {
      plugin = this;
   }


   /**
    * Returns the shared instance.
    *
    * @return   The shared instance
    */
   public static XDocletUIPlugin getDefault()
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
         return "org.jboss.ide.eclipse.xdoclet.ui";//$NON-NLS-1$
      }
      return getDefault().getBundle().getSymbolicName();
   }
}
