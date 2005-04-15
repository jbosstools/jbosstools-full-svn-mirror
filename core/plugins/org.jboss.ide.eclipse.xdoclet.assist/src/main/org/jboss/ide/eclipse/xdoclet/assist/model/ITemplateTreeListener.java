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
public interface ITemplateTreeListener
{
   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    */
   public void elementAdded(TemplateEvent event);


   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    */
   public void elementRemoved(TemplateEvent event);


   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    */
   public void treeChanged(TemplateEvent event);
}
