/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.core.ns;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.resource.impl.URIConverterImpl;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.xml.core.JDTXMLCorePlugin;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class LocalCache
{
   private static LocalCache instance = new LocalCache();
   private final static String cacheDir = JDTXMLCorePlugin.getDefault().getBaseDir() + JDTXMLCorePlugin.CACHE_FOLDER;


   /**Constructor for the LocalCache object */
   private LocalCache() { }


   /**
    * Gets the file attribute of the LocalCache object
    *
    * @param url  Description of the Parameter
    * @return     The file value
    */
   public File getFile(URL url)
   {
      File file = this.fetchContent(url);
      return file;
   }


   /**
    * Gets the location attribute of the LocalCache object
    *
    * @param location  Description of the Parameter
    * @return          The location value
    */
   public String getLocation(String location)
   {
      //System.out.println(getClass().getName()+ " : getting location for " + location);

      try
      {
         URL url = new URL(location);
         File file = this.fetchContent(url);
         return file.toURL().toString();
      }
      catch (MalformedURLException mue)
      {
      }

      try
      {
         File file = new File(location);
         return file.toURL().toString();
      }
      catch (MalformedURLException e)
      {
      }

      return null;
   }


   /**
    * Gets the schema attribute of the LocalCache object
    *
    * @param url  Description of the Parameter
    * @return     The schema value
    */
   public XSDSchema getSchema(String url)
   {
      ResourceSet resourceSet = new ResourceSetImpl();
      URIConverter uc =
         new URIConverterImpl()
         {
            public URI normalize(URI uri)
            {
               URI converted = super.normalize(uri);
               converted = URI.createURI(LocalCache.this.getLocation(converted.toString()));
               return converted;
            }
         };
      resourceSet.setURIConverter(uc);
      XSDResourceImpl xsdSchemaResource = (XSDResourceImpl) resourceSet.getResource(URI.createURI(url), true);

      for (Iterator it = resourceSet.getResources().iterator(); it.hasNext(); )
      {
         Resource resource = (Resource) it.next();
         if (resource instanceof XSDResourceImpl)
         {
            XSDResourceImpl xsdResource = (XSDResourceImpl) resource;
            return xsdResource.getSchema();
         }
      }

      return null;
   }


   /**
    * Description of the Method
    *
    * @param url  Description of the Parameter
    * @return     Description of the Return Value
    */
   private synchronized File fetchContent(URL url)
   {
      String contentFile = url.getHost() + File.separator + url.getPath();
      File file = new File(contentFile);
      if (!file.isAbsolute())
      {
         file = new File(cacheDir + File.separator + contentFile);
      }

      if (!file.exists())
      {
         try
         {
            if (file.toURL().equals(url))
            {
               // File doesn't exists and it points to the cache
               // Revert back to an URL assuming it is http
               File dir = new File(cacheDir);
               String newURL = file.toURL().toString().substring(dir.toURL().toString().length());
               newURL = "http://" + newURL;//$NON-NLS-1$
               return this.fetchContent(new URL(newURL));
            }
         }
         catch (MalformedURLException e1)
         {
         }

         // Creating parent directories
         file.getParentFile().mkdirs();

         try
         {
            //System.out.println("Donwloading " + file);

            InputStreamReader is = new InputStreamReader(url.openStream());
            String encoding = is.getEncoding();
            BufferedReader reader = new BufferedReader(is);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), encoding));

            String line = null;
            while ((line = reader.readLine()) != null)
            {
               writer.write(line);
               writer.newLine();
               writer.flush();
            }
            reader.close();
            writer.close();
         }
         catch (IOException e)
         {
            AbstractPlugin.logError("Unable to download requested document", e);//$NON-NLS-1$
         }
      }

      return file;
   }


   /**
    * Gets the instance attribute of the LocalCache class
    *
    * @return   The instance value
    */
   public static LocalCache getInstance()
   {
      return instance;
   }
}
