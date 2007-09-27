/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class IsClass extends Condition
{

   /**
    * Describe what the method does
    *
    * @return         Describe the return value
    * @todo-javadoc   Write javadocs for method parameter
    * @todo-javadoc   Write javadocs for method
    * @todo-javadoc   Write javadocs for return value
    */
   public String toString()
   {
      return getClass().getName();
   }


   /**
    * Description of the Method
    *
    * @param member  Description of the Parameter
    * @return        Description of the Return Value
    */
   protected boolean evalInternal(IMember member)
   {
      return (member instanceof IType);
   }

}
