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
package org.jboss.ide.eclipse.jdt.aop.ui.wizards;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.jboss.ide.eclipse.jdt.aop.core.BoundAdvice;
import org.jboss.ide.eclipse.jdt.aop.core.BoundPointcut;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Binding;
import org.jboss.ide.eclipse.jdt.aop.ui.AopSharedImages;
import org.jboss.ide.eclipse.jdt.aop.ui.AopUiPlugin;

/**
 * @author Marshall
 */
public class AdviceWizardPage extends WizardPage
{

   private ArrayList pointcuts;

   private StackLayout stackLayout;

   private Composite extraControls, methodControls, fieldControls, constructorControls;

   private Tree tree;

   private BoundPointcut currentChild;

   public AdviceWizardPage(ArrayList pointcuts) throws Exception
   {
      super("Advice Wizard", "Apply Advice...", ImageDescriptor.createFromURL(new URL(
            "file:///C:/Code/jboss-aop-eclipse/org.jboss.ide.eclipse.jdt.aop.ui/icons/aspect.gif")));
      this.pointcuts = pointcuts;
   }

   public void createControl(Composite parent)
   {

      Composite composite = new Composite(parent, SWT.NULL);
      GridLayout layout = new GridLayout(1, true);
      composite.setLayout(layout);

      Label label = new Label(composite, SWT.NULL);
      label.setText("Please select the advice you want to apply to each field/method");

      tree = new Tree(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
      CheckboxTreeViewer adviceTree = new CheckboxTreeViewer(tree);
      adviceTree.setLabelProvider(new AdviceTreeLabelProvider());
      adviceTree.setContentProvider(new AdviceTreeContentProvider());
      adviceTree.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
      adviceTree.setInput(pointcuts);
      adviceTree.addCheckStateListener(new ICheckStateListener()
      {
         public void checkStateChanged(CheckStateChangedEvent event)
         {
            updateCheckedItems(event);
         }
      });

      tree.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
      setControl(composite);
   }

   private class AdviceTreeLabelProvider extends LabelProvider
   {

      public Image getImage(Object obj)
      {
         if (obj instanceof BoundAdvice)
         {
            BoundAdvice advice = (BoundAdvice) obj;
            return AopUiPlugin.getDefault().getTreeImage(advice.getMethod());
         }
         else if (obj instanceof BoundPointcut)
         {
            IJavaElement element = ((BoundPointcut) obj).getElement();
            if (element != null)
            {
               return AopUiPlugin.getDefault().getTreeImage(element);
            }
            else
            {
               return AopSharedImages.getImage(AopSharedImages.IMG_BINDING);
            }
         }
         else
            return null;
      }

      public String getText(Object obj)
      {
         if (obj instanceof BoundPointcut)
         {
            IJavaElement element = ((BoundPointcut) obj).getElement();
            if (element != null)
            {
               return AopUiPlugin.getDefault().getTreeLabel(element);
            }
            else
            {
               Binding binding = ((BoundPointcut) obj).getBinding();
               if (binding != null)
               {
                  return binding.getPointcut();
               }
            }
         }
         else if (obj instanceof BoundAdvice)
         {
            IMethod method = ((BoundAdvice) obj).getMethod();
            return AopUiPlugin.getDefault().getTreeLabel(method);
         }
         return null;
      }
   }

   private class AdviceTreeContentProvider implements IStructuredContentProvider, ITreeContentProvider
   {

      public Object[] getChildren(Object inputElement)
      {

         if (inputElement != null)
         {
            if (inputElement instanceof ArrayList)
            {
               ArrayList list = (ArrayList) inputElement;
               return list.toArray();
            }

            else if (inputElement instanceof BoundPointcut)
            {
               BoundPointcut pointcut = (BoundPointcut) inputElement;
               if (pointcut.getAdvice() != null)
                  return pointcut.getAdvice().toArray();
            }
         }

         return new Object[0];
      }

      public Object getParent(Object child)
      {
         if (child instanceof BoundAdvice)
         {
            return ((BoundAdvice) child).getPointcut();
         }
         return null;
      }

      public boolean hasChildren(Object element)
      {
         if (element instanceof BoundPointcut)
         {
            ArrayList advice = ((BoundPointcut) element).getAdvice();
            return advice != null && advice.size() > 0;
         }
         return false;
      }

      public Object[] getElements(Object inputElement)
      {
         return getChildren(inputElement);
      }

      public void dispose()
      {
      }

      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }
   }

   private void updateCheckedItems(CheckStateChangedEvent event)
   {
      CheckboxTreeViewer viewer = (CheckboxTreeViewer) event.getSource();
      Object element = event.getElement();

      if (element instanceof BoundPointcut)
      {
         viewer.setSubtreeChecked(element, event.getChecked());
      }
      else if (element instanceof BoundAdvice)
      {
         BoundAdvice advice = (BoundAdvice) element;
         advice.setChecked(event.getChecked());

         boolean allChecked = true, someChecked = false;
         ArrayList otherAdvice = advice.getPointcut().getAdvice();
         for (Iterator aIter = otherAdvice.iterator(); aIter.hasNext();)
         {
            BoundAdvice otherChild = (BoundAdvice) aIter.next();
            someChecked = (someChecked || otherChild.isChecked());
            allChecked = (allChecked && otherChild.isChecked());
         }

         if (allChecked)
         {
            viewer.setChecked(advice.getPointcut(), true);
         }
         else if (someChecked)
         {
            viewer.setGrayChecked(advice.getPointcut(), true);
         }
         else
         {
            viewer.setChecked(advice.getPointcut(), false);
         }
      }
   }

   public IMethod[] getSelectedAdviceMethods()
   {
      ArrayList methods = new ArrayList();

      Iterator pIter = pointcuts.iterator();
      while (pIter.hasNext())
      {
         BoundPointcut pointcut = (BoundPointcut) pIter.next();
         Iterator aIter = pointcut.getAdvice().iterator();
         String pointcutString = pointcut.toString();

         while (aIter.hasNext())
         {
            BoundAdvice advice = (BoundAdvice) aIter.next();
            if (advice.isChecked())
            {
               methods.add(advice.getMethod());
            }
         }
      }

      return (IMethod[]) methods.toArray(new IMethod[methods.size()]);
   }
}