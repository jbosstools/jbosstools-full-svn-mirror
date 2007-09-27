/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class IsConstructor extends Condition
{
   /**
    * produce human readable string
    *
    * @return   human readable string descripbing this condition
    */
   public String toString()
   {
      return getClass().getName();
   }


   /**
    * Description of the Method
    *
    * @param member                  Description of the Parameter
    * @return                        Description of the Return Value
    * @exception JavaModelException  Description of the Exception
    */
   protected boolean evalInternal(IMember member) throws JavaModelException
   {
      if (member instanceof IMethod)
      {
         return ((IMethod) member).isConstructor();
      }
      return false;
   }

}
