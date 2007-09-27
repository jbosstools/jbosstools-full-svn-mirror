/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.packaging.core;

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jdt.core.IJavaProject;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.packaging.core.configuration.ProjectConfigurations;
import org.jboss.ide.eclipse.packaging.core.configuration.StandardConfigurations;

/**
 * The main plugin class to be used in the desktop.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class PackagingCorePlugin extends AbstractPlugin
{
   private StandardConfigurations configurations;
   private Map projectConfigurations = new Hashtable();

   /** The shared instance */
   private static PackagingCorePlugin plugin;

   /** Name of the Ant build file */
   public final static String BUILD_FILE = "packaging-build.xml";//$NON-NLS-1$
   /** Name for the standards configurations */
   public final static String GENERICS_FILE = "resources/standards.xml";//$NON-NLS-1$
   /** Name of the configuration file per project */
   public final static String PROJECT_FILE = ".packaging";//$NON-NLS-1$
   /** Stylesheet to transform configuration file to Ant build file */
   public final static String XSL_FILE = "resources/configuration2packaging.xsl";//$NON-NLS-1$


   /** The constructor. */
   public PackagingCorePlugin()
   {
      plugin = this;
   }


   /**
    * Gets the projectConfigurations attribute of the PackagingPlugin object
    *
    * @param project  Description of the Parameter
    * @return         The projectConfigurations value
    */
   public ProjectConfigurations getProjectConfigurations(IJavaProject project)
   {
      ProjectConfigurations config = (ProjectConfigurations) projectConfigurations.get(project);
      if (config == null)
      {
         config = new ProjectConfigurations(project);
      }
      return config;
   }


   /**
    * Gets the standardConfigurations attribute of the CorePlugin object
    *
    * @return   The standardConfigurations value
    */
   public StandardConfigurations getStandardConfigurations()
   {
      if (this.configurations == null)
      {
         this.configurations = new StandardConfigurations();
      }
      return this.configurations;
   }


   /**
    * Returns the shared instance.
    *
    * @return   The default value
    */
   public static PackagingCorePlugin getDefault()
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
         return "org.jboss.ide.eclipse.packaging.core";//$NON-NLS-1$
      }
      return getDefault().getBundle().getSymbolicName();
   }
}
