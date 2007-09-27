/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;

/**
 * This condition evaluates to true if a XProgramElement is a method
 *
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class IsMethod extends Condition
{
   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      return getClass().getName();
   }


   /**
    * Description of the Method
    *
    * @param javaElement  Description of the Parameter
    * @return             Description of the Return Value
    */
   protected boolean evalInternal(IMember javaElement)
   {
      return (javaElement instanceof IMethod);
   }

}
