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
