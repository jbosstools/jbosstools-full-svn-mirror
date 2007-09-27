package org.jboss.ide.eclipse.firstrun;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class FirstRunMessages {
   private final static String BUNDLE_NAME = FirstRunMessages.class.getName();
   private final static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

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
