/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import java.util.Iterator;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class Or extends Condition
{
   /** Constructor for Or. */
   public Or()
   {
      super();
      setMaximumNumberOfChildren(Condition.UNLIMITED_NUMBER_OF_CHILDREN);
   }


   /**
    * Description of the Method
    *
    * @param member                  Description of the Parameter
    * @return                        Description of the Return Value
    * @exception JavaModelException  Description of the Exception
    */
   public boolean evalInternal(IMember member) throws JavaModelException
   {
      if (getChildConditionsCount() == 0)
      {
         return true;
      }
      Iterator iterator = getChildConditions().iterator();
      while (iterator.hasNext())
      {
         Condition condition = (Condition) iterator.next();
         if (condition.eval(member))
         {
            return true;
         }
      }
      return false;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      return getClass().getName();
   }
}
