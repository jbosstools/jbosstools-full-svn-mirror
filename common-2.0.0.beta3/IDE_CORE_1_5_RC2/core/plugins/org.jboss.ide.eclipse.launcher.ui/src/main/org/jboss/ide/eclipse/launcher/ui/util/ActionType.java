/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.ui.util;

import org.jboss.ide.eclipse.core.util.NamedType;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIMessages;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class ActionType extends NamedType
{
   /** Description of the Field */
   public final static ActionType SHUTDOWN = new ActionType(LauncherUIMessages.getString("ActionType.shutdown_2"));//$NON-NLS-1$
   /** Description of the Field */
   public final static ActionType START = new ActionType(LauncherUIMessages.getString("ActionType.start_1"));//$NON-NLS-1$
   /** Description of the Field */
   public final static ActionType TERMINATE = new ActionType(LauncherUIMessages.getString("ActionType.terminate_3"));//$NON-NLS-1$


   /**
    *Constructor for the ActionType object
    *
    * @param name  Description of the Parameter
    */
   private ActionType(String name)
   {
      super(name);
   }
}
