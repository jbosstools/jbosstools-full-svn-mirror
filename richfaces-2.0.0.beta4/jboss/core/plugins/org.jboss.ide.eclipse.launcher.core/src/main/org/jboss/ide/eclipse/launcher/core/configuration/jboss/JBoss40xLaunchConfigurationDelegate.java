/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.core.configuration.jboss;

import org.jboss.ide.eclipse.launcher.core.util.JBossType;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JBoss40xLaunchConfigurationDelegate
       extends JBoss32xLaunchConfigurationDelegate
{
   /**
    * Gets the type attribute of the JBoss32xLaunchConfigurationDelegate object
    *
    * @return   The type value
    */
   public JBossType getType()
   {
      return JBossType.JBoss_4_0_X;
   }
}
