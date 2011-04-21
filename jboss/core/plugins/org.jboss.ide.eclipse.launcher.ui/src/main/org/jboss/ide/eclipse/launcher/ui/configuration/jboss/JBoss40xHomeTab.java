/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.ui.configuration.jboss;

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
   protected String getMissingJBossDirectory()
   {
      return LauncherUIMessages.getString("JBoss40xHomeTab.No_JBoss_4.0.x_Directory_2");//$NON-NLS-1$
   }
}
