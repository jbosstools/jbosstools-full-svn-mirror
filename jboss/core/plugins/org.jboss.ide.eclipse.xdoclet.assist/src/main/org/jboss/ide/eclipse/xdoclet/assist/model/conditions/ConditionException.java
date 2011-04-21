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
public class ConditionException extends Exception
{

   /** Constructor for ConditionException. */
   public ConditionException()
   {
      super();
   }


   /**
    * Constructor for ConditionException.
    *
    * @param message
    */
   public ConditionException(String message)
   {
      super(message);
   }

}
