/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.run;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Message bundle accessor helper class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XDocletRunMessages
{
   private final static String BUNDLE_NAME = "org.jboss.ide.eclipse.xdoclet.run.XDocletRunMessages";//$NON-NLS-1$
   private final static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);


   /** Default Constructor */
   private XDocletRunMessages() { }


   /**
    * Gets the string corresponding to a given key
    *
    * @param key  The bundle key
    * @return     The internationalized value or the key if not found
    */
   public static String getString(String key)
   {
      try
      {
         return RESOURCE_BUNDLE.getString(key);
      }
      catch (MissingResourceException e)
      {
         return '!' + key + '!';
      }
   }
}
