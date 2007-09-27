/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import org.eclipse.jdt.core.IMember;

/**
 * Evaluates always to true
 *
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class AlwaysTrue extends Condition
{

   /** Constructor for And. */
   public AlwaysTrue()
   {
      super();
      setMaximumNumberOfChildren(0);
   }


   /**
    * Description of the Method
    *
    * @param member  Description of the Parameter
    * @return        Description of the Return Value
    */
   public boolean evalInternal(IMember member)
   {
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
