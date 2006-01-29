/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.launcher.ui.configuration.jboss;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaJRETab;
import org.jboss.ide.eclipse.launcher.core.constants.IJBossConstants;
import org.jboss.ide.eclipse.launcher.core.constants.IServerLaunchConfigurationConstants;
import org.jboss.ide.eclipse.launcher.core.util.LaunchType;
import org.jboss.ide.eclipse.launcher.core.util.ServerLaunchUtil;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIMessages;
import org.jboss.ide.eclipse.launcher.ui.configuration.LogFileTab;
import org.jboss.ide.eclipse.launcher.ui.configuration.ServerLaunchArgumentsTab;
import org.jboss.ide.eclipse.launcher.ui.configuration.ServerTabGroup;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 */
public class JBoss24xTabGroup extends ServerTabGroup
{
   /**
    * @param dialog  Description of the Parameter
    * @param mode    Description of the Parameter
    * @see           org.eclipse.debug.ui.ILaunchConfigurationTabGroup#createTabs(ILaunchConfigurationDialog, String)
    */
   public void createTabs(ILaunchConfigurationDialog dialog, String mode)
   {
      ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[]
      {
            new JBoss24xHomeTab(),
            new ServerLaunchArgumentsTab(IServerLaunchConfigurationConstants.ATTR_USER_PROGRAM_ARGS,
                  IServerLaunchConfigurationConstants.ATTR_USER_VM_ARGS, LauncherUIMessages
                        .getString("JBoss24xTabGroup.start.tab")), //$NON-NLS-1$
            new LogFileTab(), new SourceLookupTab(), new JavaClasspathTab(), new JavaJRETab()};

      setTabs(tabs);
   }

   /**
    * @param configuration  Description of the Parameter
    * @see                  org.eclipse.debug.ui.ILaunchConfigurationTabGroup#performApply(ILaunchConfigurationWorkingCopy)
    */
   public void performApply(ILaunchConfigurationWorkingCopy configuration)
   {
      ServerLaunchUtil.setClasspath(LaunchType.START, configuration, new String[]
      {IServerLaunchConfigurationConstants.ATTR_HOMEDIR_CLASSPATH});

      ServerLaunchUtil
            .setVMArgs(LaunchType.START, configuration, new String[]
            {IServerLaunchConfigurationConstants.ATTR_DEFAULT_VM_ARGS,
                  IServerLaunchConfigurationConstants.ATTR_USER_VM_ARGS});

      ServerLaunchUtil.setProgramArgs(LaunchType.START, configuration, new String[]
      {IServerLaunchConfigurationConstants.ATTR_USER_PROGRAM_ARGS});

      super.performApply(configuration);
   }

   /**
    * @param configuration  The new defaults value
    * @see                  org.eclipse.debug.ui.ILaunchConfigurationTabGroup#setDefaults(ILaunchConfigurationWorkingCopy)
    */
   public void setDefaults(ILaunchConfigurationWorkingCopy configuration)
   {
      ServerLaunchUtil.setRelativeToHomedirClasspath(LaunchType.START, configuration, new String[]
      {IJBossConstants.RUN_JAR_RELATIVE_TO_JBOSS_HOME, IJBossConstants.CRIMSON_JAR_RELATIVE_TO_JBOSS_HOME_2X});

      ServerLaunchUtil.setMainType(LaunchType.START, configuration, IJBossConstants.JBOSS_MAIN_CLASS);

      configuration.setAttribute(IServerLaunchConfigurationConstants.ATTR_USER_VM_ARGS,
            IJBossConstants.SAX_PARSER_FACTORY_OPTION_2X + IJBossConstants.DEFAULT_SAX_PARSER_FACTORY_2X + " " //$NON-NLS-1$
                  + IJBossConstants.DOCUMENT_BUILDER_FACTORY_OPTION_2X
                  + IJBossConstants.DEFAULT_DOCUMENT_BUILDER_FACTORY_2X);

      super.setDefaults(configuration);
   }
}
