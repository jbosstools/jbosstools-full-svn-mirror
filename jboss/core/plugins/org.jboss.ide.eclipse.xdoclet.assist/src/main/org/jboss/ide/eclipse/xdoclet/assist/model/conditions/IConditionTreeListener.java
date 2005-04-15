/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import java.util.EventObject;

/**
 * @author    Hans Dockter
 *
 * Description
 * @version   $Revision$
 * @created   17 mai 2003
 */
public interface IConditionTreeListener
{
   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    */
   public void removed(EventObject event);


   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    */
   public void childAdded(EventObject event);
}
