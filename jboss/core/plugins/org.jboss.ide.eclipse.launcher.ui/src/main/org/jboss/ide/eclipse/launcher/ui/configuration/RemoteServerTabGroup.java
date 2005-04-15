/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.ui.configuration;

import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class RemoteServerTabGroup extends AbstractServerTabGroup
{
   /**
    * @param dialog  Description of the Parameter
    * @param mode    Description of the Parameter
    * @see           org.eclipse.debug.ui.ILaunchConfigurationTabGroup#createTabs(ILaunchConfigurationDialog, String)
    */
   public void createTabs(ILaunchConfigurationDialog dialog, String mode)
   {
      ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[]{
            new RemoteServerConnectTab(),
            new LogFileTab()};
      setTabs(tabs);
   }
}
