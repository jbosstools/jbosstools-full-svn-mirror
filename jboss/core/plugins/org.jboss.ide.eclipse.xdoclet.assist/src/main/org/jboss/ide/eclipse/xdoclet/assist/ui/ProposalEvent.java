/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.ui;

import java.util.EventObject;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class ProposalEvent extends EventObject
{
   /** Description of the Field */
   protected Object[] proposals;


   /**
    * Constructor for ProposalEvent.
    *
    * @param source
    * @param proposals  Description of the Parameter
    */
   public ProposalEvent(Object source, Object[] proposals)
   {
      super(source);
      this.proposals = proposals;
   }


   /**
    * Returns the selection.
    *
    * @return   Object
    */
   public Object[] getProposals()
   {
      return proposals;
   }

}
