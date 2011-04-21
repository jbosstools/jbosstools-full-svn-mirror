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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;
import org.jboss.aop.AspectManager;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.AOPType.Typedef;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModel;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTTypedefExpression;
import org.jboss.ide.eclipse.jdt.aop.core.util.JaxbAopUtil;
import org.jboss.ide.eclipse.jdt.aop.ui.dialogs.TypedefPreviewDialog;

public class CreateNewNamedTypedefAction extends Action
{
   private Viewer viewer;

   private Shell shell;

   public CreateNewNamedTypedefAction(Viewer viewer, Shell shell)
   {
      this.viewer = viewer;
      this.shell = shell;
   }

   public void run()
   {
      TypedefPreviewDialog dialog = new TypedefPreviewDialog(shell);
      int response = dialog.open();
      if (response == Dialog.OK)
      {
         try
         {
            JDTTypedefExpression expression = dialog.getTypedefExpression();
            // TODO: We eventually wanna move this to a model function
            AspectManager.instance().addTypedef(expression);
            AopModel.instance();
            AopDescriptor desc = AopCorePlugin.getDefault().getDefaultDescriptor(AopCorePlugin.getCurrentJavaProject());
            Typedef jaxbTypeDef = JaxbAopUtil.instance().getFactory().createAOPTypeTypedef();
            jaxbTypeDef.setExpr(expression.getExpr());
            jaxbTypeDef.setName(expression.getName());
            desc.getAop().getTopLevelElements().add(jaxbTypeDef);
            desc.save();
         }
         catch (Exception e)
         {
         }
      }
   }
}
