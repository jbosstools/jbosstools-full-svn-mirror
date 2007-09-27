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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;
import org.jboss.aop.AspectManager;
import org.jboss.aop.pointcut.PointcutExpression;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Pointcut;
import org.jboss.ide.eclipse.jdt.aop.ui.dialogs.PointcutPreviewDialog;

/**
 * 
 * @author Rob Stryker
 */
public class CreateNewNamedPointcutAction extends Action
{

   private Viewer viewer;

   private Shell shell;

   public CreateNewNamedPointcutAction(Viewer viewer, Shell shell)
   {
      this.viewer = viewer;
      this.shell = shell;
   }

   public void run()
   {
      PointcutPreviewDialog previewDialog = new PointcutPreviewDialog(null, "", shell, AopCorePlugin
            .getCurrentJavaProject(), PointcutPreviewDialog.NAMED);

      int response = -1;

      response = previewDialog.open();
      if (response == Dialog.OK)
      {
         String pointcut = previewDialog.getExpression();
         String name = previewDialog.getName();

         AspectManager manager = AspectManager.instance();
         try
         {
            PointcutExpression expr = new PointcutExpression(name, pointcut);
            AopDescriptor descriptor = (AopDescriptor) viewer.getInput();

            manager.addPointcut(expr);
            descriptor.addPointcut(name, pointcut);
            descriptor.save();
         }
         catch (Exception e)
         {
         }
      }

   }
}
