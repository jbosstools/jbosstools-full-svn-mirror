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

import org.jboss.ide.eclipse.launcher.core.constants.IJBossConstants;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIMessages;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class JBoss40xHomeTab extends JBoss30xHomeTab
{
   /** Constructor for JBoss3xHomeTab */
   public JBoss40xHomeTab()
   {
      super(LauncherUIMessages.getString("JBoss40xHomeTab.JBoss_4.0.x_Home_Directory_1"));//$NON-NLS-1$
   }

   /**
    * Gets the missingJBossDirectory attribute of the JBoss40xHomeTab object
    *
    * @return   The missingJBossDirectory value
    */
   protected String getMissingEntryErrorMessage(String entry)
   {
      String message = super.getMissingEntryErrorMessage(entry);
      if (message == null)
      {
         if (entry.equals(IJBossConstants.JBOSSALL_CLIENT_RELATIVE_TO_JBOSS_HOME_4X))
         {
            message = LauncherUIMessages.getString("JBoss40xHomeTab.Missing_JBoss_All_Client_Jar");
         }
      }
      return message;
   }
}
