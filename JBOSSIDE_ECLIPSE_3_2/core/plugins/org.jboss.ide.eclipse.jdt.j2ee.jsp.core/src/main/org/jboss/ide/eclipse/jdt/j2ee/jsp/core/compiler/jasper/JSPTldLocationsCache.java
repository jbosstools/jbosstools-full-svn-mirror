/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
   protected void scanJars() throws Exception
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
                  if (urlStr.startsWith(FILE_PROTOCOL) && urlStr.endsWith(JAR_FILE_SUFFIX)
                        && needScanJar(loader, webappLoader, urlStr))
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
