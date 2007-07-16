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
package org.jboss.ide.eclipse.jdt.aop.ui.wizards;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;

/**
 * @author Marshall
 */
public class InterceptorWizardPage extends WizardPage
{
   private ArrayList interceptors;

   private ArrayList selectedInterceptors;

   public InterceptorWizardPage(ArrayList interceptors)
   {
      super("Interceptor Wizard", "Select an Interceptor..", null);
      this.interceptors = interceptors;

      selectedInterceptors = new ArrayList();
   }

   public void createControl(Composite parent)
   {

      Composite composite = new Composite(parent, SWT.NULL);
      GridLayout layout = new GridLayout(1, true);
      composite.setLayout(layout);

      Label label = new Label(composite, SWT.NULL);
      label.setText("Please select any Interceptors you wish to apply to these methods.");

      Table interceptorTable = new Table(composite, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
      final TableViewer interceptorList = new TableViewer(interceptorTable);
      interceptorList.setLabelProvider(new InterceptorLabelProvider());
      interceptorList.setContentProvider(new InterceptorContentProvider());
      interceptorList.setInput(interceptors.toArray());
      interceptorList.addSelectionChangedListener(new ISelectionChangedListener()
      {
         public void selectionChanged(SelectionChangedEvent event)
         {
            selectedInterceptors.clear();
            IStructuredSelection selection = (IStructuredSelection) event.getSelection();
            Iterator iter = selection.iterator();
            while (iter.hasNext())
            {
               selectedInterceptors.add(iter.next());
            }
         }
      });
      interceptorList.getControl().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
      setControl(composite);
   }

   public class InterceptorContentProvider implements IStructuredContentProvider
   {
      public void dispose()
      {
      }

      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }

      public Object[] getElements(Object inputElement)
      {
         return interceptors.toArray();
      }
   }

   public class InterceptorLabelProvider implements ILabelProvider
   {

      public Image getImage(Object element)
      {
         Image image = JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_CLASS);
         return image;
      }

      public String getText(Object element)
      {
         if (element instanceof IType)
         {
            IType type = (IType) element;

            return type.getFullyQualifiedName();
         }
         return null;
      }

      public void addListener(ILabelProviderListener listener)
      {
      }

      public void dispose()
      {
      }

      public boolean isLabelProperty(Object element, String property)
      {
         return false;
      }

      public void removeListener(ILabelProviderListener listener)
      {
      }

   }

   /**
    * @return
    */
   public ArrayList getSelectedInterceptors()
   {
      return selectedInterceptors;
   }

}
