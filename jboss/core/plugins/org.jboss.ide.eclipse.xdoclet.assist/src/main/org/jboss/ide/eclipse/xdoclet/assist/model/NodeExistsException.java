/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class NodeExistsException extends RuntimeException
{

   /** Constructor for NodeExistsException. */
   public NodeExistsException()
   {
      super();
   }


   /**
    * Constructor for NodeExistsException.
    *
    * @param message
    */
   public NodeExistsException(String message)
   {
      super(message);
   }

}
