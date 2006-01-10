/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
   protected String getMissingEntryErrorMessage (String entry)
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
