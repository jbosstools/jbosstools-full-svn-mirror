/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.ui.views;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.part.ViewPart;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.launcher.core.LauncherPlugin;
import org.jboss.ide.eclipse.launcher.core.ServerLaunchManager;
import org.jboss.ide.eclipse.launcher.core.event.IServerDebugEventListener;
import org.jboss.ide.eclipse.launcher.core.logfiles.LogFile;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIImages;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIMessages;
import org.jboss.ide.eclipse.launcher.ui.constants.ILauncherUIConstants;
import org.jboss.ide.eclipse.launcher.ui.logfiles.LogFileViewer;
import org.jboss.ide.eclipse.launcher.ui.util.ServerLaunchUIUtil;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class ServerNavigator extends ViewPart implements ISelectionChangedListener, ILaunchConfigurationListener, IServerDebugEventListener, IDoubleClickListener
{
   private Action configurationAction;
   private Action openLogFileAction;
   //private Action selectionAction;
   private Action shutdownAction;
   private Action startAction;
   private Action terminateAction;

   private TreeViewer viewer;


   /**
    * Description of the Method
    *
    * @param r  Description of the Parameter
    */
   public void asynchExec(Runnable r)
   {
      if (isAvailable())
      {
         viewer.getControl().getDisplay().asyncExec(r);
      }
   }


   /**
    * This is a callback that will allow us to create the viewer and initialize
    * it.
    *
    * @param parent  Description of the Parameter
    */
   public void createPartControl(Composite parent)
   {
      try
      {
         viewer = new TreeViewer(parent, SWT.NONE);
         viewer.setContentProvider(new ServerNavigatorContentProvider());
         viewer.setLabelProvider(new ServerNavigatorLabelProvider());
         viewer.setSorter(new NameSorter());
         viewer.setInput(ServerLaunchManager.getInstance().getServerConfigurations());
         viewer.addSelectionChangedListener(this);
         viewer.addDoubleClickListener(this);
         DebugPlugin.getDefault().getLaunchManager().addLaunchConfigurationListener(this);
         ServerLaunchManager.getInstance().getDebugEventHandler().addListener(this);
         makeActions();
         initContextMenu();
         initDoubleClickAction();
         contributeToActionBars();
         ILaunchConfiguration[] configurations = ServerLaunchManager.getInstance().getServerConfigurations();
         for (int i = 0; i < configurations.length; i++)
         {
            viewer.update(configurations[i], null);
         }
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
         showErrorMessage(e);
      }
   }


   /**
    * @see   org.eclipse.ui.IWorkbenchPart#dispose()
    */
   public void dispose()
   {
      super.dispose();
      DebugPlugin.getDefault().getLaunchManager().removeLaunchConfigurationListener(this);
      ServerLaunchManager.getInstance().getDebugEventHandler().removeListener(this);
   }


   /** Description of the Method */
   public void doConfiguration()
   {
      IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
      String identifier = null;
      ILaunchConfiguration configuration = null;
      Object selected = selection.getFirstElement();
      if (selected != null)
      {
         if (selected instanceof ILaunchConfiguration)
         {
            configuration = (ILaunchConfiguration) selected;
         }
         else
         {
            configuration = ((LogFile) selected).getConfiguration();
         }
      }
      if (configuration != null)
      {
         selection = new StructuredSelection(configuration);
         identifier = DebugUITools.getLaunchGroup(configuration, ILaunchManager.DEBUG_MODE).getIdentifier();
      }
      else
      {
         selection = null;
         identifier = DebugUIPlugin.getDefault().getLaunchConfigurationManager().getDefaultLanuchGroup(ILaunchManager.DEBUG_MODE).getIdentifier();
      }
      DebugUITools.openLaunchConfigurationDialogOnGroup(AbstractPlugin.getShell(), selection, identifier);
   }


   /** Description of the Method */
   public void doShutdown()
   {
      try
      {
         ServerLaunchManager.getInstance().shutDown(getSelectedLaunchConfiguration());
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
         showErrorMessage(e);
      }
   }


   /** Description of the Method */
   public void doStart()
   {
      try
      {
         ServerLaunchManager.getInstance().start(getSelectedLaunchConfiguration());
      }
      catch (Exception e)
      {
         AbstractPlugin.log(e);
         showErrorMessage(e);
      }
   }


   /** Description of the Method */
   public void doTerminate()
   {
      try
      {
         ServerLaunchManager.getInstance().terminate(getSelectedLaunchConfiguration());
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
         showErrorMessage(e);
      }
   }


   /**
    * Gets the viewer attribute of the ServerNavigator object
    *
    * @return   The viewer value
    */
   public TreeViewer getViewer()
   {
      return viewer;
   }


   /**
    * Gets the available attribute of the ServerNavigator object
    *
    * @return   The available value
    */
   public boolean isAvailable()
   {
      return !(viewer == null || viewer.getControl() == null || viewer.getControl().isDisposed());
   }


   /**
    * @param configuration  Description of the Parameter
    * @see                  org.eclipse.debug.core.ILaunchConfigurationListener#launchConfigurationAdded(ILaunchConfiguration)
    */
   public void launchConfigurationAdded(final ILaunchConfiguration configuration)
   {
      Runnable r =
         new Runnable()
         {
            public void run()
            {
               synchronized (viewer)
               {
                  if (isAvailable())
                  {
                     viewer.refresh(false);
                  }
               }
            }
         };
      asynchExec(r);
   }


   /**
    * @param configuration  Description of the Parameter
    * @see                  org.eclipse.debug.core.ILaunchConfigurationListener#launchConfigurationChanged(ILaunchConfiguration)
    */
   public void launchConfigurationChanged(ILaunchConfiguration configuration)
   {
      Runnable r =
         new Runnable()
         {
            public void run()
            {
               synchronized (viewer)
               {
                  if (isAvailable())
                  {
                     viewer.refresh(false);
                  }
               }
            }
         };
      asynchExec(r);
   }


   /**
    * @param configuration  Description of the Parameter
    * @see                  org.eclipse.debug.core.ILaunchConfigurationListener#launchConfigurationRemoved(ILaunchConfiguration)
    */
   public void launchConfigurationRemoved(ILaunchConfiguration configuration)
   {
      try
      {
         ILaunch launch = ServerLaunchManager.getInstance().getRunningStartLaunch(configuration);
         if (launch != null)
         {
            if (ServerLaunchManager.getInstance().hasShutdown(configuration))
            {
               ServerLaunchManager.getInstance().shutDown(configuration);
            }
            else
            {
               launch.terminate();
            }
         }
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
         showErrorMessage(e);
      }

      Runnable r =
         new Runnable()
         {
            public void run()
            {
               synchronized (viewer)
               {
                  if (isAvailable())
                  {
                     viewer.refresh(false);
                  }
               }
            }
         };
      asynchExec(r);
   }


   /**
    * @param event  Description of the Parameter
    * @see          org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(SelectionChangedEvent)
    */
   public void selectionChanged(SelectionChangedEvent event)
   {
      IStructuredSelection selection = (IStructuredSelection) event.getSelection();
      if (selection.getFirstElement() instanceof ILaunchConfiguration)
      {
         ILaunchConfiguration configuration = (ILaunchConfiguration) selection.getFirstElement();
         setServerActions(configuration);
      }
      else
      {
         LogFile logFile = (LogFile) selection.getFirstElement();
         setLogFileActions(logFile);
      }

   }


   /* (non-Javadoc)
    * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
    */
   public void doubleClick(DoubleClickEvent event)
   {
      IStructuredSelection selection = (IStructuredSelection) event.getSelection();
      if (selection.getFirstElement() instanceof ILaunchConfiguration)
      {
         ILaunchConfiguration configuration = (ILaunchConfiguration) selection.getFirstElement();
         try
         {
            // ILaunch launch =
            // ServerLaunchManager.getInstance().getRunningStartLaunch(configuration);
            if (ServerLaunchUIUtil.getView(IConsoleConstants.ID_CONSOLE_VIEW) != null)
            {
               IProcess process = ServerLaunchManager.getInstance().getLastStartProcess(configuration);
               if (process != null)
               {
                  IConsole console = ServerLaunchUIUtil.getProcessConsole(process);
                  IConsoleView view = (IConsoleView) ServerLaunchUIUtil.showView(IConsoleConstants.ID_CONSOLE_VIEW);
                  view.display(console);
               }
            }
         }
         catch (CoreException e)
         {
            AbstractPlugin.log(e);
            showErrorMessage(e);
         }
      }
      else
      {
         LogFile logFile = (LogFile) selection.getFirstElement();
         showLogFile(logFile, false);
      }

   }


   /**
    * @param event          Description of the Parameter
    * @param configuration  Description of the Parameter
    * @see                  org.rocklet.launcher.ui.IServerDebugEventListener#serverEvent(DebugEvent,
    *      ILaunchConfiguration)
    */
   public void serverEvent(DebugEvent event, ILaunchConfiguration configuration)
   {
      updateServerActionsAndLabel(configuration);
   }


   /** Passing the focus request to the viewer's control. */
   public void setFocus()
   {
      viewer.getControl().setFocus();
   }


   /**
    * The
    *
    * @param logFile
    * @param doubleClick
    */
   public void showLogFile(LogFile logFile, boolean doubleClick)
   {
      LogFileViewer viewer = null;
      if (doubleClick)
      {
         viewer = (LogFileViewer) ServerLaunchUIUtil.showView(ILauncherUIConstants.VIEW_ID);
      }
      else
      {
         viewer = (LogFileViewer) ServerLaunchUIUtil.getView(ILauncherUIConstants.VIEW_ID);
      }
      if (viewer != null)
      {
         if (doubleClick || viewer.hasLog(logFile))
         {
            viewer.openLog(logFile);
         }
         if (viewer.hasLog(logFile))
         {
            ServerLaunchUIUtil.showView(ILauncherUIConstants.VIEW_ID);
         }
      }
   }


   /**
    * Gets the view attribute of the ServerNavigator object
    *
    * @param id  Description of the Parameter
    * @return    The view value
    */
   protected IViewPart getView(String id)
   {
      IWorkbenchWindow window = LauncherPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
      if (window != null)
      {
         IWorkbenchPage page = window.getActivePage();
         if (page != null)
         {
            return page.findView(id);
         }
      }
      return null;
   }


   /** Method openLogFile. */
   protected void openSelectedLogFile()
   {
      IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
      Object selected = selection.getFirstElement();
      if (selected != null && selected instanceof LogFile)
      {
         showLogFile((LogFile) selected, true);
      }
   }


   /**
    * Sets the logFileActions attribute of the ServerNavigator object
    *
    * @param logFile  The new logFileActions value
    */
   protected void setLogFileActions(LogFile logFile)
   {
      if (logFile != null)
      {
         if (isAvailable())
         {
            startAction.setEnabled(false);
            shutdownAction.setEnabled(false);
            terminateAction.setEnabled(false);
            openLogFileAction.setEnabled(true);
         }
      }
   }


   /**
    * Sets the serverActions attribute of the ServerNavigator object
    *
    * @param configuration  The new serverActions value
    */
   protected void setServerActions(ILaunchConfiguration configuration)
   {
      if (configuration != null)
      {
         if (isAvailable())
         {
            try
            {
               openLogFileAction.setEnabled(false);
               ServerLaunchUIUtil.enableServerActions(configuration, startAction, shutdownAction, terminateAction);
            }
            catch (CoreException e)
            {
               AbstractPlugin.log(e);
               showErrorMessage(e);
            }
         }
      }
   }


   /**
    * Description of the Method
    *
    * @param e  Description of the Parameter
    */
   protected void showErrorMessage(Throwable e)
   {
      if (isAvailable())
      {
         LauncherPlugin.getDefault().showErrorMessage(e);
      }
   }


   /**
    * Description of the Method
    *
    * @param configuration  Description of the Parameter
    */
   protected void updateServerActionsAndLabel(ILaunchConfiguration configuration)
   {
      synchronized (viewer)
      {
         if (isAvailable())
         {
            viewer.update(configuration, null);
         }
      }
      setServerActions(configuration);
   }


   /**
    * Description of the Method
    *
    * @param configuration  Description of the Parameter
    */
   protected void updateServerActionsAndLabelThread(final ILaunchConfiguration configuration)
   {
      Runnable r =
         new Runnable()
         {
            public void run()
            {
               updateServerActionsAndLabel(configuration);
            }
         };
      asynchExec(r);
   }


   /** Description of the Method */
   private void contributeToActionBars()
   {
      IActionBars bars = getViewSite().getActionBars();
      fillLocalToolBar(bars.getToolBarManager());
   }


   /**
    * Method fillContextMenu.
    *
    * @param manager
    */
   private void fillContextMenu(IMenuManager manager)
   {
      IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
      if (selection.getFirstElement() instanceof ILaunchConfiguration)
      {
         fillServerContextMenu(manager);
      }
      else if (selection.getFirstElement() instanceof LogFile)
      {
         fillLogFileContextMenu(manager);
      }
      else
      {
         fillNoSelectionContextMenu(manager);
      }
   }


   /**
    * Description of the Method
    *
    * @param manager  Description of the Parameter
    */
   private void fillLocalToolBar(IToolBarManager manager)
   {
      manager.add(startAction);
      manager.add(shutdownAction);
      manager.add(terminateAction);
      manager.add(new Separator("org.rocklet.launcher.ui.configuration"));//$NON-NLS-1$
      manager.add(configurationAction);
      manager.add(new Separator("org.rocklet.launcher.ui.logFile"));//$NON-NLS-1$
      manager.add(openLogFileAction);
   }


   /**
    * Description of the Method
    *
    * @param manager  Description of the Parameter
    */
   private void fillLogFileContextMenu(IMenuManager manager)
   {
      manager.add(openLogFileAction);
      manager.add(new Separator("org.rocklet.launcher.ui.configuration"));//$NON-NLS-1$
      manager.add(configurationAction);
      // Other plug-ins can contribute there actions here
      manager.add(new Separator("Additions"));//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param manager  Description of the Parameter
    */
   private void fillNoSelectionContextMenu(IMenuManager manager)
   {
      manager.add(configurationAction);
      // Other plug-ins can contribute there actions here
      manager.add(new Separator("Additions"));//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param manager  Description of the Parameter
    */
   private void fillServerContextMenu(IMenuManager manager)
   {
      manager.add(startAction);
      manager.add(shutdownAction);
      manager.add(terminateAction);
      manager.add(new Separator("org.rocklet.launcher.ui.configuration"));//$NON-NLS-1$
      manager.add(configurationAction);
      // Other plug-ins can contribute there actions here
      manager.add(new Separator("Additions"));//$NON-NLS-1$
   }


   /**
    * The sele
    *
    * @return   ILaunchConfiguration
    */
   private ILaunchConfiguration getSelectedLaunchConfiguration()
   {
      if (viewer.getSelection() == null)
      {
         return null;
      }
      return (ILaunchConfiguration) ((IStructuredSelection) viewer.getSelection()).getFirstElement();
   }


   /** Description of the Method */
   private void initContextMenu()
   {
      MenuManager menuMgr = new MenuManager("#PopupMenu");//$NON-NLS-1$
      menuMgr.setRemoveAllWhenShown(true);
      menuMgr.addMenuListener(
         new IMenuListener()
         {
            public void menuAboutToShow(IMenuManager manager)
            {
               ServerNavigator.this.fillContextMenu(manager);
            }
         });
      Menu menu = menuMgr.createContextMenu(viewer.getControl());
      viewer.getControl().setMenu(menu);
      getSite().registerContextMenu(menuMgr, viewer);
   }


   /** Description of the Method */
   private void initDoubleClickAction()
   {
      viewer.addDoubleClickListener(
         new IDoubleClickListener()
         {
            public void doubleClick(DoubleClickEvent event)
            {
               openSelectedLogFile();
            }
         });
   }


   /** Description of the Method */
   private void makeActions()
   {
      startAction =
         new Action()
         {
            public void run()
            {
               doStart();
            }
         };
      startAction.setText(LauncherUIMessages.getString("ServerNavigator.start_8"));//$NON-NLS-1$
      startAction.setToolTipText(LauncherUIMessages.getString("ServerNavigator.start_9"));//$NON-NLS-1$
      startAction.setEnabled(false);
      startAction.setImageDescriptor(LauncherUIImages.getImageDescriptor(org.jboss.ide.eclipse.launcher.ui.ILauncherUIConstants.IMG_CTOOL_START_SERVER));
      startAction.setDisabledImageDescriptor(LauncherUIImages.getImageDescriptor(org.jboss.ide.eclipse.launcher.ui.ILauncherUIConstants.IMG_DTOOL_START_SERVER));

      shutdownAction =
         new Action()
         {
            public void run()
            {
               doShutdown();
            }
         };
      shutdownAction.setText(LauncherUIMessages.getString("ServerNavigator.shutdown_10"));//$NON-NLS-1$
      shutdownAction.setToolTipText(LauncherUIMessages.getString("ServerNavigator.shutdown_11"));//$NON-NLS-1$
      shutdownAction.setEnabled(false);
      shutdownAction.setImageDescriptor(LauncherUIImages.getImageDescriptor(org.jboss.ide.eclipse.launcher.ui.ILauncherUIConstants.IMG_CTOOL_SHUTDOWN_SERVER));
      shutdownAction.setDisabledImageDescriptor(LauncherUIImages.getImageDescriptor(org.jboss.ide.eclipse.launcher.ui.ILauncherUIConstants.IMG_DTOOL_SHUTDOWN_SERVER));

      terminateAction =
         new Action()
         {
            public void run()
            {
               doTerminate();
            }
         };
      terminateAction.setText(LauncherUIMessages.getString("ServerNavigator.terminate_12"));//$NON-NLS-1$
      terminateAction.setToolTipText(LauncherUIMessages.getString("ServerNavigator.terminate_13"));//$NON-NLS-1$
      terminateAction.setEnabled(false);
      terminateAction.setImageDescriptor(LauncherUIImages.getImageDescriptor(org.jboss.ide.eclipse.launcher.ui.ILauncherUIConstants.IMG_CTOOL_TERMINATE_SERVER));
      terminateAction.setDisabledImageDescriptor(LauncherUIImages.getImageDescriptor(org.jboss.ide.eclipse.launcher.ui.ILauncherUIConstants.IMG_DTOOL_TERMINATE_SERVER));

      configurationAction =
         new Action()
         {
            public void run()
            {
               doConfiguration();
            }

         };
      configurationAction.setText(LauncherUIMessages.getString("ServerNavigator.configuration_14"));//$NON-NLS-1$
      configurationAction.setToolTipText(LauncherUIMessages.getString("ServerNavigator.configuration_15"));//$NON-NLS-1$
      configurationAction.setEnabled(true);
      configurationAction.setImageDescriptor(LauncherUIImages.getImageDescriptor(org.jboss.ide.eclipse.launcher.ui.ILauncherUIConstants.IMG_CTOOL_CONFIGURATION));
      configurationAction.setDisabledImageDescriptor(LauncherUIImages.getImageDescriptor(org.jboss.ide.eclipse.launcher.ui.ILauncherUIConstants.IMG_DTOOL_CONFIGURATION));

      openLogFileAction =
         new Action()
         {
            public void run()
            {
               openSelectedLogFile();
            }
         };
      openLogFileAction.setText(LauncherUIMessages.getString("ServerNavigator.open_16"));//$NON-NLS-1$
      openLogFileAction.setToolTipText(LauncherUIMessages.getString("ServerNavigator.open_17"));//$NON-NLS-1$
      openLogFileAction.setEnabled(false);
      openLogFileAction.setImageDescriptor(LauncherUIImages.getImageDescriptor(org.jboss.ide.eclipse.launcher.ui.ILauncherUIConstants.IMG_CTOOL_OPEN_LOGFILE));
      openLogFileAction.setDisabledImageDescriptor(LauncherUIImages.getImageDescriptor(org.jboss.ide.eclipse.launcher.ui.ILauncherUIConstants.IMG_DTOOL_OPEN_LOGFILE));
   }


   /**
    * Description of the Class
    *
    * @author    Hans Dockter
    * @version   $Revision$
    * @created   18 mai 2003
    */
   class NameSorter extends ViewerSorter
   {
   }
}
