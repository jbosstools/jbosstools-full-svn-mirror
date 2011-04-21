/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMember;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class IsField extends Condition
{
   /**
    * string representation of this class
    *
    * @return   string representation of class
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
      return (member instanceof IField);
   }

}

