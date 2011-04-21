/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.xml.ui;

import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JDTJ2EEXMLUIPlugin extends AbstractPlugin
{
   /** The shared instance */
   private static JDTJ2EEXMLUIPlugin plugin;


   /** The constructor. */
   public JDTJ2EEXMLUIPlugin()
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
   public void start(BundleContext context)
      throws Exception
   {
      super.start(context);
   }


   /**
    * Description of the Method
    *
    * @param context        Description of the Parameter
    * @exception Exception  Description of the Exception
    */
   public void stop(BundleContext context)
      throws Exception
   {
      super.stop(context);
   }


   /**
    * Returns the shared instance.
    *
    * @return   The default value
    */
   public static JDTJ2EEXMLUIPlugin getDefault()
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
         return "org.jboss.ide.eclipse.jdt.j2ee.xml.ui";//$NON-NLS-1$
      }
      return getDefault().getBundle().getSymbolicName();
   }

}
