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

public class NameEquals extends Condition
{
   private final String name;


   /**
    *Constructor for the NameEquals object
    *
    * @param name  Description of the Parameter
    */
   public NameEquals(String name)
   {
      this.name = name;
   }


   /**
    * Description of the Method
    *
    * @param member  Description of the Parameter
    * @return        Description of the Return Value
    */
   public boolean evalInternal(IMember member)
   {
      return name.equals(member.getElementName());
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      return getClass().getName() + ": " + name;//$NON-NLS-1$
   }
}
