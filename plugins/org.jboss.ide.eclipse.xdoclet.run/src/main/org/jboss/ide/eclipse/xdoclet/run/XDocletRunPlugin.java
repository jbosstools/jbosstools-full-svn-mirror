/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.run;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.xdoclet.core.XDocletCorePlugin;
import org.jboss.ide.eclipse.xdoclet.run.configuration.ProjectConfigurations;
import org.jboss.ide.eclipse.xdoclet.run.configuration.StandardConfigurations;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletDataRepository;
import org.jboss.ide.eclipse.xdoclet.run.util.AntUtil;

/**
 * The main plugin class to be used in the desktop.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XDocletRunPlugin extends AbstractPlugin
{
   private StandardConfigurations configurations;
   private Map projectConfigurations = new Hashtable();
   private XDocletDataRepository repository;

   /** The shared instance */
   private static XDocletRunPlugin plugin;

   /** Name of the Ant build file */
   public final static String BUILD_FILE = "xdoclet-build.xml";//$NON-NLS-1$
   /** Name for the standards configurations */
   public final static String GENERICS_FILE = "resources/standards.xml";//$NON-NLS-1$
   /** Name of the configuration file per project */
   public final static String PROJECT_FILE = ".xdoclet";//$NON-NLS-1$
   /** Stylesheet to transform configuration file to Ant build file */
   public final static String XSL_FILE = "resources/configuration2xdoclet.xsl";//$NON-NLS-1$


   /** The constructor. */
   public XDocletRunPlugin()
   {
      XDocletRunPlugin.plugin = this;
   }


   /**
    * Gets the project configurations attribute of the PackagingPlugin object
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
    * Gets the xDocletDataRepository attribute of the CorePlugin object
    *
    * @return   The xDocletDataRepository value
    */
   public XDocletDataRepository getXDocletDataRepository()
   {
      if (this.repository == null)
      {
         this.repository = new XDocletDataRepository();
      }
      return this.repository;
   }


   /**
    * Returns the shared instance.
    *
    * @return   The default value
    */
   public static XDocletRunPlugin getDefault()
   {
      return XDocletRunPlugin.plugin;
   }


   /**
    * Convenience method which returns the unique identifier of this plugin.
    *
    * @return   The uniqueIdentifier value
    */
   public static String getUniqueIdentifier()
   {
      if (getDefault() == null)
      {
         // If the default instance is not yet initialized,
         // return a static identifier. This identifier must
         // match the plugin id defined in plugin.xml
         return "org.jboss.ide.eclipse.xdoclet.run";//$NON-NLS-1$
      }
      return getDefault().getBundle().getSymbolicName();
   }


   public IFile createBuildFile(IJavaProject project) throws IOException, CoreException, TransformerException
   {
      IFile file = project.getProject().getFile(XDocletRunPlugin.PROJECT_FILE);
      IFile buildFile = project.getProject().getFile(XDocletRunPlugin.BUILD_FILE);
   
      // If the xdoclet project file exists, then process it
      if (file.exists() && file.getModificationStamp() > buildFile.getModificationStamp())
      {
         TransformerFactory tFactory = TransformerFactory.newInstance();
   
         InputStream is = null;
         byte[] bytes;
         try
         {
            // Gets the XSL file
            URL xslFile = XDocletRunPlugin.getDefault().find(new Path(XDocletRunPlugin.XSL_FILE));
            is = new BufferedInputStream(xslFile.openStream());
            Source stylesheet = new StreamSource(is);
   
            // Create a new transformer on the stylesheet
            Transformer transformer = tFactory.newTransformer(stylesheet);
   
            Source source = new StreamSource(file.getContents());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Result result = new StreamResult(baos);
   
            // Apply the XSL style
            transformer.setOutputProperty("indent", "yes");//$NON-NLS-1$ //$NON-NLS-2$
            transformer.setParameter("project.jars", AntUtil.getClassPathAsXml(project));//$NON-NLS-1$
            transformer.setParameter("eclipse.home", AntUtil.normalizePath(Platform.getInstallLocation().getURL()));//$NON-NLS-1$
            transformer.setParameter("xdoclet.basedir", AntUtil.performSubstitutions(AntUtil.normalizePath(XDocletCorePlugin.getDefault().getBaseDir())));//$NON-NLS-1$
            transformer.setParameter("jboss.net.version", XDocletCorePlugin.getDefault().getJBossNetVersion());//$NON-NLS-1$
            transformer.transform(source, result);
            
            bytes = baos.toByteArray();
         }
         finally
         {
            // Ensure that the input stream is closed.
            if (is != null)
            {
               try
               {
                  is.close();
               }
               catch (Throwable ignore)
               {
               }
            }
         }
   
         // Save the file
         if (!buildFile.exists())
         {
            buildFile.create(null, true, null);
         }
         buildFile.setContents(new ByteArrayInputStream(bytes), true, true, null);
      }
      
      return buildFile;
   }
}
