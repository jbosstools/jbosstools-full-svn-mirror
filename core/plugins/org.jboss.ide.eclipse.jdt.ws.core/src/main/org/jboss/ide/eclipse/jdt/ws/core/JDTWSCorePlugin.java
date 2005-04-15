/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ws.core;

import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.core.classpath.ClassPathContainerRepository;
import org.jboss.ide.eclipse.jdt.test.core.JDTTestCorePlugin;
import org.jboss.ide.eclipse.jdt.ws.core.classpath.Axis12ClasspathContainer;
import org.jboss.ide.eclipse.jdt.ws.core.classpath.WSIBasicProfile10ClasspathContainer;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JDTWSCorePlugin extends AbstractPlugin
{

   /** The shared instance */
   private static JDTWSCorePlugin plugin;


   /** The constructor. */
   public JDTWSCorePlugin()
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

      ClassPathContainerRepository.getInstance().addClassPathEntry(WSIBasicProfile10ClasspathContainer.CLASSPATH_CONTAINER);
      ClassPathContainerRepository.getInstance().addClassPathEntry(Axis12ClasspathContainer.CLASSPATH_CONTAINER);

      // Force the plugin load
      JDTTestCorePlugin.getDefault().getBaseDir();
   }


   /**
    * Returns the shared instance.
    *
    * @return   The default value
    */
   public static JDTWSCorePlugin getDefault()
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
         return "org.jboss.ide.eclipse.jdt.ws.core";//$NON-NLS-1$
      }
      return getDefault().getBundle().getSymbolicName();
   }
}
