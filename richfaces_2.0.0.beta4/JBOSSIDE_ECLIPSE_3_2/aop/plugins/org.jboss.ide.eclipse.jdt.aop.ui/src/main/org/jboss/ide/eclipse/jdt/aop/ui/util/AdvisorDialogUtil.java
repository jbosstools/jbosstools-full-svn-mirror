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
package org.jboss.ide.eclipse.jdt.aop.ui.util;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.BoundAdvice;
import org.jboss.ide.eclipse.jdt.aop.core.BoundPointcut;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Binding;
import org.jboss.ide.eclipse.jdt.aop.core.util.AspectValidator;
import org.jboss.ide.eclipse.jdt.aop.core.util.PointcuttableValidator;
import org.jboss.ide.eclipse.jdt.aop.ui.wizards.AdviceWizard;
import org.jboss.ide.eclipse.jdt.aop.ui.wizards.InterceptorWizard;

/**
 * @author Marshall
 *
 * This is a utility class for prompting the user with an Interceptor or Advice selection dialog.
 */
public class AdvisorDialogUtil
{

   protected static ArrayList availableInterceptors = new ArrayList();

   protected static ArrayList selectedInterceptors;

   public static ArrayList openInterceptorDialog(Shell shell)
   {
      getAvailableInterceptors(shell);

      return selectedInterceptors;
   }

   private static void getAvailableInterceptors(Shell shell)
   {
      try
      {
         SearchPattern pattern = SearchPattern.createPattern("org.jboss.aop.advice.Interceptor",
               IJavaSearchConstants.TYPE, IJavaSearchConstants.IMPLEMENTORS, SearchPattern.R_EXACT_MATCH);
         IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaElement[]
         {AopCorePlugin.getCurrentJavaProject()}, IJavaSearchScope.SOURCES | IJavaSearchScope.APPLICATION_LIBRARIES
               | IJavaSearchScope.REFERENCED_PROJECTS);
         SearchEngine engine = new SearchEngine();

         availableInterceptors.clear();
         engine.search(pattern, new SearchParticipant[]
         {SearchEngine.getDefaultSearchParticipant()}, scope, new Requestor(shell), new NullProgressMonitor());
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   private static class Requestor extends SearchRequestor
   {
      protected Shell shell;

      public Requestor(Shell shell)
      {
         this.shell = shell;
      }

      public void acceptSearchMatch(SearchMatch match) throws CoreException
      {
         if (match.getElement() instanceof IType)
         {
            IType type = (IType) match.getElement();

            if (!Flags.isAbstract(type.getFlags())
                  && !type.getPackageFragment().getElementName().startsWith("org.jboss.aop"))
            {
               availableInterceptors.add(match.getElement());
            }
         }
      }

      public void endReporting()
      {
         askForInterceptor(shell);
      }
   }

   private static void askForInterceptor(Shell shell)
   {
      InterceptorWizard wizard = new InterceptorWizard(availableInterceptors);
      WizardDialog dialog = new WizardDialog(shell, wizard);

      dialog.create();
      dialog.getShell().setBounds(shell.getBounds().x, shell.getBounds().y, 450, 500);

      int response = dialog.open();

      if (response == WizardDialog.OK)
      {
         selectedInterceptors = wizard.getSelectedInterceptors();
      }
   }

   public static IMethod[] openAdviceDialog(Binding binding, Shell shell, IProgressMonitor monitor)
   {
      ArrayList pointcuts = new ArrayList();
      BoundPointcut pointcut = new BoundPointcut(binding);
      ArrayList advice = getAvaiableAdvice(binding, shell, monitor);

      pointcut.setAdvice(advice);
      pointcuts.add(pointcut);

      return openAdviceDialog(pointcuts, shell, monitor);
   }

   public static IMethod[] openAdviceDialog(ArrayList pointcuts, Shell shell, IProgressMonitor monitor)
   {
      AdviceWizard wizard = new AdviceWizard(pointcuts);
      WizardDialog dialog = new WizardDialog(shell, wizard);

      dialog.create();
      dialog.getShell().setSize(450, 500);
      int response = dialog.open();

      if (response == WizardDialog.OK)
      {
         return wizard.getSelectedAdviceMethods();
      }
      else
         return new IMethod[]
         {};
   }

   public static ArrayList getAvaiableAdvice(IJavaElement element, Shell shell, IProgressMonitor monitor)
   {
      ArrayList availableAdvice = new ArrayList();
      SearchPattern pattern = SearchPattern.createPattern("*(*Invocation) Object", IJavaSearchConstants.METHOD,
            IJavaSearchConstants.DECLARATIONS, SearchPattern.R_PATTERN_MATCH);

      IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaElement[]
      {element.getJavaProject()}, IJavaSearchScope.SOURCES | IJavaSearchScope.APPLICATION_LIBRARIES);
      SearchEngine engine = new SearchEngine();
      AdviceRequestor requestor = new AdviceRequestor(element, availableAdvice, shell, monitor);

      monitor.subTask("Finding applicable advice for pointcut: " + AopCorePlugin.getDefault().getAopSignature(element));

      try
      {
         engine.search(pattern, new SearchParticipant[]
         {SearchEngine.getDefaultSearchParticipant()}, scope, requestor, monitor);

      }
      catch (CoreException e)
      {
         e.printStackTrace();
      }

      monitor.worked(1);

      return requestor.getAvailableAdvice();
   }

   public static ArrayList getAvaiableAdvice(Binding binding, Shell shell, IProgressMonitor monitor)
   {
      ArrayList availableAdvice = new ArrayList();
      SearchPattern pattern = SearchPattern.createPattern("*(Invocation) Object", IJavaSearchConstants.METHOD,
            IJavaSearchConstants.DECLARATIONS, SearchPattern.R_PATTERN_MATCH);

      IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaElement[]
      {AopCorePlugin.getCurrentJavaProject()}, IJavaSearchScope.SOURCES | IJavaSearchScope.APPLICATION_LIBRARIES
            | IJavaSearchScope.REFERENCED_PROJECTS);
      SearchEngine engine = new SearchEngine();
      AdviceRequestor requestor = new AdviceRequestor(binding, availableAdvice, shell, monitor);

      monitor.subTask("Finding applicable advice for pointcut: " + binding.getPointcut());

      try
      {
         engine.search(pattern, new SearchParticipant[]
         {SearchEngine.getDefaultSearchParticipant()}, scope, requestor, monitor);

      }
      catch (CoreException e)
      {
         e.printStackTrace();
      }

      monitor.worked(1);

      return requestor.getAvailableAdvice();
   }

   private static class AdviceRequestor extends SearchRequestor
   {
      private ArrayList availableAdvice;

      private IJavaElement forElement;

      private Binding binding;

      private Shell shell;

      private IProgressMonitor monitor;

      public AdviceRequestor(IJavaElement forElement, ArrayList advice, Shell shell, IProgressMonitor monitor)
      {
         this.forElement = forElement;
         availableAdvice = advice;
         this.shell = shell;
         this.monitor = monitor;
      }

      public AdviceRequestor(Binding binding, ArrayList advice, Shell shell, IProgressMonitor monitor)
      {
         this.binding = binding;
         availableAdvice = advice;
         this.shell = shell;
         this.monitor = monitor;
      }

      public void acceptSearchMatch(SearchMatch match) throws CoreException
      {
         if (match.getElement() instanceof IMethod)
         {
            IMethod method = (IMethod) match.getElement();

            if (PointcuttableValidator.validateMethod(method) && AspectValidator.validateMethod(method))
            {
               if (forElement != null)
               {
                  if (forElement.getElementType() == IJavaElement.METHOD
                        && AspectValidator.validateMethodAdvice(method))
                  {
                     addMethod(method);
                  }
                  else if (forElement.getElementType() == IJavaElement.FIELD
                        && AspectValidator.validateFieldAdvice(method))
                  {
                     addMethod(method);
                  }
               }
               else if (binding != null)
               {
                  addMethod(method);
               }
            }
         }

      }

      private void addMethod(IMethod method)
      {
         monitor.setTaskName("Found " + method.getDeclaringType().getFullyQualifiedName() + "."
               + method.getElementName() + "...");

         BoundAdvice advice = new BoundAdvice();
         advice.setMethod(method);
         availableAdvice.add(advice);
      }

      public void endReporting()
      {
         monitor.done();
      }

      public ArrayList getAvailableAdvice()
      {
         return availableAdvice;
      }
   }
}
