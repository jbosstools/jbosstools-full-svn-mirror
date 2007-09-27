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
 */
public class JBoss24xHomeTab extends HomeTab
{
   /** Constructor for JBoss2xHomeTab. */
   public JBoss24xHomeTab()
   {
      this(LauncherUIMessages.getString("JBoss24xHomeTab.JBoss_2.4.x_Home_Directory_1"));//$NON-NLS-1$
   }


   /**
    *Constructor for the JBoss24xHomeTab object
    *
    * @param title  Description of the Parameter
    */
   public JBoss24xHomeTab(String title)
   {
      super(title, false);
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
               IServerLaunchConfigurationConstants.ATTR_RELATIVE_TO_HOMEDIR_CLASSPATH))
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
    * Gets the missingJBossDirectory attribute of the JBoss24xHomeTab object
    *
    * @return   The missingJBossDirectory value
    */
   protected String getMissingJBossDirectory()
   {
      return LauncherUIMessages.getString("JBoss24xHomeTab.No_JBoss_2.4.x_directory_2");//$NON-NLS-1$
   }
}
