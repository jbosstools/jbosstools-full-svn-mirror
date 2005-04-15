/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */

public class IsPublic extends Condition
{
   /** evaluate public status of program element */
   public IsPublic() { }


   /**
    * evaluate public status of program element
    *
    * @param member                  Description of the Parameter
    * @return                        public status of program element
    * @exception JavaModelException  Description of the Exception
    */
   public boolean evalInternal(IMember member) throws JavaModelException
   {
      return Flags.isPublic(member.getFlags());
   }


   /**
    * return string representation
    *
    * @return   string representation
    */
   public String toString()
   {
      return getClass().getName();
   }
}
