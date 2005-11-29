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
public class NameStartsWith extends Condition
{
   /** name to check */
   private final String prefix;


   /**
    * Describe what the NameEquals constructor does
    *
    * @param prefix   Describe what the parameter does
    * @todo-javadoc   Write javadocs for method parameter
    * @todo-javadoc   Write javadocs for constructor
    * @todo-javadoc   Write javadocs for method parameter
    */
   public NameStartsWith(String prefix)
   {
      this.prefix = prefix;
   }


   /**
    * Description of the Method
    *
    * @param member  Description of the Parameter
    * @return        Description of the Return Value
    */
   public boolean evalInternal(IMember member)
   {
      return member.getElementName().startsWith(prefix);
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      return getClass().getName() + ": " + prefix;//$NON-NLS-1$
   }
}
