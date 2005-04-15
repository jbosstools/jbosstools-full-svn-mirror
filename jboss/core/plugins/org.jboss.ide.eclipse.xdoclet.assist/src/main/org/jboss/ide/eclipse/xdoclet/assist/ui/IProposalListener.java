/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.ui;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public interface IProposalListener
{
   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    */
   public void handleProposal(ProposalEvent event);


   /** Description of the Method */
   public void closedWithoutProposals();
}
