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
package org.jboss.ide.eclipse.jdt.aop.ui.dialogs;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aop;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Pointcut;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModelUtils;
import org.jboss.ide.eclipse.jdt.aop.ui.AopSharedImages;

/**
 * Lets the user associate a premade pointcut with this binding
 * @author Rob Stryker
 */
public class PointcutChooseDialog extends Dialog
{

   private Pointcut selected = null;

   public PointcutChooseDialog(Shell parentShell)
   {
      super(parentShell);
   }

   public Pointcut getPointcut()
   {
      return selected;
   }

   protected Control createDialogArea(Composite parent)
   {
      getShell().setText("Choose a Pointcut");

      GridData mainData = new GridData();
      mainData.horizontalAlignment = GridData.FILL;
      mainData.grabExcessHorizontalSpace = true;
      Composite main = new Composite(parent, SWT.NONE);
      main.setLayoutData(mainData);

      main.setLayout(new FormLayout());

      final TableViewer viewer = new TableViewer(main, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
      ChoosePointcutProvider provider = new ChoosePointcutProvider();
      viewer.setLabelProvider(provider);
      viewer.setContentProvider(provider);

      FormData data = new FormData();
      data.left = new FormAttachment(0, 0);
      data.top = new FormAttachment(0, 0);
      data.right = new FormAttachment(100, 0);
      data.bottom = new FormAttachment(100, 0);

      viewer.getTable().setLayoutData(data);
      viewer.setInput("");

      viewer.addSelectionChangedListener(new ISelectionChangedListener()
      {
         public void selectionChanged(SelectionChangedEvent event)
         {
            IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
            selected = (Pointcut) selection.getFirstElement();
         }
      });

      return main;
   }

   protected class ChoosePointcutProvider extends LabelProvider implements IStructuredContentProvider
   {

      private List pointcuts;

      public ChoosePointcutProvider()
      {
         AopDescriptor descriptor = AopCorePlugin.getDefault().getDefaultDescriptor(
               AopCorePlugin.getCurrentJavaProject());
         Aop aop = descriptor.getAop();
         this.pointcuts = AopModelUtils.getPointcutsFromAop(aop);
      }

      public Object[] getElements(Object inputElement)
      {
         // TODO Auto-generated method stub
         return pointcuts.toArray();
      }

      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }

      public Image getImage(Object element)
      {
         return AopSharedImages.getImage(AopSharedImages.IMG_POINTCUT);
      }

      public String getText(Object element)
      {
         if (element instanceof Pointcut)
         {
            return ((Pointcut) element).getExpr();
         }

         return element.toString();
      }

   }
}
