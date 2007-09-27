/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.ui.configuration;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.jboss.ide.eclipse.launcher.core.constants.IServerLaunchConfigurationConstants;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public abstract class AbstractServerTabGroup extends AbstractLaunchConfigurationTabGroup
{
   /**
    * Set a validation attribute after performApply
    *
    * @param configuration  Description of the Parameter
    * @see                  org.eclipse.debug.ui.ILaunchConfigurationTabGroup#performApply(ILaunchConfigurationWorkingCopy)
    */
   public void performApply(ILaunchConfigurationWorkingCopy configuration)
   {
      super.performApply(configuration);
      ILaunchConfigurationTab[] tabs = getTabs();
      configuration.setAttribute(IServerLaunchConfigurationConstants.ATTR_CONFIGURATION_ERROR, false);
      for (int i = 0; i < tabs.length; i++)
      {
         if (!tabs[i].isValid(configuration))
         {
            configuration.setAttribute(IServerLaunchConfigurationConstants.ATTR_CONFIGURATION_ERROR, true);
            break;
         }
      }
   }
}
