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
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards;

/**
 * Generation templates accessor helper class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class J2EENamingUtil
{
   /** Default Constructor */
   private J2EENamingUtil()
   {
   }

   /**
    * Description of the Method
    *
    * @param classname  Description of the Parameter
    * @return           Description of the Return Value
    */
   public static String stripEJBSuffix(String classname)
   {
      String result = classname;

      result = stripSuffix(result, new String[]
      {"Bean", "EJB"});//$NON-NLS-1$ //$NON-NLS-2$

      return result;
   }

   /**
    * Description of the Method
    *
    * @param classname  Description of the Parameter
    * @return           Description of the Return Value
    */
   public static String stripFilterSuffix(String classname)
   {
      String result = classname;

      result = stripSuffix(result, new String[]
      {"Filter"});//$NON-NLS-1$

      return result;
   }

   /**
    * Description of the Method
    *
    * @param classname  Description of the Parameter
    * @return           Description of the Return Value
    */
   public static String stripServletSuffix(String classname)
   {
      String result = classname;

      result = stripSuffix(result, new String[]
      {"Servlet"});//$NON-NLS-1$

      return result;
   }

   /**
    * Description of the Method
    *
    * @param classname  Description of the Parameter
    * @return           Description of the Return Value
    */
   public static String stripTagSuffix(String classname)
   {
      String result = classname;

      result = stripSuffix(result, new String[]
      {"Tag"});//$NON-NLS-1$

      return result;
   }

   /**
    * Description of the Method
    *
    * @param classname  Description of the Parameter
    * @param suffixes   Description of the Parameter
    * @return           Description of the Return Value
    */
   private static String stripSuffix(String classname, String[] suffixes)
   {
      String result = classname;

      for (int i = 0; i < suffixes.length; i++)
      {
         String suffix = suffixes[i];
         if (result.endsWith(suffix))
         {
            result = result.substring(0, result.indexOf(suffix));
            break;
         }
      }

      return result;
   }
}
