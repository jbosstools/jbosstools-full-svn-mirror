/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.ui.configuration.jboss;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;
import org.jboss.ide.eclipse.launcher.core.constants.IJBossConstants;
import org.jboss.ide.eclipse.launcher.core.constants.IServerLaunchConfigurationConstants;
import org.jboss.ide.eclipse.launcher.core.util.LaunchType;
import org.jboss.ide.eclipse.launcher.core.util.ServerLaunchUtil;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIMessages;
import org.jboss.ide.eclipse.launcher.ui.configuration.JDKTab;
import org.jboss.ide.eclipse.launcher.ui.configuration.LogFileTab;
import org.jboss.ide.eclipse.launcher.ui.configuration.ServerLaunchArgumentsTab;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JBoss40xTabGroup extends JBoss32xTabGroup
{
   /**
    * @param dialog  Description of the Parameter
    * @param mode    Description of the Parameter
    * @see           org.eclipse.debug.ui.ILaunchConfigurationTabGroup#createTabs(ILaunchConfigurationDialog, String)
    */
   public void createTabs(ILaunchConfigurationDialog dialog, String mode)
   {
      ILaunchConfigurationTab[] tabs =
            new ILaunchConfigurationTab[]{
            new JBoss40xHomeTab(),
            new ServerLaunchArgumentsTab(
            IServerLaunchConfigurationConstants.ATTR_USER_PROGRAM_ARGS,
            IServerLaunchConfigurationConstants.ATTR_USER_VM_ARGS,
            LauncherUIMessages.getString("JBoss40xTabGroup.start.tab")), //$NON-NLS-1$
            new ServerLaunchArgumentsTab(
            IServerLaunchConfigurationConstants.ATTR_USER_SHUTDOWN_PROGRAM_ARGS,
            IServerLaunchConfigurationConstants.ATTR_USER_SHUTDOWN_VM_ARGS,
            LauncherUIMessages.getString("JBoss40xTabGroup.shutdown.tab")), //$NON-NLS-1$
            new LogFileTab(),
			new SourceLookupTab(),
            new JavaClasspathTab(),
            new JDKTab()};

      setTabs(tabs);
   }


   /**
    * @param configuration  The new defaults value
    * @see                  org.eclipse.debug.ui.ILaunchConfigurationTabGroup#setDefaults(ILaunchConfigurationWorkingCopy)
    */
   public void setDefaults(ILaunchConfigurationWorkingCopy configuration)
   {
      super.setDefaults(configuration);

      ServerLaunchUtil.setRelativeToHomedirClasspath(LaunchType.SHUTDOWN,
            configuration,
            new String[]{IJBossConstants.JBOSSALL_CLIENT_RELATIVE_TO_JBOSS_HOME_4X,
            IJBossConstants.SHUTDOWN_JAR_RELATIVE_TO_JBOSS_HOME_3X});
   }
}
