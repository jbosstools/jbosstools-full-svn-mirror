/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper;

import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;

import javax.servlet.ServletContext;

import org.apache.jasper.compiler.TldLocationsCache;

/**
 * Extends the Jasper TldLocationsCache to allow the scan of a given
 * ClassLoader instead of the ThreadContext Classloader.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPTldLocationsCache extends TldLocationsCache
{

   /** The ClassLoader to scan */
   private URLClassLoader loader;


   /**
    * Extended constructor
    *
    *
    * @param ctxt
    * @param redeployMode
    * @param loader
    */
   public JSPTldLocationsCache(ServletContext ctxt, boolean redeployMode, URLClassLoader loader)
   {
      super(ctxt, redeployMode);
      this.loader = loader;
   }


   /**
    * Override to scan the given ClassLoader
    *
    * @exception Exception  Description of the Exception
    */
   protected void scanJars()
      throws Exception
   {
      ClassLoader webappLoader = this.loader;
      ClassLoader loader = webappLoader;

      while (loader != null)
      {
         if (loader instanceof URLClassLoader)
         {
            URL[] urls = ((URLClassLoader) loader).getURLs();
            for (int i = 0; i < urls.length; i++)
            {
               URLConnection conn = urls[i].openConnection();
               if (conn instanceof JarURLConnection)
               {
                  if (needScanJar(loader, webappLoader, ((JarURLConnection) conn).getJarFile().getName()))
                  {
                     this.scanJar((JarURLConnection) conn, true);
                  }
               }
               else
               {
                  String urlStr = urls[i].toString();
                  if (urlStr.startsWith(FILE_PROTOCOL) && urlStr.endsWith(JAR_FILE_SUFFIX) && needScanJar(loader, webappLoader, urlStr))
                  {
                     URL jarURL = new URL("jar:" + urlStr + "!/");//$NON-NLS-1$ //$NON-NLS-2$
                     this.scanJar((JarURLConnection) jarURL.openConnection(), true);
                  }
               }
            }
         }
         loader = loader.getParent();
      }
   }
}
