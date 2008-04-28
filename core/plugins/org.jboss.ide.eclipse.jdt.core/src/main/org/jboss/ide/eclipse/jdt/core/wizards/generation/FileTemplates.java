/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
package org.jboss.ide.eclipse.jdt.core.wizards.generation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.core.AbstractPlugin;

/**
 * Generation file-based templates accessor helper class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class FileTemplates implements ITemplates
{
   /** Description of the Field */
   protected URL url;

   /** Description of the Field */
   public static String ROOT = "resources/templates/";//$NON-NLS-1$

   /**
    *Constructor for the FileTemplates object
    *
    * @param plugin  Description of the Parameter
    */
   public FileTemplates(AbstractPlugin plugin)
   {
      IPath path = new Path(ROOT);
      this.url = plugin.find(path);
   }

   /**
    * Gets the string corresponding to a given key
    *
    * @param key  The bundle key
    * @return     The value or the key if not found
    */
   public String getString(String key)
   {
      StringBuffer result = new StringBuffer();
      try
      {
         URL tmpUrl = new URL(this.url, key);
         InputStream is = tmpUrl.openStream();
         BufferedReader reader = new BufferedReader(new InputStreamReader(is));
         int len = -1;
         char[] buffer = new char[512];
         while ((len = reader.read(buffer)) >= 0)
         {
            result.append(buffer, 0, len);
         }
         reader.close();
      }
      catch (IOException e)
      {
         return '!' + key + '!';
      }
      return result.toString();
   }

   /**
    * Gets the string corresponding to a given key and format it with
    * the parameters
    *
    * @param key         The bundle key
    * @param parameters  The parameters
    * @return            The value or the key if not found
    */
   public String getString(String key, Object[] parameters)
   {
      return this.getString(key);
   }
}
