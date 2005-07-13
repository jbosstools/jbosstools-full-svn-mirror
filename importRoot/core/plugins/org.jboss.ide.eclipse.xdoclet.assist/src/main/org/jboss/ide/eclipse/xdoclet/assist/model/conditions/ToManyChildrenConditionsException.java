/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class ToManyChildrenConditionsException extends ConditionException
{
   /** Constructor for ConditionExistsException. */
   public ToManyChildrenConditionsException()
   {
      super();
   }


   /**
    * Constructor for ConditionExistsException.
    *
    * @param message
    */
   public ToManyChildrenConditionsException(String message)
   {
      super(message);
   }

}
