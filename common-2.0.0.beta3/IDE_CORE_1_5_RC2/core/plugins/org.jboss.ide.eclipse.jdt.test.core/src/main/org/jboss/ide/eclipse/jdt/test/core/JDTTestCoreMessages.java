/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.test.core;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Message bundle accessor helper class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JDTTestCoreMessages
{
   private final static String BUNDLE_NAME = JDTTestCoreMessages.class.getName();
   private final static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);


   /** Default Constructor */
   private JDTTestCoreMessages() { }


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
