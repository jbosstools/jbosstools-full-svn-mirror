/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.ui.views;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.launcher.core.ServerLaunchManager;
import org.jboss.ide.eclipse.launcher.core.constants.IJBossConstants;
import org.jboss.ide.eclipse.launcher.core.logfiles.LogFile;
import org.jboss.ide.eclipse.launcher.core.util.LaunchStatus;
import org.jboss.ide.eclipse.launcher.ui.ILauncherUIConstants;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIImages;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIMessages;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class ServerNavigatorLabelProvider extends LabelProvider
{
   /**
    * @param obj  Description of the Parameter
    * @return     The image value
    * @see        org.eclipse.jface.viewers.ILabelProvider#getImage(Object)
    */
   public Image getImage(Object obj)
   {
      if (obj instanceof ILaunchConfiguration)
      {
         ILaunchConfiguration configuration = (ILaunchConfiguration) obj;
         try
         {
            if (ServerLaunchManager.getInstance().getStatusForStartLaunch(configuration) == LaunchStatus.RUNNING)
            {
               return LauncherUIImages.getImage(ILauncherUIConstants.IMG_OBJS_SERVER_RUNNING);
            }
            return LauncherUIImages.getImage(ILauncherUIConstants.IMG_OBJS_SERVER_NOT_RUNNING);
         }
         catch (DebugException e)
         {
            AbstractPlugin.log(e);
            return null;
         }
      }
      return LauncherUIImages.getImage(ILauncherUIConstants.IMG_OBJS_LOGFILE);
   }


   /**
    * @param element  Description of the Parameter
    * @return         The text value
    * @see            org.eclipse.jface.viewers.ILabelProvider#getText(Object)
    */
   public String getText(Object element)
   {
      if (element instanceof ILaunchConfiguration)
      {
         ILaunchConfiguration configuration = (ILaunchConfiguration) element;
         String label;
         try
         {
            label = configuration.getType().getName() + ": " //$NON-NLS-1$
            + configuration.getName();
            
            String serverConfiguration = configuration.getAttribute(IJBossConstants.ATTR_SERVER_CONFIGURATION, "default");
            label += " [" + serverConfiguration + "]";
            
            // String suffix;
            if (ServerLaunchManager.getInstance().getStatusForStartLaunch(configuration) == LaunchStatus.RUNNING)
            {
               return label + " (" + LaunchStatus.RUNNING.getName() + ")";//$NON-NLS-1$ //$NON-NLS-2$
            }
            else if (ServerLaunchManager.getInstance().isValid(configuration))
            {
               return label + " (" //$NON-NLS-1$
               + LaunchStatus.NOT_RUNNING.getName() + ")";//$NON-NLS-1$
            }
            else
            {
               return label
                     + " (" //$NON-NLS-1$
               + LauncherUIMessages.getString("LauncherTreeLabelProvider.Configure_Error_7") //$NON-NLS-1$
               + ")";//$NON-NLS-1$
            }
         }
         catch (CoreException e)
         {
            AbstractPlugin.log(e);
            return "";//$NON-NLS-1$
         }
      }
      return ((LogFile) element).getFileName();
   }
}
