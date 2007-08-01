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
   private ClassNamingUtil()
   {
   }

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
