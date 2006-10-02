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
package org.jboss.ide.eclipse.jdt.aop.ui.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.classpath.AopJdk14ClasspathContainer;
import org.jboss.ide.eclipse.jdt.aop.core.project.AopProjectNature;

/**
 * @author Marshall
 */
public class AddAopNatureAction implements IObjectActionDelegate
{

   private IWorkbenchPart part;

   private IStructuredSelection selection;

   public void setActivePart(IAction action, IWorkbenchPart targetPart)
   {
      part = targetPart;
   }

   public void run(IAction action)
   {

      IRunnableWithProgress op = new IRunnableWithProgress()
      {
         public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
         {
            Object first = selection.getFirstElement();
            if (!(first instanceof IProject))
               return;
            IProject proj = (IProject) first;
            IProjectNature nature = null;
            try
            {
               nature = proj.getNature("org.eclipse.jdt.core.javanature");
            }
            catch (Exception e)
            {

            }

            if (nature != null)
            {
               IJavaProject project;
               if (!(first instanceof IJavaProject))
               {
                  project = JavaCore.create(proj);
               }
               else
               {
                  project = (IJavaProject) first;
               }
               monitor.beginTask("Applying AOP Project Nature to Project \"" + project.getElementName() + "\"...", 2);
               AopCorePlugin.setCurrentJavaProject(project);

               AopProjectNature.ensureAopProjectNature(project);
               monitor.worked(1);

               try
               {
                  IClasspathEntry entries[] = project.getRawClasspath();
                  ArrayList entriesAsList = new ArrayList();
                  entriesAsList.addAll(Arrays.asList(entries));
                  entriesAsList.add(JavaCore.newContainerEntry(new Path(AopJdk14ClasspathContainer.CONTAINER_ID)));

                  entries = (IClasspathEntry[]) entriesAsList.toArray(new IClasspathEntry[entriesAsList.size()]);
                  project.setRawClasspath(entries, project.getOutputLocation(), monitor);

                  monitor.worked(1);
               }
               catch (JavaModelException e)
               {
                  e.printStackTrace();
               }
            }
         }
      };

      try
      {
         new ProgressMonitorDialog(part.getSite().getShell()).run(false, true, op);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   public void selectionChanged(IAction action, ISelection selection)
   {
      if (selection instanceof IStructuredSelection)
      {
         this.selection = (IStructuredSelection) selection;
      }
   }

}
