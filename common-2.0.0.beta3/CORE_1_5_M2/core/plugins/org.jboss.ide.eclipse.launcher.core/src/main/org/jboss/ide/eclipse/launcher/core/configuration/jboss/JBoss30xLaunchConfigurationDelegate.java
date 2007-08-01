/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.core.configuration.jboss;

import org.jboss.ide.eclipse.launcher.core.configuration.ServerLaunchConfigurationDelegate;
import org.jboss.ide.eclipse.launcher.core.util.JBossType;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class JBoss30xLaunchConfigurationDelegate
       extends ServerLaunchConfigurationDelegate
       implements IJBossLaunchConfigurationDelegate
{

   /**
    * Gets the type attribute of the JBoss30xLaunchConfigurationDelegate object
    *
    * @return   The type value
    */
   public JBossType getType()
   {
      return JBossType.JBoss_3_0_X;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean hasShutdown()
   {
      return true;
   }
}
