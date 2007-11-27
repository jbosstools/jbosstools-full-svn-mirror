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

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.ui.dialogs.TypeSelectionDialog2;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.jdt.aop.core.BoundPointcut;
import org.jboss.ide.eclipse.jdt.aop.ui.AopUiPlugin;
import org.jboss.ide.eclipse.jdt.aop.ui.dialogs.MethodSelectionDialog;

/**
 * @author Marshall
 */
public class CreatePointcutWizard extends Wizard
{

   private IJavaElement fromElement;

   private BoundPointcut pointcut;

   private Page page;

   public CreatePointcutWizard(IJavaElement fromElement)
   {
      this.fromElement = fromElement;
      pointcut = new BoundPointcut(fromElement);
      page = new Page();
   }

   public void addPages()
   {
      addPage(page);
   }

   public boolean performFinish()
   {
      return true;
   }

   private class Page extends WizardPage
   {

      public Page()
      {
         super("Create a pointcut", "Create a pointcut", null);
      }

      private Button get, set, getAndSet;

      private Button calledByMethod, calledByMethodBrowse;

      private Text calledByMethodText, pointcutName;

      private Composite main;

      private ExtraOptionListener listener;

      public void createControl(Composite parent)
      {
         listener = new ExtraOptionListener();
         Font header = new Font(parent.getDisplay(), parent.getFont().getFontData()[0].getName(), parent.getFont()
               .getFontData()[0].getHeight() + 2, SWT.BOLD);

         main = new Composite(parent, SWT.NONE);
         main.setLayout(new GridLayout(2, true));

         new Label(main, SWT.NONE).setText("Method: ");
         Composite elementLabel = new Composite(main, SWT.NONE);
         elementLabel.setLayout(new RowLayout());

         Label label = new Label(elementLabel, SWT.NONE);
         label.setImage(AopUiPlugin.getDefault().getTreeImage(fromElement));
         Label label2 = new Label(elementLabel, SWT.NONE);
         label2.setText(AopUiPlugin.getDefault().getTreeLabel(fromElement));

         new Label(main, SWT.NONE).setText("Pointcut name: ");
         pointcutName = new Text(main, SWT.BORDER);
         pointcutName.setText(fromElement.getElementName());
         pointcut.setName(fromElement.getElementName());
         pointcutName.addModifyListener(new ModifyListener()
         {
            public void modifyText(ModifyEvent e)
            {
               pointcut.setName(pointcutName.getText());
            }
         });

         pointcutName.setLayoutData(new GridData(GridData.FILL, GridData.END, true, false));

         if (fromElement.getElementType() == IJavaElement.METHOD)
         {
            createMethodControls();
         }

         else if (fromElement.getElementType() == IJavaElement.FIELD)
         {
            createFieldControls();
         }

         setControl(main);
      }

      private void createMethodControls()
      {
         calledByMethod = new Button(main, SWT.CHECK);
         calledByMethod.setText("Called by another method");
         calledByMethod.addSelectionListener(listener);

         Composite calledByOpts = new Composite(main, SWT.NONE);
         GridLayout calledByLayout = new GridLayout(2, false);
         calledByLayout.marginHeight = calledByLayout.marginWidth = 0;
         calledByOpts.setLayout(calledByLayout);
         calledByOpts.setLayoutData(new GridData(GridData.FILL, GridData.END, true, false));

         calledByMethodText = new Text(calledByOpts, SWT.BORDER);
         calledByMethodText.setLayoutData(new GridData(GridData.FILL, GridData.END, true, false));
         calledByMethodText.setEnabled(false);

         calledByMethodBrowse = new Button(calledByOpts, SWT.PUSH);
         calledByMethodBrowse.setText("Browse...");
         calledByMethodBrowse.setEnabled(false);

         calledByMethodBrowse.addSelectionListener(new SelectionListener()
         {
            public void widgetSelected(SelectionEvent e)
            {
               IJavaElement[] elements = new IJavaElement[]
               {fromElement.getJavaProject()};
               IJavaSearchScope scope = SearchEngine.createJavaSearchScope(elements);

               TypeSelectionDialog2 dialog = new TypeSelectionDialog2(getShell(), false, getWizard().getContainer(),
                     scope, IJavaSearchConstants.CLASS);
               dialog.setTitle("Called by..");
               dialog.setMessage("Please select a class...");
               //dialog.setFilter(calledByMethodText.getText());

               if (dialog.open() == Window.OK)
               {
                  MethodSelectionDialog methodDialog = new MethodSelectionDialog(getShell(), (IType) dialog
                        .getFirstResult());
                  methodDialog.setTitle("Called by..");
                  methodDialog.setMessage("Please select a method...");

                  if (methodDialog.open() == Window.OK)
                  {
                     IMethod method = (IMethod) methodDialog.getFirstResult();
                     pointcut.setOption(BoundPointcut.METHOD_CALLED_BY_METHOD, method);

                     calledByMethodText.setText(method.getDeclaringType().getFullyQualifiedName() + "."
                           + method.getElementName());
                  }

               }
            }

            public void widgetDefaultSelected(SelectionEvent e)
            {
            }
         });
      }

      private void createFieldControls()
      {
         new Label(main, SWT.NONE).setText("Apply when field is:");
         Composite getSetOpts = new Composite(main, SWT.NONE);
         getSetOpts.setLayout(new RowLayout());

         get = new Button(getSetOpts, SWT.RADIO);
         set = new Button(getSetOpts, SWT.RADIO);
         getAndSet = new Button(getSetOpts, SWT.RADIO);
         get.setText("Get");
         set.setText("Set");
         getAndSet.setText("Get && Set");

         get.addSelectionListener(listener);
         set.addSelectionListener(listener);
         getAndSet.addSelectionListener(listener);
      }

      public void dispose()
      {
      }

      private class ExtraOptionListener implements SelectionListener
      {
         public void widgetSelected(SelectionEvent e)
         {
            if (fromElement.getElementType() == IJavaElement.FIELD)
            {
               if (e.getSource().equals(get))
               {
                  pointcut.setOption(BoundPointcut.FIELD_READ, Boolean.TRUE);
                  pointcut.setOption(BoundPointcut.FIELD_WRITE, Boolean.FALSE);
               }
               else if (e.getSource().equals(set))
               {
                  pointcut.setOption(BoundPointcut.FIELD_READ, Boolean.FALSE);
                  pointcut.setOption(BoundPointcut.FIELD_WRITE, Boolean.TRUE);
               }
               else if (e.getSource().equals(getAndSet))
               {
                  pointcut.setOption(BoundPointcut.FIELD_READ, Boolean.TRUE);
                  pointcut.setOption(BoundPointcut.FIELD_WRITE, Boolean.TRUE);
               }
            }
            else if (fromElement.getElementType() == IJavaElement.METHOD)
            {
               if (e.getSource().equals(calledByMethod))
               {
                  pointcut.setOption(BoundPointcut.USE_METHOD_CALLED_BY_METHOD, new Boolean(calledByMethod
                        .getSelection()));

                  calledByMethodText.setEnabled(calledByMethod.getSelection());
                  calledByMethodBrowse.setEnabled(calledByMethod.getSelection());
               }
            }
         }

         public void widgetDefaultSelected(SelectionEvent e)
         {
         }
      }
   }

   public BoundPointcut getPointcut()
   {
      return pointcut;
   }
}
