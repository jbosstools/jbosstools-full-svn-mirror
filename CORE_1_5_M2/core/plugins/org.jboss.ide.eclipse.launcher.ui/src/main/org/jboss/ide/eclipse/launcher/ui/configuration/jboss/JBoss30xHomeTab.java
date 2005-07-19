/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.ui.configuration.jboss;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.launcher.core.constants.IServerLaunchConfigurationConstants;
import org.jboss.ide.eclipse.launcher.core.util.ServerLaunchUtil;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIMessages;
import org.jboss.ide.eclipse.launcher.ui.configuration.HomeTab;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class JBoss30xHomeTab extends HomeTab
{
   /** Constructor for JBoss3xHomeTab */
   public JBoss30xHomeTab()
   {
      this(LauncherUIMessages.getString("JBoss30xHomeTab.JBoss_3.0.x_Home_Directory_1"));//$NON-NLS-1$
   }


   /**
    *Constructor for the JBoss30xHomeTab object
    *
    * @param title  Description of the Parameter
    */
   public JBoss30xHomeTab(String title)
   {
      super(title);
   }


   /**
    * @param launchConfig  Description of the Parameter
    * @return              The valid value
    * @see                 org.eclipse.debug.ui.ILaunchConfigurationTab#isValid(ILaunchConfiguration)
    */
   public boolean isValid(ILaunchConfiguration launchConfig)
   {
      setErrorMessage(null);
      setMessage(null);
      try
      {
         if (!ServerLaunchUtil.isValidDirectory(
               launchConfig,
               homedirText.getText(),
               IServerLaunchConfigurationConstants.ATTR_RELATIVE_TO_HOMEDIR_CLASSPATH)
               || !ServerLaunchUtil.isValidDirectory(
               launchConfig,
               homedirText.getText(),
               IServerLaunchConfigurationConstants.ATTR_RELATIVE_TO_HOMEDIR_SHUTDOWN_CLASSPATH))
         {
            setErrorMessage(getMissingJBossDirectory());
            return false;
         }
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
      }
      return true;
   }


   /**
    * Gets the missingJBossDirectory attribute of the JBoss30xHomeTab object
    *
    * @return   The missingJBossDirectory value
    */
   protected String getMissingJBossDirectory()
   {
      return LauncherUIMessages.getString("JBoss30xHomeTab.No_JBoss_3.0.x_Directory_2");//$NON-NLS-1$
   }
}
