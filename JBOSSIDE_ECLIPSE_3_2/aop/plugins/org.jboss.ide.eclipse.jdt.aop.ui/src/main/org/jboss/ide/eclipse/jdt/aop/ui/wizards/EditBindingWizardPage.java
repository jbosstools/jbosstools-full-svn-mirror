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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Advice;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Binding;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Interceptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.InterceptorRef;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Pointcut;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModelUtils;
import org.jboss.ide.eclipse.jdt.aop.ui.AopSharedImages;
import org.jboss.ide.eclipse.jdt.aop.ui.dialogs.PointcutChooseDialog;
import org.jboss.ide.eclipse.jdt.aop.ui.dialogs.PointcutPreviewDialog;
import org.jboss.ide.eclipse.jdt.aop.ui.util.AdvisorDialogUtil;

/**
 * @author Marshall
 */
public class EditBindingWizardPage extends WizardPage
{

   protected Text pointcutText;

   protected Button editPointcutButton, choosePointcutButton;

   protected TableViewer interceptorList;

   protected Button addInterceptorButton, removeInterceptorButton, moveInterceptorUpButton, moveInterceptorDownButton;

   protected TableViewer adviceList;

   protected Button addAdviceButton, removeAdviceButton, moveAdviceUpButton, moveAdviceDownButton;

   protected ButtonListener buttonListener;

   protected Binding binding;

   protected AopDescriptor descriptor;

   // original state
   private String originalName;

   private String originalPointcut;

   private ArrayList originalElements;

   public EditBindingWizardPage(Binding binding, AopDescriptor descriptor)
   {
      super(binding == null ? "Create a new JBossAOP Binding" : "Edit a JBossAOP Binding");

      setTitle(binding == null ? "Create a new JBossAOP Binding" : "Edit a JBossAOP Binding");
      setMessage(binding == null
            ? "Create a new JBossAOP Pointcut Binding"
            : "Edit an existing JBossAOP Pointcut Binding");

      this.binding = binding;
      this.descriptor = descriptor;

      if (binding != null)
      {
         // save the state of the original in case of cancel.
         this.originalName = binding.getName();
         this.originalPointcut = binding.getPointcut();
         this.originalElements = new ArrayList(binding.getElements().size());
         for (Iterator i = binding.getElements().iterator(); i.hasNext();)
         {
            this.originalElements.add(i.next());
         }
      } // end if

   }

   /*
    * Some getters that we can use to make sure the state 
    * of the overall plugin remains the same in the event of
    * a cancel.
    */

   public Binding getBinding()
   {
      return binding;
   }

   public String getOriginalPointcut()
   {
      return this.originalPointcut;
   }

   public String getOriginalName()
   {
      return this.originalName;
   }

   public ArrayList getOriginalElements()
   {
      return this.originalElements;
   }

   private class ButtonListener implements SelectionListener
   {
      public void widgetDefaultSelected(SelectionEvent e)
      {
         widgetSelected(e);
      }

      public void widgetSelected(SelectionEvent e)
      {
         if (e.getSource().equals(editPointcutButton))
            editPointcutPressed();
         else if (e.getSource().equals(choosePointcutButton))
            choosePointcutPressed();
         else if (e.getSource().equals(addInterceptorButton))
            addInterceptorPressed();
         else if (e.getSource().equals(removeInterceptorButton))
            removeInterceptorPressed();
         else if (e.getSource().equals(moveInterceptorUpButton))
            moveInterceptorUpPressed();
         else if (e.getSource().equals(moveInterceptorDownButton))
            moveInterceptorDownPressed();
         else if (e.getSource().equals(addAdviceButton))
            addAdvicePressed();
         else if (e.getSource().equals(removeAdviceButton))
            removeAdvicePressed();
         else if (e.getSource().equals(moveAdviceUpButton))
            moveAdviceUpPressed();
         else if (e.getSource().equals(moveAdviceDownButton))
            moveAdviceDownPressed();
      }
   }

   private void fillGrid(Control control)
   {
      fillGrid(control, true);
   }

   private void fillGrid(Control control, boolean fillVertical)
   {
      control.setLayoutData(new GridData(GridData.FILL, fillVertical ? GridData.FILL : GridData.CENTER, true,
            fillVertical));
   }

   private void topAlignInGrid(Control control)
   {
      control.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
   }

   private void gridButton(Control control)
   {
      control.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
   }

   public void createControl(Composite parent)
   {
      buttonListener = new ButtonListener();

      Composite main = new Composite(parent, SWT.NONE);
      main.setLayout(new GridLayout(3, false));

      new Label(main, SWT.NONE).setText("Pointcut:");
      pointcutText = new Text(main, SWT.BORDER);
      pointcutText.setEnabled(false);
      fillGrid(pointcutText, false);

      if (binding != null)
         pointcutText.setText(binding.getPointcut());

      Composite editPointcutComposite = new Composite(main, SWT.NONE);
      editPointcutComposite.setLayout(new GridLayout(2, false));
      editPointcutComposite.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));

      editPointcutButton = new Button(editPointcutComposite, SWT.PUSH);
      editPointcutButton.setText("Edit...");
      editPointcutButton.addSelectionListener(buttonListener);
      editPointcutButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

      choosePointcutButton = new Button(editPointcutComposite, SWT.PUSH);
      choosePointcutButton.setText("Choose...");
      choosePointcutButton.addSelectionListener(buttonListener);
      choosePointcutButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
      List pointcuts = AopModelUtils.getPointcutsFromAop(descriptor.getAop());
      choosePointcutButton.setEnabled(pointcuts.size() > 0 ? true : false);

      createInterceptorControls(main);
      createAdviceControls(main);

      setControl(main);
   }

   private class InterceptorProvider extends LabelProvider implements IStructuredContentProvider
   {
      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }

      public Object[] getElements(Object inputElement)
      {
         if (inputElement instanceof List)
         {
            List list = (List) inputElement;
            return list.toArray();
         }

         return null;
      }

      public Image getImage(Object element)
      {
         return AopSharedImages.getImage(AopSharedImages.IMG_INTERCEPTOR);
      }

      public String getText(Object element)
      {
         if (element instanceof Interceptor)
         {
            Interceptor interceptor = (Interceptor) element;
            return interceptor.getClazz();
         }
         else
            return element.toString();
      }
   }

   protected void createInterceptorControls(Composite main)
   {
      Label label = new Label(main, SWT.NONE);
      label.setText("Interceptors:");
      topAlignInGrid(label);

      interceptorList = new TableViewer(main, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
      InterceptorProvider provider = new InterceptorProvider();
      interceptorList.setLabelProvider(provider);
      interceptorList.setContentProvider(provider);
      fillGrid(interceptorList.getTable());
      interceptorList.addSelectionChangedListener(new ISelectionChangedListener()
      {
         public void selectionChanged(SelectionChangedEvent event)
         {
            boolean empty = event.getSelection().isEmpty();

            removeInterceptorButton.setEnabled(!empty);
            moveInterceptorUpButton.setEnabled(interceptorList.getTable().getSelectionIndex() > 0);
            moveInterceptorDownButton.setEnabled(interceptorList.getTable().getSelectionIndex() < interceptorList
                  .getTable().getItemCount() - 1);
         }
      });

      Composite interceptorButtons = new Composite(main, SWT.NONE);
      interceptorButtons.setLayout(new GridLayout(1, true));
      interceptorButtons.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));

      addInterceptorButton = new Button(interceptorButtons, SWT.PUSH);
      addInterceptorButton.setText("Add...");
      addInterceptorButton.addSelectionListener(buttonListener);
      gridButton(addInterceptorButton);
      addInterceptorButton.setEnabled(binding != null);

      removeInterceptorButton = new Button(interceptorButtons, SWT.PUSH);
      removeInterceptorButton.setText("Remove");
      removeInterceptorButton.addSelectionListener(buttonListener);
      gridButton(removeInterceptorButton);
      removeInterceptorButton.setEnabled(false);

      moveInterceptorUpButton = new Button(interceptorButtons, SWT.PUSH);
      moveInterceptorUpButton.setText("Move Up");
      moveInterceptorUpButton.addSelectionListener(buttonListener);
      gridButton(moveInterceptorUpButton);
      moveInterceptorUpButton.setEnabled(false);

      moveInterceptorDownButton = new Button(interceptorButtons, SWT.PUSH);
      moveInterceptorDownButton.setText("Move Down");
      moveInterceptorDownButton.addSelectionListener(buttonListener);
      gridButton(moveInterceptorDownButton);
      moveInterceptorDownButton.setEnabled(false);

      if (binding != null)
      {
         for (Iterator iter = AopModelUtils.getInterceptorRefssFromBinding(binding).iterator(); iter.hasNext();)
         {
            interceptorList.add(iter.next());
         }
         for (Iterator iter = AopModelUtils.getInterceptorsFromBinding(binding).iterator(); iter.hasNext();)
         {
            interceptorList.add(iter.next());
         }
      }
   }

   private class AdviceProvider extends LabelProvider implements IStructuredContentProvider
   {
      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }

      public Object[] getElements(Object inputElement)
      {
         if (inputElement instanceof List)
         {
            List list = (List) inputElement;
            return list.toArray();
         }

         return null;
      }

      public Image getImage(Object element)
      {
         return AopSharedImages.getImage(AopSharedImages.IMG_ADVICE);
      }

      public String getText(Object element)
      {
         if (element instanceof Advice)
         {
            Advice advice = (Advice) element;
            return advice.getName() + " : " + advice.getAspect();
         }
         else
            return element.toString();
      }
   }

   protected void createAdviceControls(Composite main)
   {
      Label label = new Label(main, SWT.NONE);
      label.setText("Advice:");
      topAlignInGrid(label);

      adviceList = new TableViewer(main, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
      AdviceProvider provider = new AdviceProvider();
      adviceList.setLabelProvider(provider);
      adviceList.setContentProvider(provider);
      fillGrid(adviceList.getTable());
      adviceList.addSelectionChangedListener(new ISelectionChangedListener()
      {
         public void selectionChanged(SelectionChangedEvent event)
         {
            boolean empty = event.getSelection().isEmpty();

            removeAdviceButton.setEnabled(!empty);
            moveAdviceUpButton.setEnabled(adviceList.getTable().getSelectionIndex() > 0);
            moveAdviceDownButton.setEnabled(adviceList.getTable().getSelectionIndex() < adviceList.getTable()
                  .getItemCount() - 1);
         }
      });

      Composite adviceButtons = new Composite(main, SWT.NONE);
      adviceButtons.setLayout(new GridLayout(1, true));
      adviceButtons.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));

      addAdviceButton = new Button(adviceButtons, SWT.PUSH);
      addAdviceButton.setText("Add...");
      addAdviceButton.addSelectionListener(buttonListener);
      gridButton(addAdviceButton);
      addAdviceButton.setEnabled(binding != null);

      removeAdviceButton = new Button(adviceButtons, SWT.PUSH);
      removeAdviceButton.setText("Remove");
      removeAdviceButton.addSelectionListener(buttonListener);
      gridButton(removeAdviceButton);
      removeAdviceButton.setEnabled(false);

      moveAdviceUpButton = new Button(adviceButtons, SWT.PUSH);
      moveAdviceUpButton.setText("Move Up");
      moveAdviceUpButton.addSelectionListener(buttonListener);
      gridButton(moveAdviceUpButton);
      moveAdviceUpButton.setEnabled(false);

      moveAdviceDownButton = new Button(adviceButtons, SWT.PUSH);
      moveAdviceDownButton.setText("Move Down");
      moveAdviceDownButton.addSelectionListener(buttonListener);
      gridButton(moveAdviceDownButton);
      moveAdviceDownButton.setEnabled(false);

      if (binding != null)
      {
         for (Iterator iter = AopModelUtils.getAdvicesFromBinding(binding).iterator(); iter.hasNext();)
         {
            adviceList.add(iter.next());
         }
      }
   }

   protected void editPointcutPressed()
   {
      PointcutPreviewDialog previewDialog = new PointcutPreviewDialog(null, pointcutText.getText(), getShell(),
            AopCorePlugin.getCurrentJavaProject(), PointcutPreviewDialog.UNNAMED);

      int response = -1;

      response = previewDialog.open();
      if (response == Dialog.OK)
      {
         String pointcut = previewDialog.getExpression();
         pointcutText.setText(pointcut);

         addInterceptorButton.setEnabled(pointcut != null && pointcut.length() > 0);
         addAdviceButton.setEnabled(pointcut != null && pointcut.length() > 0);
         if (pointcut != null && pointcut.length() > 0)
         {
            if (binding == null)
            {
               binding = descriptor.findBinding(pointcut);
            }

            binding.setPointcut(pointcut);
         }
      }
   }

   protected void choosePointcutPressed()
   {
      PointcutChooseDialog dialog = new PointcutChooseDialog(getShell());

      int response = dialog.open();

      if (response == Dialog.OK && dialog.getPointcut() != null)
      {
         Pointcut pointcut = dialog.getPointcut();
         pointcutText.setText(pointcut.getName());
         addInterceptorButton.setEnabled(pointcutText.getText() != null && pointcutText.getText().length() > 0);
         addAdviceButton.setEnabled(pointcutText.getText() != null && pointcutText.getText().length() > 0);
         if (binding == null)
         {
            binding = descriptor.findBinding(pointcut.getName());
         }
         binding.setPointcut(pointcut.getName());
      }
   }

   private Interceptor getSelectedInterceptor()
   {
      IStructuredSelection selection = (IStructuredSelection) interceptorList.getSelection();
      Object element = selection.getFirstElement();
      if (element instanceof Interceptor)
      {
         Interceptor interceptor = (Interceptor) element;
         return interceptor;
      }

      return null;
   }

   private InterceptorRef getSelectedInterceptorRef()
   {
      IStructuredSelection selection = (IStructuredSelection) interceptorList.getSelection();
      Object element = selection.getFirstElement();
      if (element instanceof InterceptorRef)
      {
         InterceptorRef interceptor = (InterceptorRef) element;
         return interceptor;
      }

      return null;
   }

   private void addInterceptorPressed()
   {
      ArrayList interceptors = AdvisorDialogUtil.openInterceptorDialog(getShell());
      for (Iterator iter = interceptors.iterator(); iter.hasNext();)
      {
         IType type = (IType) iter.next();
         Interceptor interceptor = descriptor.bindInterceptor(binding.getPointcut(), type.getFullyQualifiedName());

         interceptorList.add(interceptor);
      }
   }

   private void removeInterceptorPressed()
   {
      Interceptor interceptor = getSelectedInterceptor();
      if (interceptor != null)
      {
         binding.getElements().remove(interceptor);
         interceptorList.remove(interceptor);
      }

      InterceptorRef interceptorRef = getSelectedInterceptorRef();
      if (interceptorRef != null)
      {
         binding.getElements().remove(interceptorRef);
         interceptorList.remove(interceptorRef);
      }
   }

   private void moveInterceptorUpPressed()
   {
      Interceptor interceptor = getSelectedInterceptor();
      if (interceptor != null)
      {
         int position = interceptorList.getTable().getSelectionIndex();
         if (position - 1 >= 0)
         {
            interceptorList.remove(interceptor);
            interceptorList.insert(interceptor, position - 1);
            interceptorList.getTable().select(position - 1);

            List elements = binding.getElements();
            int currentIndex = elements.indexOf(interceptor);

            for (int i = currentIndex - 1; i >= 0; i++)
            {
               if (elements.get(i) instanceof Interceptor)
               {
                  binding.getElements().remove(currentIndex);
                  binding.getElements().add(i, interceptor);
                  break;
               }
            }

         }
      }

      InterceptorRef interceptorRef = getSelectedInterceptorRef();
      if (interceptorRef != null)
      {
         int position = interceptorList.getTable().getSelectionIndex();
         if (position - 1 >= 0)
         {
            interceptorList.remove(interceptorRef);
            interceptorList.insert(interceptorRef, position - 1);
            interceptorList.getTable().select(position - 1);

            List elements = binding.getElements();
            int currentIndex = elements.indexOf(interceptorRef);

            for (int i = currentIndex - 1; i >= 0; i++)
            {
               if (elements.get(i) instanceof InterceptorRef)
               {
                  binding.getElements().remove(currentIndex);
                  binding.getElements().add(i, interceptorRef);
                  break;
               }
            }
         }
      }
   }

   private void moveInterceptorDownPressed()
   {
      Interceptor interceptor = getSelectedInterceptor();
      if (interceptor != null)
      {
         int position = interceptorList.getTable().getSelectionIndex();
         if (position + 1 < interceptorList.getTable().getItemCount())
         {
            interceptorList.remove(interceptor);
            interceptorList.insert(interceptor, position + 1);
            interceptorList.getTable().select(position + 1);

            List elements = binding.getElements();
            int currentIndex = elements.indexOf(interceptor);

            for (int i = currentIndex + 1; i < elements.size(); i++)
            {
               if (elements.get(i) instanceof Interceptor)
               {
                  binding.getElements().remove(currentIndex);
                  binding.getElements().add(i, interceptor);
                  break;
               }
            }
         }
      }

      InterceptorRef interceptorRef = getSelectedInterceptorRef();
      if (interceptorRef != null)
      {
         int position = interceptorList.getTable().getSelectionIndex();
         if (position + 1 < interceptorList.getTable().getItemCount())
         {
            interceptorList.remove(interceptorRef);
            interceptorList.insert(interceptorRef, position + 1);
            interceptorList.getTable().select(position + 1);

            List elements = binding.getElements();
            int currentIndex = elements.indexOf(interceptorRef);

            for (int i = currentIndex + 1; i < elements.size(); i++)
            {
               if (elements.get(i) instanceof InterceptorRef)
               {
                  binding.getElements().remove(currentIndex);
                  binding.getElements().add(i, interceptorRef);
                  break;
               }
            }

         }
      }
   }

   private Advice getSelectedAdvice()
   {
      IStructuredSelection selection = (IStructuredSelection) adviceList.getSelection();
      Object element = selection.getFirstElement();
      if (element instanceof Advice)
      {
         Advice advice = (Advice) element;
         return advice;
      }

      return null;
   }

   private void addAdvicePressed()
   {
      IMethod adviceMethods[] = AdvisorDialogUtil.openAdviceDialog(binding, getShell(), new NullProgressMonitor());
      for (int i = 0; i < adviceMethods.length; i++)
      {
         Advice advice = descriptor.bindAdvice(binding.getPointcut(), adviceMethods[i].getDeclaringType()
               .getFullyQualifiedName(), adviceMethods[i].getElementName());

         adviceList.add(advice);
      }
   }

   private void removeAdvicePressed()
   {
      Advice advice = getSelectedAdvice();
      if (advice != null)
      {
         binding.getElements().remove(advice);
         adviceList.remove(advice);
      }
   }

   private void moveAdviceUpPressed()
   {
      Advice advice = getSelectedAdvice();
      if (advice != null)
      {
         int position = adviceList.getTable().getSelectionIndex();
         if (position - 1 >= 0)
         {
            adviceList.remove(advice);
            adviceList.insert(advice, position - 1);
            adviceList.getTable().select(position - 1);

            List elements = binding.getElements();
            int currentIndex = elements.indexOf(advice);

            for (int i = currentIndex - 1; i < elements.size(); i++)
            {
               if (elements.get(i) instanceof Advice)
               {
                  binding.getElements().remove(currentIndex);
                  binding.getElements().add(i, advice);
                  break;
               }
            }
         }
      }
   }

   private void moveAdviceDownPressed()
   {
      Advice advice = getSelectedAdvice();
      if (advice != null)
      {
         int position = adviceList.getTable().getSelectionIndex();
         if (position + 1 < adviceList.getTable().getItemCount())
         {
            adviceList.remove(advice);
            adviceList.insert(advice, position + 1);
            adviceList.getTable().select(position + 1);

            List elements = binding.getElements();
            int currentIndex = elements.indexOf(advice);

            for (int i = currentIndex + 1; i < elements.size(); i++)
            {
               if (elements.get(i) instanceof Advice)
               {
                  binding.getElements().remove(currentIndex);
                  binding.getElements().add(i, advice);
                  break;
               }
            }
         }
      }
   }
}
