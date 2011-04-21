/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.ui.actions;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jboss.ide.eclipse.launcher.core.LauncherPlugin;
import org.jboss.ide.eclipse.launcher.core.ServerLaunchManager;
import org.jboss.ide.eclipse.launcher.core.event.IServerDebugEventListener;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public abstract class AbstractDefaultServerActionDelegate
       implements
      IWorkbenchWindowActionDelegate,
      IServerDebugEventListener,
      ILaunchConfigurationListener,
      IPropertyChangeListener
{
   IAction action;


   /**
    * We can use this method to dispose of any system
    * resources we previously allocated.
    *
    * @see   IWorkbenchWindowActionDelegate#dispose
    */
   public void dispose()
   {
      ServerLaunchManager.getInstance().getDebugEventHandler().removeListener(this);
   }


   /**
    * We will cache window object in order to
    * be able to provide parent shell for the message dialog.
    *
    * @param window  Description of the Parameter
    * @see           IWorkbenchWindowActionDelegate#init
    */
   public void init(IWorkbenchWindow window)
   {
      ServerLaunchManager.getInstance().getDebugEventHandler().addListener(this);
      DebugPlugin.getDefault().getLaunchManager().addLaunchConfigurationListener(this);
      LauncherPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);
   }


   /**
    * @param configuration  Description of the Parameter
    * @see                  org.eclipse.debug.core.ILaunchConfigurationListener#launchConfigurationAdded(ILaunchConfiguration)
    */
   public void launchConfigurationAdded(ILaunchConfiguration configuration) { }


   /**
    * @param configuration  Description of the Parameter
    * @see                  org.eclipse.debug.core.ILaunchConfigurationListener#launchConfigurationChanged(ILaunchConfiguration)
    */
   public void launchConfigurationChanged(ILaunchConfiguration configuration)
   {
      enableAction(ServerLaunchManager.getInstance().getDefaultLaunchConfiguration(), action);
   }


   /**
    * @param configuration  Description of the Parameter
    * @see                  org.eclipse.debug.core.ILaunchConfigurationListener#launchConfigurationRemoved(ILaunchConfiguration)
    */
   public void launchConfigurationRemoved(ILaunchConfiguration configuration)
   {
      enableAction(ServerLaunchManager.getInstance().getDefaultLaunchConfiguration(), action);
   }


   /**
    * @param event  Description of the Parameter
    * @see          org.eclipse.jface.util.IPropertyChangeListener#propertyChange(PropertyChangeEvent)
    */
   public void propertyChange(PropertyChangeEvent event)
   {
      enableAction(ServerLaunchManager.getInstance().getDefaultLaunchConfiguration(), action);
   }


   /**
    * @param action     Description of the Parameter
    * @param selection  Description of the Parameter
    * @see              org.eclipse.ui.IActionDelegate#selectionChanged(IAction, ISelection)
    */
   public void selectionChanged(IAction action, ISelection selection)
   {
      this.action = action;
      enableAction(ServerLaunchManager.getInstance().getDefaultLaunchConfiguration(), action);
   }


   /**
    * @param event          Description of the Parameter
    * @param configuration  Description of the Parameter
    * @see                  org.rocklet.launcher.ui.IServerDebugEventListener#serverEvent(DebugEvent, ILaunchConfiguration)
    */
   public void serverEvent(DebugEvent event, ILaunchConfiguration configuration)
   {
      enableAction(configuration, action);
   }


   /**
    * Description of the Method
    *
    * @param configuration  Description of the Parameter
    * @param action         Description of the Parameter
    */
   protected abstract void enableAction(ILaunchConfiguration configuration, IAction action);
}
