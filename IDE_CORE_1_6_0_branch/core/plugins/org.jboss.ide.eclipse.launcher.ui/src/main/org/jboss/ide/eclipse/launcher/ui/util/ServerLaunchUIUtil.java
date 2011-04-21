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
package org.jboss.ide.eclipse.launcher.ui.util;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.views.console.ProcessConsole;
import org.eclipse.debug.ui.console.IConsoleColorProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.launcher.core.LauncherPlugin;
import org.jboss.ide.eclipse.launcher.core.ServerLaunchManager;
import org.jboss.ide.eclipse.launcher.core.util.LaunchStatus;

/**
 * @author    Hans Dockter
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class ServerLaunchUIUtil
{
   /** Constructor for the ServerLaunchUIUtil object */
   private ServerLaunchUIUtil()
   {
   }

   /**
    * Create some empty space.
    *
    * @param comp     Description of the Parameter
    * @param colSpan  Description of the Parameter
    */
   public static void createVerticalSpacer(Composite comp, int colSpan)
   {
      Label label = new Label(comp, SWT.NONE);
      GridData gd = new GridData();
      gd.horizontalSpan = colSpan;
      label.setLayoutData(gd);
   }

   /**
    * Description of the Method
    *
    * @param configuration      Description of the Parameter
    * @param start              Description of the Parameter
    * @param shutdown           Description of the Parameter
    * @param terminate          Description of the Parameter
    * @exception CoreException  Description of the Exception
    */
   public static synchronized void enableServerActions(ILaunchConfiguration configuration, IAction start,
         IAction shutdown, IAction terminate) throws CoreException
   {
      if (configuration != null)
      {
         if (start != null)
         {
            start.setEnabled(isLaunchable(configuration));
         }
         if (shutdown != null)
         {
            shutdown.setEnabled(isShutdownable(configuration));
         }
         if (terminate != null)
         {
            terminate
                  .setEnabled(configuration != null
                        && (ServerLaunchManager.getInstance().getStatusForStartLaunch(configuration) == LaunchStatus.RUNNING));
         }
      }
      else
      {
         if (start != null)
         {
            start.setEnabled(false);
         }
         if (shutdown != null)
         {
            shutdown.setEnabled(false);
         }
         if (terminate != null)
         {
            terminate.setEnabled(false);
         }
      }
   }

   /**
    * Helper that opens the directory chooser dialog.
    *
    * @param startingDirectory  Description of the Parameter
    * @param shell              Description of the Parameter
    * @return                   The directory value
    */
   public static File getDirectory(File startingDirectory, Shell shell)
   {
      DirectoryDialog fileDialog = new DirectoryDialog(shell, SWT.OPEN);
      if (startingDirectory != null)
      {
         fileDialog.setFilterPath(startingDirectory.getPath());
      }
      String dir = fileDialog.open();
      if (dir != null)
      {
         dir = dir.trim();
         if (dir.length() > 0)
         {
            return new File(dir);
         }
      }
      return null;
   }

   /**
    * Gets the file attribute of the ServerLaunchUIUtil class
    *
    * @param startingFile  Description of the Parameter
    * @param shell         Description of the Parameter
    * @return              The file value
    */
   public static File getFile(File startingFile, Shell shell)
   {
      FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
      if (startingFile != null)
      {
         fileDialog.setFilterPath(startingFile.getPath());
      }
      String file = fileDialog.open();
      if (file != null)
      {
         file = file.trim();
         if (file.length() > 0)
         {
            return new File(file);
         }
      }
      return null;
   }

   /**
    * Gets the name attribute of the ServerLaunchUIUtil class
    *
    * @param configuration  Description of the Parameter
    * @return               The name value
    */
   public static String getName(ILaunchConfiguration configuration)
   {
      try
      {
         return configuration.getType().getName() + ": " //$NON-NLS-1$
               + configuration.getName();
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
         return null;
      }
   }

   /**
    * Gets the processConsole attribute of the ServerLaunchUIUtil class
    *
    * @param process  Description of the Parameter
    * @return         The processConsole value
    */
   public static IConsole getProcessConsole(IProcess process)
   {
      IConsole console = null;
      IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
      IConsole[] consoles = manager.getConsoles();

      for (int i = 0; i < consoles.length; i++)
      {
         if (consoles[i] instanceof ProcessConsole)
         {
            ProcessConsole pc = (ProcessConsole) consoles[i];
            if (pc.getProcess().equals(process))
            {
               console = consoles[i];
               break;
            }
         }
      }

      if (console == null)
      {
         IConsoleColorProvider colorProvider = DebugUIPlugin.getDefault().getProcessConsoleManager().getColorProvider(
               null);
         console = new ProcessConsole(process, colorProvider);
         manager.addConsoles(new IConsole[]
         {console});
      }

      return console;
   }

   /**
    * Returns a view by its id.
    *
    * @param id
    * @return    IViewPart the view according to the id or null if the view is
    *         closed
    */
   public static IViewPart getView(String id)
   {
      IViewPart view = null;
      IWorkbenchWindow window = LauncherPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
      if (window != null)
      {
         IWorkbenchPage page = window.getActivePage();
         if (page != null)
         {
            view = page.findView(id);
         }
      }
      return view;
   }

   /**
    * Gets the fileExistent attribute of the ServerLaunchUIUtil class
    *
    * @param path  Description of the Parameter
    * @return      The fileExistent value
    */
   public static boolean isFileExistent(String path)
   {
      return (new File(path)).isFile();
   }

   /**
    * Gets the launchable attribute of the ServerLaunchUIUtil class
    *
    * @param configuration      Description of the Parameter
    * @return                   The launchable value
    * @exception CoreException  Description of the Exception
    */
   public static boolean isLaunchable(ILaunchConfiguration configuration) throws CoreException
   {

      return (configuration != null
            && (ServerLaunchManager.getInstance().getStatusForStartLaunch(configuration) == LaunchStatus.NOT_RUNNING) && ServerLaunchManager
            .getInstance().isValid(configuration));
   }

   /**
    * Gets the shutdownable attribute of the ServerLaunchUIUtil class
    *
    * @param configuration      Description of the Parameter
    * @return                   The shutdownable value
    * @exception CoreException  Description of the Exception
    */
   public static boolean isShutdownable(ILaunchConfiguration configuration) throws CoreException
   {
      return configuration != null
            && (ServerLaunchManager.getInstance().getStatusForStartLaunch(configuration) == LaunchStatus.RUNNING)
            && ServerLaunchManager.getInstance().hasShutdown(configuration);
   }

   /**
    * A view is opened or brought to the front.
    *
    * @param id  the id of the view
    * @return    IViewPart the view that is shown
    */
   public static IViewPart showView(String id)
   {
      IViewPart view = null;
      IWorkbenchWindow window = LauncherPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
      if (window != null)
      {
         IWorkbenchPage page = window.getActivePage();
         if (page != null)
         {
            try
            {
               view = page.findView(id);
               if (view == null)
               {
                  IWorkbenchPart activePart = page.getActivePart();
                  view = page.showView(id);
                  //restore focus stolen by the creation of the console
                  page.activate(activePart);
               }
               else
               {
                  page.bringToTop(view);
               }
            }
            catch (PartInitException e)
            {
               AbstractPlugin.log(e);
            }
         }
      }
      return view;
   }
}
