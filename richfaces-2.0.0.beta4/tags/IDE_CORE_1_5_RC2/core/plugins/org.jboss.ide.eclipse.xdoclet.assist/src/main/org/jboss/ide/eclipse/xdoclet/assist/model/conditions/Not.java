/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class Not extends Condition
{
   /**Constructor for the Not object */
   public Not()
   {
      super();
      setMaximumNumberOfChildren(1);
   }


   /**
    * @param condition               The feature to be added to the ChildCondition attribute
    * @exception ConditionException  Description of the Exception
    * @see                           org.jboss.ide.eclipse.xdoclet.model.conditions.Condition#addChildCondition(org.jboss.ide.eclipse.xdoclet.model.conditions.Condition)
    */
   public void addChildCondition(Condition condition) throws ConditionException
   {
      super.addChildCondition(condition);
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
      if (getChildConditionsCount() < 1)
      {
         throw new IllegalStateException("You must nest a condition into <not>");//$NON-NLS-1$
      }
      return !((Condition) getChildConditions().get(0)).eval(member);
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
