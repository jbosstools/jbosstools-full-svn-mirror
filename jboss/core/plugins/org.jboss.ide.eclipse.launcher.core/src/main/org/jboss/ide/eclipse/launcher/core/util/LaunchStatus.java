/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.core.util;

import org.jboss.ide.eclipse.core.util.NamedType;
import org.jboss.ide.eclipse.launcher.core.LauncherCoreMessages;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class LaunchStatus extends NamedType
{
   /** Description of the Field */
   public final static LaunchStatus CONFIGURE_ERROR = new LaunchStatus(LauncherCoreMessages.getString("LaunchStatusconfigure_error_3"));//$NON-NLS-1$
   /** Description of the Field */
   public final static LaunchStatus NOT_RUNNING = new LaunchStatus(LauncherCoreMessages.getString("LaunchStatusnot_running_2"));//$NON-NLS-1$
   /** Description of the Field */
   public final static LaunchStatus RUNNING = new LaunchStatus(LauncherCoreMessages.getString("LaunchStatusrunning_1"));//$NON-NLS-1$


   /**
    *Constructor for the LaunchStatus object
    *
    * @param name  Description of the Parameter
    */
   private LaunchStatus(String name)
   {
      super(name);
   }
}
