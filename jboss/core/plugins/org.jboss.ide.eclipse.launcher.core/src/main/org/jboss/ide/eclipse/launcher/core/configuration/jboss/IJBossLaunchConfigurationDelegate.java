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
 * @created   28 juillet 2003
 */
public interface IJBossLaunchConfigurationDelegate
{
   /**
    * Gets the type attribute of the IJBossLaunchConfigurationDelegate object
    *
    * @return   The type value
    */
   public JBossType getType();
}
