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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.action.IAction;
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
public class ApplyInterceptorAction extends PointcuttableAction
{

   private ISelection currentSelection;

   private IWorkbenchPart part;

   private ArrayList availableInterceptors;

   private ArrayList selectedInterceptors;

   public ApplyInterceptorAction()
   {
      availableInterceptors = new ArrayList();
   }

   public ApplyInterceptorAction(Viewer viewer)
   {
      super(viewer);
      availableInterceptors = new ArrayList();
   }

   public void setActivePart(IAction action, IWorkbenchPart targetPart)
   {
      part = targetPart;
   }

   public void runAction(IAction action)
   {
      try
      {
         if (currentSelection instanceof IStructuredSelection)
         {
            ArrayList pointcuts = new ArrayList();
            IJavaProject project = AopCorePlugin.getCurrentJavaProject();

            IStructuredSelection selection = (IStructuredSelection) currentSelection;
            if (!selection.isEmpty())
            {
               Iterator iter = selection.iterator();
               selectedInterceptors = AdvisorDialogUtil.openInterceptorDialog(part.getSite().getShell());

               if (selectedInterceptors == null)
                  return;

               while (iter.hasNext())
               {
                  Object selected = iter.next();

                  if (selected instanceof IJavaElement)
                  {
                     IJavaElement element = (IJavaElement) selected;

                     if (project == null)
                     {
                        project = element.getJavaProject();
                        AopCorePlugin.setCurrentJavaProject(project);
                     }

                     BoundPointcut pointcut = new BoundPointcut(element);
                     pointcuts.add(pointcut);
                  }
                  else if (selected instanceof Binding)
                  {
                     Binding binding = (Binding) selected;
                     BoundPointcut pointcut = new BoundPointcut(binding);
                     pointcuts.add(pointcut);
                  }
               }

               AopDescriptor aopDescriptor = AopCorePlugin.getDefault().getDefaultDescriptor(project);
               Iterator pIter = pointcuts.iterator();
               while (pIter.hasNext())
               {
                  BoundPointcut pointcut = (BoundPointcut) pIter.next();
                  Iterator interceptorIterator = selectedInterceptors.iterator();
                  while (interceptorIterator.hasNext())
                  {
                     IType type = (IType) interceptorIterator.next();
                     aopDescriptor.bindInterceptor(pointcut.toString(), type.getFullyQualifiedName());
                  }
               }

               aopDescriptor.save();
            }
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   public void setSelection(ISelection selection)
   {
      currentSelection = selection;
   }
}
