/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.ui.actions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.action.IAction;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.launcher.core.LauncherPlugin;
import org.jboss.ide.eclipse.launcher.core.ServerLaunchManager;
import org.jboss.ide.eclipse.launcher.ui.constants.ILauncherUIConstants;
import org.jboss.ide.eclipse.launcher.ui.util.ServerLaunchUIUtil;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class ShowLogViewDefaultServerActionDelegate extends AbstractDefaultServerActionDelegate
{
   /**
    * @param action  Description of the Parameter
    * @see           IWorkbenchWindowActionDelegate#run
    */
   public void run(IAction action)
   {
      try
      {
         ServerLaunchUIUtil.showView(ILauncherUIConstants.VIEW_ID);
      }
      catch (Exception e)
      {
         AbstractPlugin.log(e);
         LauncherPlugin.getDefault().showErrorMessage(e.getMessage());
      }
   }


   /**
    * Description of the Method
    *
    * @param configuration  Description of the Parameter
    * @param action         Description of the Parameter
    */
   protected void enableAction(ILaunchConfiguration configuration, IAction action)
   {
      try
      {
         action.setEnabled(ServerLaunchManager.getInstance().getLogFiles(configuration).length > 0);
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
         LauncherPlugin.getDefault().showErrorMessage(e);
      }
   }
}
