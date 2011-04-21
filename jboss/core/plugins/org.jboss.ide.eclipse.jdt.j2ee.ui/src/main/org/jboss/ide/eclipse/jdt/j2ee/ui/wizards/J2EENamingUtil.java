/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
   private J2EENamingUtil() { }


   /**
    * Description of the Method
    *
    * @param classname  Description of the Parameter
    * @return           Description of the Return Value
    */
   public static String stripEJBSuffix(String classname)
   {
      String result = classname;

      result = stripSuffix(result, new String[]{"Bean", "EJB"});//$NON-NLS-1$ //$NON-NLS-2$

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

      result = stripSuffix(result, new String[]{"Filter"});//$NON-NLS-1$

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

      result = stripSuffix(result, new String[]{"Servlet"});//$NON-NLS-1$

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

      result = stripSuffix(result, new String[]{"Tag"});//$NON-NLS-1$

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
