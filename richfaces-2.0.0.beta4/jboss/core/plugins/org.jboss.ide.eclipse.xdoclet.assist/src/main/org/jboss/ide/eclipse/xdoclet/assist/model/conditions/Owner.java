/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import org.eclipse.jdt.core.IMember;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class Owner extends Condition
{
   /** construct Owner condition object */
   public Owner()
   {
      setMaximumNumberOfChildren(Condition.UNLIMITED_NUMBER_OF_CHILDREN);
   }


   /**
    * @param member  Description of the Parameter
    * @return        Description of the Return Value
    * @see           org.jboss.ide.eclipse.xdoclet.model.conditions.Condition#eval(IMember)
    */
   public boolean evalInternal(IMember member)
   {
      return true;
//		JavaElementAnalyzer analyzer = new JavaElementAnalyzer(member);
//		if (analyzer.getType() != null) {
//			Iterator iterator = getChildConditions().iterator();
//			if (iterator.hasNext()) {
//				Condition cond = (Condition) iterator.next();
//				return cond.eval(analyzer.getType());
//			} else {
//				// nothing to evaluate. return true
//				return true;
//			}
//		} else {
//			return false;
//		}
   }


   /**
    * convert to string value
    *
    * @return   string representation
    */
   public String toString()
   {
      return getClass().getName();
   }
}
