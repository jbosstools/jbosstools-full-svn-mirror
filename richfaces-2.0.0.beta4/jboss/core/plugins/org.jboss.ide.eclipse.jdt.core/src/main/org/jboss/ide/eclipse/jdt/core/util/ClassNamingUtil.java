/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.core.util;


/**
 * Utility class for method and property naming
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ClassNamingUtil
{
   /** Avoid instantiation */
   private ClassNamingUtil() { }


   /**
    * Description of the Method
    *
    * @param name  Description of the Parameter
    * @return      Description of the Return Value
    */
   public static String capitalize(String name)
   {
      if (name == null || name.length() == 0)
      {
         return name;
      }
      if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0)))
      {
         return name;
      }
      char chars[] = name.toCharArray();
      chars[0] = Character.toUpperCase(chars[0]);
      return new String(chars);
   }


   /**
    * Description of the Method
    *
    * @param name  Description of the Parameter
    * @return      Description of the Return Value
    */
   public static String decapitalize(String name)
   {
      if (name == null || name.length() == 0)
      {
         return name;
      }
      if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0)))
      {
         return name;
      }
      char chars[] = name.toCharArray();
      chars[0] = Character.toLowerCase(chars[0]);
      return new String(chars);
   }


   /**
    * Description of the Method
    *
    * @param name  Description of the Parameter
    * @return      Description of the Return Value
    */
   public static String getterName(String name)
   {
      String capString = capitalize(name);
      return "get" + capString;//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param name      Description of the Parameter
    * @param aBoolean  Description of the Parameter
    * @return          Description of the Return Value
    */
   public static String getterName(String name, boolean aBoolean)
   {
      String capString = capitalize(name);
      return "is" + capString;//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param name  Description of the Parameter
    * @return      Description of the Return Value
    */
   public static String setterName(String name)
   {
      String capString = capitalize(name);
      return "set" + capString;//$NON-NLS-1$
   }
}
