/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ws.ui;

import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.ws.core.JDTWSCorePlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JDTWSUIPlugin extends AbstractPlugin
{
   /** The shared instance */
   private static JDTWSUIPlugin plugin;


   /** The constructor. */
   public JDTWSUIPlugin()
   {
      super();
      plugin = this;
   }


   /**
    * Description of the Method
    *
    * @param context        Description of the Parameter
    * @exception Exception  Description of the Exception
    */
   public void start(BundleContext context) throws Exception
   {
      super.start(context);

      // Force the plugin load
      JDTWSCorePlugin.getDefault().getBaseDir();
   }


   /**
    * Returns the shared instance.
    *
    * @return   The default value
    */
   public static JDTWSUIPlugin getDefault()
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
         return "org.jboss.ide.eclipse.jdt.ws.ui";//$NON-NLS-1$
      }
      return getDefault().getBundle().getSymbolicName();
   }
}
