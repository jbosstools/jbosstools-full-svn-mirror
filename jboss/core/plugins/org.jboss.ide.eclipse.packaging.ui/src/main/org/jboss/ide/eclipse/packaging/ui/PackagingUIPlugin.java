/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.packaging.ui;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.packaging.core.PackagingCorePlugin;

/**
 * The main plugin class to be used in the desktop.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class PackagingUIPlugin extends AbstractPlugin
{
   /** The shared instance. */
   private static PackagingUIPlugin plugin;


   /** The constructor. */
   public PackagingUIPlugin()
   {
      plugin = this;
   }


   /**
    * Returns the shared instance.
    *
    * @return   The default value
    */
   public static PackagingUIPlugin getDefault()
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
         return "org.jboss.ide.eclipse.packaging.ui";//$NON-NLS-1$
      }
      return getDefault().getBundle().getSymbolicName();
   }


   public IFile createBuildFile(IProject project) throws IOException, CoreException, TransformerException
   {
      IFile file = project.getFile(PackagingCorePlugin.PROJECT_FILE);
      IFile buildFile = project.getFile(PackagingCorePlugin.BUILD_FILE);

      // If the packaging project file exists, then process it
      if (file.exists() && file.getModificationStamp() > buildFile.getModificationStamp())
      {
         TransformerFactory tFactory = TransformerFactory.newInstance();

         InputStream is = null;
         byte[] bytes;
         try
         {
            // Gets the XSL file
            URL xslFile = PackagingCorePlugin.getDefault().find(new Path(PackagingCorePlugin.XSL_FILE));
            is = new BufferedInputStream(xslFile.openStream());
            Source stylesheet = new StreamSource(is);

            // Create a new transformer on the stylesheet
            Transformer transformer = tFactory.newTransformer(stylesheet);

            Source source = new StreamSource(file.getContents());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Result result = new StreamResult(baos);

            // Apply the XSL style
            transformer.setOutputProperty("indent", "yes");//$NON-NLS-1$ //$NON-NLS-2$
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
