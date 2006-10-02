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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.core.AbstractPlugin;

/**
 * Generation templates accessor helper class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class AbstractTemplates implements ITemplates
{
   /** Description of the Field */
   protected ResourceBundle bundle;

   /** Description of the Field */
   public static String LEAF = "/Templates.properties";//$NON-NLS-1$

   /** Description of the Field */
   public static String ROOT = "resources/templates/";//$NON-NLS-1$

   /**
    *Constructor for the AbstractTemplates object
    *
    * @param plugin  Description of the Parameter
    * @param prefix  Description of the Parameter
    */
   protected AbstractTemplates(AbstractPlugin plugin, String prefix)
   {
      try
      {
         IPath path = new Path(ROOT + prefix + LEAF);
         URL url = plugin.find(path);
         InputStream is = url.openStream();
         this.bundle = new PropertyResourceBundle(is);
         is.close();
      }
      catch (IOException ioe)
      {
         AbstractPlugin.wrapException(ioe);
      }
   }

   /**
    * Gets the string corresponding to a given key
    *
    * @param key  The bundle key
    * @return     The value or the key if not found
    */
   public String getString(String key)
   {
      try
      {
         return bundle.getString(key);
      }
      catch (MissingResourceException e)
      {
         return '!' + key + '!';
      }
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
      try
      {
         String value = bundle.getString(key);
         return MessageFormat.format(value, parameters);
      }
      catch (MissingResourceException e)
      {
         return '!' + key + '!';
      }
   }
}
