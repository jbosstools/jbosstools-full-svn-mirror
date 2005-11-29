/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.ui;

import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.xml.ui.editors.XMLTextTools;
import org.jboss.ide.eclipse.jdt.xml.ui.preferences.XMLSyntaxPreferencePage;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JDTXMLUIPlugin extends AbstractPlugin
{
   private XMLTextTools xmlTextTools;
   /** The shared instance */
   private static JDTXMLUIPlugin plugin;


   /** The constructor. */
   public JDTXMLUIPlugin()
   {
      super();
      plugin = this;
   }


   /**
    * Returns instance of text tools for XML.
    *
    * @return   The xMLTextTools value
    */
   public XMLTextTools getXMLTextTools()
   {
      if (xmlTextTools == null)
      {
         xmlTextTools = new XMLTextTools(this.getPreferenceStore());
      }
      return xmlTextTools;
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
      if (this.xmlTextTools != null)
      {
         this.xmlTextTools.dispose();
      }

      super.stop(context);
   }


   /** Description of the Method */
   protected void initializeDefaultPluginPreferences()
   {
      XMLSyntaxPreferencePage.initDefaults(this.getPreferenceStore());
   }


   /**
    * Returns the shared instance.
    *
    * @return   The default value
    */
   public static JDTXMLUIPlugin getDefault()
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
         return "org.jboss.ide.eclipse.jdt.xml.ui";//$NON-NLS-1$
      }
      return getDefault().getBundle().getSymbolicName();
   }
}
