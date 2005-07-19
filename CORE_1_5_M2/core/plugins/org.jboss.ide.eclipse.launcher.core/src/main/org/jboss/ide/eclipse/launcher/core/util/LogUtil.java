/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.core.util;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class LogUtil
{
   /** Description of the Field */
   public final static int POLLING_INTERVALL_LIMIT = 0;


   /**
    * Gets the validPollingIntervall attribute of the LogUtil class
    *
    * @param i  Description of the Parameter
    * @return   The validPollingIntervall value
    */
   public static boolean isValidPollingIntervall(int i)
   {
      if (i < POLLING_INTERVALL_LIMIT)
      {
         return false;
      }
      return true;
   }
}
