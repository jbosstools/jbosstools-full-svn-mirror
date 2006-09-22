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
package org.jboss.ide.eclipse.jdt.aop.ui.views;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModel;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvised;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvisor;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopModelChangeListener;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopTypeMatcher;
import org.jboss.ide.eclipse.jdt.aop.ui.util.JumpToCodeUtil;
import org.jboss.ide.eclipse.jdt.aop.ui.views.providers.AdvisedMembersContentProvider;
import org.jboss.ide.eclipse.jdt.aop.ui.views.providers.AdvisedMembersLabelProvider;

/**
 * @author Marshall
 */
public class AdvisedMembersView extends ViewPart implements IAopModelChangeListener
{

   private IJavaElement input;

   private Label noAdvisedMembers;

   private TreeViewer advisedMembersTree;

   private Tree tree;

   private Composite main;

   private StackLayout stackLayout;

   private AdvisedMembersContentProvider contentProvider;

   private AdvisedMembersLabelProvider labelProvider;

   private static AdvisedMembersView instance;

   public AdvisedMembersView()
   {
      instance = this;
   }

   public void createPartControl(Composite parent)
   {
      //main = new Composite(parent, SWT.NONE);
      //stackLayout = new StackLayout();
      //main.setLayout(stackLayout);

      //noAdvisedMembers = new Label(main, SWT.NONE);
      //noAdvisedMembers.setText("There are no advised members for this class.");

      advisedMembersTree = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
      advisedMembersTree.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

      contentProvider = new AdvisedMembersContentProvider();
      advisedMembersTree.setContentProvider(contentProvider);

      labelProvider = new AdvisedMembersLabelProvider();
      advisedMembersTree.setLabelProvider(labelProvider);

      advisedMembersTree.addDoubleClickListener(JumpToCodeUtil.getDoubleClickListener());

      AopModel.instance().addAopModelChangeListener(this);

      //stackLayout.topControl = tree;
      //main.layout();
   }

   public void setFocus()
   {
   }

   public void setInput(IJavaElement element)
   {
      if (element != null)
      {
         if (!advisedMembersTree.getControl().isDisposed())
         {
            input = element;

            //IJavaProject project = element.getJavaProject();
            //report = AopCorePlugin.getDefault().getProjectReport(project);

            advisedMembersTree.setInput(element);
         }
      }
   }

   public void showNoAdvisedMembers()
   {
      stackLayout.topControl = noAdvisedMembers;
      main.layout();
   }

   public void refresh()
   {
      final IJavaElement finalElement = input;
      if (!advisedMembersTree.getControl().isDisposed())
      {
         Display display = advisedMembersTree.getControl().getDisplay();

         if (!display.isDisposed())
         {
            display.asyncExec(new Runnable()
            {
               public void run()
               {
                  //make sure the tree still exists
                  if (advisedMembersTree != null && advisedMembersTree.getControl().isDisposed())
                     return;

                  setInput(finalElement);
               }
            });
         }
      }
   }

   public static AdvisedMembersView instance()
   {
      return instance;
   }

   public void advisorAdded(IAopAdvised advised, IAopAdvisor advisor)
   {
      refresh();
   }

   public void advisorRemoved(IAopAdvised advised, IAopAdvisor advisor)
   {
      refresh();
   }

   /* (non-Javadoc)
    * @see org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopModelChangeListener#typeMatchAdded(org.eclipse.jdt.core.IType, org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopTypeMatcher)
    */
   public void typeMatchAdded(IType type, IAopTypeMatcher matcher)
   {
      // TODO Auto-generated method stub

   }

   /* (non-Javadoc)
    * @see org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopModelChangeListener#typeMatchRemoved(org.eclipse.jdt.core.IType, org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopTypeMatcher)
    */
   public void typeMatchRemoved(IType type, IAopTypeMatcher matcher)
   {
      // TODO Auto-generated method stub

   }
}
