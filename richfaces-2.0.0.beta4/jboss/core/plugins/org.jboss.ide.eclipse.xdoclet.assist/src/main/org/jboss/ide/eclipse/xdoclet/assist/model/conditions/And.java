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
 * Evaluates to true if all the conditions contained evaluate to true. Evaluates
 * to true if there are no nested conditions
 *
 * @author         Aslak Hellesøy
 * @version        $Revision$
 * @created        14. januar 2002
 * @todo-javadoc   Write javadocs
 */
public class And extends Condition
{

   /** Constructor for And. */
   public And()
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
      Iterator iterator = getChildConditions().iterator();
      while (iterator.hasNext())
      {
         Condition condition = (Condition) iterator.next();
         if (!condition.eval(member))
         {
            return false;
         }
      }
      return true;
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
