/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Shell;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.xdoclet.assist.completion.XDocletJavaDocCompletionProcessor;
import org.jboss.ide.eclipse.xdoclet.assist.model.AdditionalValuesXMLManager;
import org.jboss.ide.eclipse.xdoclet.assist.model.DocletTree;
import org.jboss.ide.eclipse.xdoclet.assist.model.DocletTreeXMLManager;
import org.jboss.ide.eclipse.xdoclet.assist.model.TemplateList;
import org.jboss.ide.eclipse.xdoclet.assist.model.TemplateListXMLManager;
import org.jboss.ide.eclipse.xdoclet.assist.model.VariableStore;
import org.jboss.ide.eclipse.xdoclet.assist.model.VariableStoreXMLManager;
import org.jboss.ide.eclipse.xdoclet.assist.model.XMLPersistenceManager;
import org.jboss.ide.eclipse.xdoclet.assist.model.XTagsProvider;
import org.jboss.ide.eclipse.xdoclet.core.XDocletCorePlugin;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;
import org.osgi.framework.BundleContext;
import org.xml.sax.InputSource;

/**
 * The main plugin class
 *
 * @author    Hans Dockter
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @todo      Javadoc to complete
 */
public class XDocletAssistPlugin extends AbstractPlugin implements IPropertyChangeListener
{
   /** Description of the Field */
   protected XDocletJavaDocCompletionProcessor completionProvider = new XDocletJavaDocCompletionProcessor();
   /** Description of the Field */
   protected Document docletDocument;
   /** Description of the Field */
   protected DocletTree docletTree;
   /** Description of the Field */
   protected DocletTreeXMLManager docletTreeXMLManager = new DocletTreeXMLManager();
   /** Description of the Field */
   protected TemplateList templateList;
   /** Description of the Field */
   protected TemplateListXMLManager templateListXMLManager = new TemplateListXMLManager();
   /** Description of the Field */
   protected VariableStore variableStore;
   /** Description of the Field */
   protected VariableStoreXMLManager variableStoreXMLManager = new VariableStoreXMLManager();
   /** Description of the Field */
   protected HashMap xmlPersistenceManager = new HashMap(3);

   /** Description of the Field */
   public static String ADDITIONAL_VALUES_FILE = "additional_values.xml";//$NON-NLS-1$
   /** Description of the Field */
   public static String ATTR_ADDITIONAL_VALUES = "additionalValues";//$NON-NLS-1$
   /** Description of the Field */
   public static String ATTR_TEMPLATES = "templates";//$NON-NLS-1$
   /** Description of the Field */
   public static String ATTR_VARIABLES = "variables";//$NON-NLS-1$
   /** Description of the Field */
   public static String DOCLETTREE_FILE = "doclettree.dat";//$NON-NLS-1$
   /** Description of the Field */
   public static String TEMPLATES_FILE = "templates.xml";//$NON-NLS-1$
   /** Description of the Field */
   public static String VARIABLES_FILE = "variables.xml";//$NON-NLS-1$

   /** Description of the Field */
   protected static HashMap filenames = new HashMap(3);
   /** Description of the Field */
   protected static URL iconPathURL = null;
   /** Description of the Field */
   protected static HashMap inputSources = new HashMap(3);
   /** Description of the Field */
   protected static HashMap originalFilenames = new HashMap(3);
   /** Description of the Field */
   protected static String testIconPath = null;

   /** The shared instance */
   private static XDocletAssistPlugin plugin;


   /** The constructor. */
   public XDocletAssistPlugin()
   {
      plugin = this;
   }


   /**
    * Adds a feature to the ValuesToDocletTree attribute of the XDocletPlugin
    * object
    *
    * @param docletTree  The feature to be added to the ValuesToDocletTree attribute
    */
   public void addValuesToDocletTree(DocletTree docletTree)
   {
      AdditionalValuesXMLManager valuesPersistent = new AdditionalValuesXMLManager();
      valuesPersistent.addDocumentToDocletTree(docletTree, getXMLDocument(ATTR_ADDITIONAL_VALUES));
   }


   /**
    * Gets the andSetRefreshedTemplateList attribute of the XDocletPlugin
    * object
    *
    * @return   The andSetRefreshedTemplateList value
    */
   public TemplateList getAndSetRefreshedTemplateList()
   {
      templateList = templateListXMLManager.setTemplateListFromDocument(getXMLDocument(ATTR_TEMPLATES, true), getDocletTree());
      return templateList;
   }


   /**
    * Returns the docletTree.
    *
    * @return   DocletTree
    */
   public DocletTree getDocletTree()
   {
      if (docletTree == null)
      {
         if (getDocletTreeFile().exists())
         {
            try
            {
               docletTree = docletTreeXMLManager.readDocletTreeFromCache(new FileInputStream(getDocletTreeFile()));
               addValuesToDocletTree(docletTree);
            }
            catch (IOException e)
            {
               log(e);
            }
            catch (ClassNotFoundException e)
            {
               log(e);
            }
         }
         if (docletTree == null)
         {
            docletTree = getRefreshedDocletTree();
         }
      }
      return docletTree;
   }


   /**
    * Gets the refreshedDocletTree attribute of the XDocletPlugin object
    *
    * @return   The refreshedDocletTree value
    */
   public DocletTree getRefreshedDocletTree()
   {
      try
      {
         IRunnableWithProgress runnableWithProgress =
            new IRunnableWithProgress()
            {
               public void run(IProgressMonitor monitor)
               {
                  monitor.beginTask(XDocletAssistMessages.getString("XDocletPlugin.Reading_the_xtags.xml_of_the_XDoclet_modules_15"), 1000);//$NON-NLS-1$
                  URL[] urls = XDocletCorePlugin.getDefault().getXTagsXml();
                  docletTree = docletTreeXMLManager.initDocletTree(urls, monitor, 500);
                  if (docletTree == null)
                  {
                     return;
                  }
                  try
                  {
                     docletTreeXMLManager.writeDocletTreeToCache(new FileOutputStream(getDocletTreeFile()));
                  }
                  catch (IOException e)
                  {
                     log(e);
                  }
                  addValuesToDocletTree(docletTree);
               }
            };
         Shell activeShell = XDocletAssistPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
         if (activeShell == null)
         {
            return null;
         }
         ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(activeShell);
         progressMonitorDialog.run(true, true, runnableWithProgress);
      }
      catch (Exception e)
      {
         log(e);
         return null;
      }
      return docletTree;
   }


   /**
    * Returns the templateList.
    *
    * @return   TemplateList
    */
   public TemplateList getTemplateList()
   {
      if (templateList == null)
      {
         getAndSetRefreshedTemplateList();
      }
      XMLOutputter outputter = new XMLOutputter();
      try
      {
         outputter.output(getXMLDocument(ATTR_TEMPLATES), System.out);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return templateList;
   }


   /**
    * Returns the variableStore.
    *
    * @return   VariableStore
    */
   public VariableStore getVariableStore()
   {
      if (variableStore == null)
      {
         variableStore = variableStoreXMLManager.setVariableStoreFromDocument(getXMLDocument(ATTR_VARIABLES));
         setPatternToStore();
      }
      return variableStore;
   }


   /**
    * @param event  Description of the Parameter
    * @see          org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
    */
   public void propertyChange(PropertyChangeEvent event)
   {
      setPatternToStore();
   }


   /** Description of the Method */
   public void saveTemplateList()
   {
      templateListXMLManager.writeToDocument(getXMLDocument(ATTR_TEMPLATES));
      saveXMLDocument(ATTR_TEMPLATES);
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
      initDefaultPatterns();
      initCodeAssistPreferences();
      getPreferenceStore().addPropertyChangeListener(this);
   }


   /**
    * A unit test for JUnit
    *
    * @param docletTree  Description of the Parameter
    */
   public void testSetDocletTree(DocletTree docletTree)
   {
      this.docletTree = docletTree;
   }


   /**
    * Gets the docletTreeFile attribute of the XDocletPlugin object
    *
    * @return   The docletTreeFile value
    */
   protected File getDocletTreeFile()
   {
      return new File(getStateLocation().toFile(), DOCLETTREE_FILE);
   }


   /**
    * Gets the xMLDocument attribute of the XDocletPlugin object
    *
    * @param key  Description of the Parameter
    * @return     The xMLDocument value
    */
   protected Document getXMLDocument(String key)
   {
      return getXMLDocument(key, false);
   }


   /**
    * Gets the xMLDocument attribute of the XDocletPlugin object
    *
    * @param key      Description of the Parameter
    * @param refresh  Description of the Parameter
    * @return         The xMLDocument value
    */
   protected Document getXMLDocument(String key, boolean refresh)
   {
      try
      {
         return getXMLPersistenceManager(key, refresh).getDocument();
      }
      catch (IOException e)
      {
         log(e);
      }
      catch (JDOMException e)
      {
         log(e);
      }
      return null;
   }


   /**
    * Gets the xMLPersistenceManager attribute of the XDocletPlugin object
    *
    * @param key      Description of the Parameter
    * @param refresh  Description of the Parameter
    * @return         The xMLPersistenceManager value
    */
   protected XMLPersistenceManager getXMLPersistenceManager(String key, boolean refresh)
   {
      if (xmlPersistenceManager.get(key) == null || refresh == true)
      {
         try
         {
            File file = getStateLocation().append("/" + filenames.get(key)).toFile();//$NON-NLS-1$
            if (!file.exists())
            {
               XMLPersistenceManager persistenceManager = new XMLPersistenceManager(new FileReader(getBaseDir()
                     + originalFilenames.get(key)), false, (InputSource) inputSources.get(key));
               try
               {
                  persistenceManager.persistDocument(new FileWriter(file));
               }
               catch (IOException e)
               {
                  log(e);
               }
               catch (JDOMException e)
               {
                  log(e);
               }
            }
            xmlPersistenceManager.put(key, new XMLPersistenceManager(new FileReader(file), false, (InputSource) inputSources.get(key)));

         }
         catch (FileNotFoundException e)
         {
            log(e);
            return null;
         }
      }
      return (XMLPersistenceManager) xmlPersistenceManager.get(key);
   }


   /** Description of the Method */
   protected void initDefaultPatterns()
   {
      getPreferenceStore().setDefault(IXDocletConstants.VARIABLE_CLASSNAME, "^(\\w*)$");//$NON-NLS-1$
      getPreferenceStore().setDefault(IXDocletConstants.VARIABLE_CLASSNAME_WITHOUT_SUFFIX, "^(\\w*)Bean$");//$NON-NLS-1$
      getPreferenceStore().setDefault(IXDocletConstants.VARIABLE_PACKAGE, "^(\\S*)$");//$NON-NLS-1$
      getPreferenceStore().setDefault(IXDocletConstants.VARIABLE_PARENT_PACKAGE, "^(\\S*)\\.\\w*$");//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param key  Description of the Parameter
    */
   protected void saveXMLDocument(String key)
   {
      try
      {
         FileWriter writer = new FileWriter(getStateLocation().toString() + "/" //$NON-NLS-1$
         + filenames.get(key));
         getXMLPersistenceManager(key, false).persistDocument(writer);
      }
      catch (IOException e)
      {
         showErrorMessage(XDocletAssistMessages.getString("XDocletPlugin.Can__t_write_to_file_26"));//$NON-NLS-1$
         log(e);
      }
      catch (JDOMException e)
      {
         log(e);
         showErrorMessage(XDocletAssistMessages.getString("XDocletPlugin.Can__t_write_to_file_27"));//$NON-NLS-1$
      }
   }

   // This is a hack for the alpha (see VariablesPreferencePage)
   /** Sets the patternToStore attribute of the XDocletPlugin object */
   protected void setPatternToStore()
   {
      getVariableStore().getVariable("classname").setPattern(//$NON-NLS-1$
      getPreferenceStore().getString(IXDocletConstants.VARIABLE_CLASSNAME));
      getVariableStore().getVariable("classnameWithoutSuffix").setPattern(//$NON-NLS-1$
      getPreferenceStore().getString(IXDocletConstants.VARIABLE_CLASSNAME_WITHOUT_SUFFIX));
      getVariableStore().getVariable("package").setPattern(//$NON-NLS-1$
      getPreferenceStore().getString(IXDocletConstants.VARIABLE_PACKAGE));
      getVariableStore().getVariable("packageWithoutLast").setPattern(//$NON-NLS-1$
      getPreferenceStore().getString(IXDocletConstants.VARIABLE_PARENT_PACKAGE));
   }


   /** Method initIndentBehaviour */
   private void initCodeAssistPreferences()
   {
      getPreferenceStore().setDefault(IXDocletConstants.DEACTIVATE_XDOCLET_SUPPORT, false);
   }


   /**
    * Returns the shared instance.
    *
    * @return   The default value
    */
   public static XDocletAssistPlugin getDefault()
   {
      return plugin;
   }


   /**
    * Returns the string from the plugin's resource bundle, or 'key' if not
    * found.
    *
    * @param key  Description of the Parameter
    * @return     The resourceString value
    */
   public static String getResourceString(String key)
   {
      ResourceBundle bundle = XDocletAssistPlugin.getDefault().getResourceBundle();
      try
      {
         return bundle.getString(key);
      }
      catch (MissingResourceException e)
      {
         return key;
      }

   }


   /**
    * Returns the testIconPath.
    *
    * @return   String
    */
   public static String getTestIconPath()
   {
      return testIconPath;
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
         return "org.jboss.ide.eclipse.xdoclet.assist";//$NON-NLS-1$
      }
      return getDefault().getBundle().getSymbolicName();
   }


   /**
    * Returns the workspace instance.
    *
    * @return   The workspace value
    */
   public static IWorkspace getWorkspace()
   {
      return ResourcesPlugin.getWorkspace();
   }


   /**
    * Sets the testIconPath.
    *
    * @param testIconPath  The testIconPath to set
    */
   public static void setTestIconPath(String testIconPath)
   {
      XDocletAssistPlugin.testIconPath = testIconPath;
   }

   static
   {
      inputSources.put(ATTR_VARIABLES, XTagsProvider.getVariablesDTDInputSource());
      inputSources.put(ATTR_TEMPLATES, XTagsProvider.getTemplatesDTDInputSource());
      inputSources.put(ATTR_ADDITIONAL_VALUES, XTagsProvider.getAdditionalValuesDTDInputSource());

      filenames.put(ATTR_VARIABLES, VARIABLES_FILE);
      filenames.put(ATTR_TEMPLATES, TEMPLATES_FILE);
      filenames.put(ATTR_ADDITIONAL_VALUES, ADDITIONAL_VALUES_FILE);

      originalFilenames.put(ATTR_VARIABLES, "resources/" + VARIABLES_FILE);//$NON-NLS-1$
      originalFilenames.put(ATTR_TEMPLATES, "resources/" + TEMPLATES_FILE);//$NON-NLS-1$
      originalFilenames.put(ATTR_ADDITIONAL_VALUES, "resources/" + ADDITIONAL_VALUES_FILE);//$NON-NLS-1$
   }
}
