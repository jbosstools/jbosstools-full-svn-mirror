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

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.BoundPointcut;
import org.jboss.ide.eclipse.jdt.aop.ui.wizards.ConvertToPointcutWizard;

/**
 * @author Marshall
 */
public class ConvertToPointcutAction extends PointcuttableAction
{

   private ISelection currentSelection;

   private IWorkbenchPart part;

   public ConvertToPointcutAction()
   {
   }

   public ConvertToPointcutAction(Viewer viewer)
   {
      super(viewer);
   }

   protected void setSelection(ISelection selection)
   {
      currentSelection = selection;
   }

   public void setActivePart(IAction action, IWorkbenchPart targetPart)
   {
      part = targetPart;
   }

   public void runAction(IAction action)
   {

      if (currentSelection instanceof IStructuredSelection)
      {
         IStructuredSelection selection = (IStructuredSelection) currentSelection;
         if (selection.getFirstElement() instanceof IJavaElement)
         {
            IJavaElement element = (IJavaElement) selection.getFirstElement();
            AopCorePlugin.setCurrentJavaProject(element.getJavaProject());

            ConvertToPointcutWizard wizard = new ConvertToPointcutWizard(element);
            WizardDialog dialog = new WizardDialog(part.getSite().getShell(), wizard);

            dialog.create();
            int response = dialog.open();

            if (response == WizardDialog.OK)
            {
               BoundPointcut pointcut = wizard.getPointcut();
               AopDescriptor descriptor = AopCorePlugin.getDefault().getDefaultDescriptor(element.getJavaProject());

               descriptor.addPointcut(pointcut.getName(), pointcut.toString());
               descriptor.save();
            }
         }
      }

   }

}
