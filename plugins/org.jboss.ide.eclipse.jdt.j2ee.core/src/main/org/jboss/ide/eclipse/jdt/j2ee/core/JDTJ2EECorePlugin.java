/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.core;

import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.core.classpath.ClassPathContainerRepository;
import org.jboss.ide.eclipse.jdt.j2ee.core.classpath.J2EE13ClasspathContainer;
import org.jboss.ide.eclipse.jdt.j2ee.core.classpath.J2EE14ClasspathContainer;
import org.jboss.ide.eclipse.jdt.test.core.JDTTestCorePlugin;
import org.jboss.ide.eclipse.jdt.ws.core.JDTWSCorePlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JDTJ2EECorePlugin extends AbstractPlugin
{

	public static final String WST_VALIDATION_BUILDER_ID = "org.eclipse.wst.validation.validationbuilder";
	
   /** The shared instance */
   private static JDTJ2EECorePlugin plugin;


   /** The constructor. */
   public JDTJ2EECorePlugin()
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

      ClassPathContainerRepository.getInstance().addClassPathEntry(J2EE13ClasspathContainer.CLASSPATH_CONTAINER);
      ClassPathContainerRepository.getInstance().addClassPathEntry(J2EE14ClasspathContainer.CLASSPATH_CONTAINER);

      // Force the plugin load
      JDTTestCorePlugin.getDefault().getBaseDir();
      JDTWSCorePlugin.getDefault().getBaseDir();
   }


   /**
    * Returns the shared instance.
    *
    * @return   The default value
    */
   public static JDTJ2EECorePlugin getDefault()
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
         return "org.jboss.ide.eclipse.jdt.j2ee.core";//$NON-NLS-1$
      }
      return getDefault().getBundle().getSymbolicName();
   }
}
