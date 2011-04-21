/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.core;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.runtime.Preferences;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @created   24 mars 2003
 */
public class XDocletCorePlugin extends AbstractPlugin
{
   public static final String JBOSS_NET_VERSION = "jboss.net.version";

   public static final String JBOSS_NET_VERSION_4_0 = "4.0";

   public static final String JBOSS_NET_VERSION_3_2 = "3.2";
   
   public static final String JBOSS_NET_VERSION_DEFAULT = JBOSS_NET_VERSION_4_0;

   private URL[] modules;
   private URL[] xTagsXML;

   /** The shared instance */
   private static XDocletCorePlugin plugin;


   /** The constructor. */
   public XDocletCorePlugin()
   {
      XDocletCorePlugin.plugin = this;
   }


   /**
    * Gets the XDoclet modules installed in the XDoclet Core plugin
    *
    * @return   The modules URLs
    */
   public URL[] getModules()
   {
      if (this.modules == null)
      {
         this.fetchModules();
      }
      return this.modules;
   }


   /**
    * Gets the XDoclet XTagsXml files installed in the XDoclet Core plugin
    *
    * @return   The XTagsXml fiels URLs
    */
   public URL[] getXTagsXml()
   {
      if (this.xTagsXML == null)
      {
         this.fetchXTagsXML();
      }
      return this.xTagsXML;
   }


   /** Refresh the XDoclet modules installed in the XDoclet Core plugin */
   public void refreshModules()
   {
      this.fetchModules();
      this.fetchXTagsXML();
   }


   /** Refresh the XDoclet XTagsXml files installed in the XDoclet Core plugin */
   public void refreshXtagsXML()
   {
      this.fetchXTagsXML();
   }
   
   
   public String getJBossNetVersion()
   {
      Preferences prefs = getPluginPreferences();
      return prefs.getString(XDocletCorePlugin.JBOSS_NET_VERSION);
   }
   
   
   public void setJBossNetVersion(String version)
   {
      if (!JBOSS_NET_VERSION_4_0.equals(version) && !JBOSS_NET_VERSION_3_2.equals(version))
      {
         throw new IllegalArgumentException(version);
      }
      Preferences prefs = getPluginPreferences();
      prefs.setValue(XDocletCorePlugin.JBOSS_NET_VERSION, version);
      savePluginPreferences();
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
      //TODO: this isn't the right place for setting defaults
      getPluginPreferences().setDefault(JBOSS_NET_VERSION, JBOSS_NET_VERSION_DEFAULT);
      this.fetchModules();
   }


   /** Fetchs the XDoclet modules installed in the XDoclet Core plugin */
   protected void fetchModules()
   {
      StringBuffer cp = new StringBuffer();
      ArrayList result = new ArrayList();
      try
      {
         File[] files = this.getModuleFiles();
         for (int i = 0; i < files.length; i++)
         {
            result.add(files[i].toURL());
            cp.append(files[i].getAbsolutePath()).append(File.pathSeparatorChar);
         }
      }
      catch (MalformedURLException mfue)
      {
         AbstractPlugin.logError("Error while getting modules files", mfue);//$NON-NLS-1$
      }
      System.setProperty("xdoclet.class.path", cp.toString());
      this.modules = (URL[]) result.toArray(new URL[result.size()]);
   }


   /** Fetchs the XDoclet XTagsXml files installed in the XDoclet Core plugin */
   protected void fetchXTagsXML()
   {
      this.xTagsXML = this.getURLs("META-INF/xtags.xml");//$NON-NLS-1$
   }


   /**
    * Gets the XDoclet modules installed in the XDoclet Core plugin
    *
    * @return   The XDoclet modules files
    */
   protected File[] getModuleFiles()
   {
      File dir = new File(this.getBaseDir());//$NON-NLS-1$

      // Lists every modules in the base dir
      File[] modules = dir.listFiles(
         new FileFilter()
         {
            public boolean accept(File file)
            {
               String fileName = file.getName();
               if (fileName.endsWith(".jar"))//$NON-NLS-1
               {
                  if (fileName.startsWith("xdoclet-module-jboss-net"))
                  {
                     String version = getJBossNetVersion();
                     return (fileName.indexOf(version) != -1);
                  }
                  return true;
               }
               return false;
            }
         });

      return modules;
   }


   /**
    * Gets the URLs of files according to a given pattern
    *
    * @param pattern  Pattern to search file into modules
    * @return         The URLs of found files
    */
   protected URL[] getURLs(final String pattern)
   {
      final ArrayList result = new ArrayList();

      try
      {
         File[] modules = XDocletCorePlugin.this.getModuleFiles();

         // Search every modules for the pattern
         for (int i = 0; i < modules.length; i++)
         {
            JarFile jarFile = new JarFile(modules[i]);
            JarEntry xtagsXml = jarFile.getJarEntry(pattern);
            if (xtagsXml != null)
            {
               result.add(new URL(new URL("jar:" + modules[i].toURL() + "!/"), pattern));//$NON-NLS-1$ //$NON-NLS-2$
            }
         }
      }
      catch (IOException e)
      {
         AbstractPlugin.logError("Error while getting modules URLs", e);//$NON-NLS-1$
      }

      return (URL[]) result.toArray(new URL[result.size()]);
   }


   /**
    * Returns the shared instance.
    *
    * @return   The shared instance
    */
   public static XDocletCorePlugin getDefault()
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
         return "org.jboss.ide.eclipse.xdoclet.core";//$NON-NLS-1$
      }
      return getDefault().getBundle().getSymbolicName();
   }
}
