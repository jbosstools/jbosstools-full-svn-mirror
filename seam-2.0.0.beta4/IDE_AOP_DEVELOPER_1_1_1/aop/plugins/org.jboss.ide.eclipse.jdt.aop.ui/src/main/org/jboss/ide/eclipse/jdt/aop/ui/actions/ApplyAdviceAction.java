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
package org.jboss.ide.eclipse.jdt.aop.ui.actions;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IWorkbenchPart;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.BoundPointcut;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Binding;
import org.jboss.ide.eclipse.jdt.aop.ui.util.AdvisorDialogUtil;

/**
 * @author Marshall
 */
public class ApplyAdviceAction extends PointcuttableAction
{

   private IWorkbenchPart part;

   private IStructuredSelection currentSelection;

   private ProgressMonitorDialog monitor;

   public ApplyAdviceAction()
   {
   }

   public ApplyAdviceAction(Viewer viewer)
   {
      super(viewer);
   }

   public void setActivePart(IAction action, IWorkbenchPart targetPart)
   {
      part = targetPart;
   }

   public void runAction(IAction action)
   {
      if (currentSelection instanceof IStructuredSelection)
      {
         IJavaProject project = AopCorePlugin.getCurrentJavaProject();
         IStructuredSelection selection = (IStructuredSelection) currentSelection;
         ArrayList pointcuts = new ArrayList();
         Iterator iter = selection.iterator();

         monitor = new ProgressMonitorDialog(part.getSite().getShell());
         monitor.open();
         monitor.getProgressMonitor().beginTask("Searching for advisable methods...", selection.size());

         while (iter.hasNext())
         {
            Object object = iter.next();

            if (object instanceof IJavaElement)
            {
               IJavaElement element = (IJavaElement) object;
               if (project == null)
               {
                  project = element.getJavaProject();
                  AopCorePlugin.setCurrentJavaProject(project);
               }

               BoundPointcut pointcut = new BoundPointcut(element);
               ArrayList advice = AdvisorDialogUtil.getAvaiableAdvice(element, part.getSite().getShell(), monitor
                     .getProgressMonitor());

               pointcut.setAdvice(advice);
               pointcuts.add(pointcut);
            }
            else if (object instanceof Binding)
            {
               Binding binding = (Binding) object;

               BoundPointcut pointcut = new BoundPointcut(binding);
               ArrayList advice = AdvisorDialogUtil.getAvaiableAdvice(binding, part.getSite().getShell(), monitor
                     .getProgressMonitor());

               pointcut.setAdvice(advice);
               pointcuts.add(pointcut);
            }
         }

         monitor.close();

         IMethod adviceMethods[] = AdvisorDialogUtil.openAdviceDialog(pointcuts, part.getSite().getShell(), monitor
               .getProgressMonitor());
         AopDescriptor descriptor = AopCorePlugin.getDefault().getDefaultDescriptor(project);

         Iterator pIter = pointcuts.iterator();
         while (pIter.hasNext())
         {
            BoundPointcut pointcut = (BoundPointcut) pIter.next();
            Iterator aIter = pointcut.getAdvice().iterator();
            String pointcutString = pointcut.toString();

            for (int i = 0; i < adviceMethods.length; i++)
            {
               descriptor.bindAdvice(pointcutString, adviceMethods[i].getDeclaringType().getFullyQualifiedName(),
                     adviceMethods[i].getElementName());
            }
         }

         descriptor.save();
      }
   }

   public void setSelection(ISelection selection)
   {
      currentSelection = (IStructuredSelection) selection;
   }

}
