/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
package org.jboss.ide.eclipse.packaging.ui.actions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.eclipse.ant.internal.ui.launchConfigurations.AntLaunchShortcut;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionDelegate;
import org.eclipse.ui.externaltools.internal.model.IExternalToolConstants;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.core.util.ResourceUtil;
import org.jboss.ide.eclipse.packaging.core.PackagingCorePlugin;
import org.jboss.ide.eclipse.packaging.ui.PackagingUIMessages;
import org.jboss.ide.eclipse.packaging.ui.PackagingUIPlugin;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class PackagingRunAction extends ActionDelegate implements IObjectActionDelegate, IWorkbenchWindowActionDelegate
{
   /** Description of the Field */
   protected IWorkbenchPart part = null;

   /** Description of the Field */
   protected ISelection selection = null;

   /** Description of the Field */
   protected IWorkbenchWindow window = null;

   /** Constructor for the PackaginRunAction object */
   public PackagingRunAction()
   {
      super();
   }

   /** Description of the Method */
   public void dispose()
   {
   }

   /**
    * Description of the Method
    *
    * @param window  Description of the Parameter
    */
   public void init(IWorkbenchWindow window)
   {
      this.window = window;
   }

   /**
    * Main processing method for the XDocletRunAction object
    *
    * @param action  Description of the Parameter
    */
   public void run(IAction action)
   {
      if (this.selection != null && (this.selection instanceof IStructuredSelection))
      {
         IStructuredSelection sel = (IStructuredSelection) this.selection;
         Object o = sel.getFirstElement();

         // For each Java Project
         if (o instanceof IResource)
         {
            IProject project = ((IResource) o).getProject();
            this.process(project);
         }
         if (o instanceof IJavaProject)
         {
            IProject project = ((IJavaProject) o).getProject();
            this.process(project);
         }
      }
   }

   /**
    * Description of the Method
    *
    * @param action     Description of the Parameter
    * @param selection  Description of the Parameter
    */
   public void selectionChanged(IAction action, ISelection selection)
   {
      this.selection = selection;
   }

   /**
    * @param action      The new ActivePart value
    * @param targetPart  The new ActivePart value
    * @see               org.eclipse.ui.IObjectActionDelegate#setActivePart(IAction,
    *      IWorkbenchPart)
    */
   public void setActivePart(IAction action, IWorkbenchPart targetPart)
   {
      this.part = targetPart;
   }

   /**
    * Description of the Method
    *
    * @param project  Description of the Parameter
    */
   protected void process(final IProject project)
   {
      final IFile projectFile = project.getFile(PackagingCorePlugin.PROJECT_FILE);
      //final IFile buildFile = project.getFile(PackagingCorePlugin.BUILD_FILE);

      // If the xdoclet build file exists, then process it
      if (projectFile.exists())
      {
         Job job = new Job(PackagingUIMessages.getString("PackagingRunAction.job.title")//$NON-NLS-1$
         )
         {

            protected IStatus run(IProgressMonitor monitor)
            {
               try
               {
                  monitor.beginTask(PackagingUIMessages.getString("PackagingRunAction.packaging.run"), 100);//$NON-NLS-1$

                  // Transform configuration to Ant build file
                  monitor.subTask(PackagingUIMessages.getString("PackagingRunAction.packaging.generate"));//$NON-NLS-1$

                  // Building is now taken care of by the builder
                  IFile buildFile = PackagingCorePlugin.getDefault().createBuildFile(project);

                  monitor.worked(10);

                  // Launch the generation
                  monitor.subTask(PackagingUIMessages.getString("PackagingRunAction.packaging.process"));//$NON-NLS-1$

                  ILaunchConfiguration configuration = null;
                  List cfgs = AntLaunchShortcut.findExistingLaunchConfigurations(buildFile);
                  if (cfgs.size() > 0)
                  {
                     // Take the first and remove the others
                     configuration = (ILaunchConfiguration) cfgs.get(0);
                     for (int i = 0; i < cfgs.size(); i++)
                     {
                        ((ILaunchConfiguration) cfgs.get(i)).delete();
                     }
                  }

                  // Create a new one
                  configuration = AntLaunchShortcut.createDefaultLaunchConfiguration(buildFile);
                  ILaunchConfigurationWorkingCopy copy = configuration.getWorkingCopy();
                  copy.setAttribute(IExternalToolConstants.ATTR_CAPTURE_OUTPUT, true);
                  copy.setAttribute(IExternalToolConstants.ATTR_SHOW_CONSOLE, true);
                  copy.setAttribute(IDebugUIConstants.ATTR_LAUNCH_IN_BACKGROUND, false);
                  copy.setAttribute(IDebugUIConstants.ATTR_PRIVATE, true);
                  configuration = copy.doSave();

                  // Launch the generation
                  configuration.launch(ILaunchManager.RUN_MODE, monitor);

                  monitor.worked(80);

                  // Refresh the project
                  monitor.subTask(PackagingUIMessages.getString("PackagingRunAction.packaging.refresh"));//$NON-NLS-1$

                  ResourceUtil.safeRefresh(project, IResource.DEPTH_INFINITE);

                  monitor.worked(10);
               }
               catch (CoreException ce)
               {
                  AbstractPlugin.logError("Error while running packaging", ce);//$NON-NLS-1$
                  PackagingUIPlugin.getDefault().showErrorMessage(
                        PackagingUIMessages.getString("PackagingRunAction.failed") + ce.getMessage());//$NON-NLS-1$
               }
               catch (IOException e)
               {
                  AbstractPlugin.logError("Error while running packaging", e);//$NON-NLS-1$
                  PackagingUIPlugin.getDefault().showErrorMessage(
                        PackagingUIMessages.getString("PackagingRunAction.failed") + e.getMessage());//$NON-NLS-1$

               }
               catch (TransformerException e)
               {
                  AbstractPlugin.logError("Error while running packaging", e);//$NON-NLS-1$
                  PackagingUIPlugin.getDefault().showErrorMessage(
                        PackagingUIMessages.getString("PackagingRunAction.failed") + e.getMessage());//$NON-NLS-1$

               }

               return Status.OK_STATUS;
            }
         };
         job.setRule(project.getProject());
         job.setPriority(Job.BUILD);
         job.schedule();
      }
   }

}
