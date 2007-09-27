/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
