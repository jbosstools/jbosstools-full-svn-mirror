/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.core.event;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public interface IServerDebugEventListener
{
   /**
    * Description of the Method
    *
    * @param event          Description of the Parameter
    * @param configuration  Description of the Parameter
    */
   public void serverEvent(DebugEvent event, ILaunchConfiguration configuration);
}
