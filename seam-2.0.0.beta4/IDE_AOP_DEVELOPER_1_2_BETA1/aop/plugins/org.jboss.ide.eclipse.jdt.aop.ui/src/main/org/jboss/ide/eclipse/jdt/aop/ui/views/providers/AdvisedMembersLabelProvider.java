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
package org.jboss.ide.eclipse.jdt.aop.ui.views.providers;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.ui.viewsupport.JavaUILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvice;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopInterceptor;
import org.jboss.ide.eclipse.jdt.aop.core.model.internal.AopInterfaceIntroduction;
import org.jboss.ide.eclipse.jdt.aop.core.model.internal.AopTypedef;
import org.jboss.ide.eclipse.jdt.aop.ui.AopSharedImages;
import org.jboss.ide.eclipse.jdt.aop.ui.AopUiPlugin;

/**
 * @author Marshall
 */
public class AdvisedMembersLabelProvider extends LabelProvider
{

   private JavaUILabelProvider javaUILabelProviderDelegate;

   public AdvisedMembersLabelProvider()
   {
      javaUILabelProviderDelegate = new JavaUILabelProvider();
   }

   public Image getImage(Object element)
   {
      if (element instanceof IAopAdvice)
      {
         return AopSharedImages.getImage(AopSharedImages.IMG_ADVICE_24);
      }
      else if (element instanceof IAopInterceptor)
      {
         return AopSharedImages.getImage(AopSharedImages.IMG_INTERCEPTOR_24);
      }

      else if (element instanceof IJavaElement)
      {

         // For some reason, when we try to pass the image handling off to the delegate
         // our icons are stretched and look ugly.. 
         //return AopUiPlugin.getDefault().getTreeImage((IJavaElement)element);
         return javaUILabelProviderDelegate.getImage(element);
      }

      else if (element instanceof AdvisedMembersContentProvider.TypeMatcherWrapper)
      {

         AdvisedMembersContentProvider.TypeMatcherWrapper matcher = (AdvisedMembersContentProvider.TypeMatcherWrapper) element;

         if (matcher.getType().equals(AdvisedMembersContentProvider.MATCHED_TYPEDEFS))
         {
            return AopSharedImages.getImage(AopSharedImages.IMG_TYPEDEF24);
         }
         else if (matcher.getType().equals(AdvisedMembersContentProvider.MATCHED_INTRODUCTIONS))
         {
            return AopSharedImages.getImage(AopSharedImages.IMG_INTRODUCTION24);
         }
      }
      else if (element instanceof AopTypedef)
      {
         return AopSharedImages.getImage(AopSharedImages.IMG_TYPEDEF24);
      }
      else if (element instanceof AopInterfaceIntroduction)
      {
         return AopSharedImages.getImage(AopSharedImages.IMG_INTRODUCTION24);
      }

      else if (element.equals(AdvisedMembersContentProvider.NO_ADVISED_CHILDREN[0]))
      {
         return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK);
      }

      return null;
   }

   public String getText(Object element)
   {

      if (element instanceof IAopAdvice)
      {
         IAopAdvice advice = (IAopAdvice) element;

         return advice.getAdvisingMethod().getDeclaringType().getElementName() + "."
               + javaUILabelProviderDelegate.getText(advice.getAdvisingElement());
      }
      else if (element instanceof IAopInterceptor)
      {
         IAopInterceptor interceptor = (IAopInterceptor) element;

         return javaUILabelProviderDelegate.getText(interceptor.getAdvisingElement());
      }
      else if (element.equals(AdvisedMembersContentProvider.NO_ADVISED_CHILDREN[0]))
      {
         return "No Advised Members.";
      }

      if (element instanceof IType)
      {
         return ((IType) element).getElementName();
      }

      else if (element instanceof AdvisedMembersContentProvider.TypeMatcherWrapper)
      {

         AdvisedMembersContentProvider.TypeMatcherWrapper matcher = (AdvisedMembersContentProvider.TypeMatcherWrapper) element;

         if (matcher.getType().equals(AdvisedMembersContentProvider.MATCHED_TYPEDEFS))
         {
            return "Matched Typedefs";
         }
         else if (matcher.getType().equals(AdvisedMembersContentProvider.MATCHED_INTRODUCTIONS))
         {
            return "Matched Introductions";
         }
      }
      else if (element instanceof AopTypedef)
      {
         return ((AopTypedef) element).getTypedef().getName() + " : " + ((AopTypedef) element).getTypedef().getExpr();
      }
      else if (element instanceof AopInterfaceIntroduction)
      {
         return ((AopInterfaceIntroduction) element).getIntroduction().getExpr();
      }

      return javaUILabelProviderDelegate.getText(element);
   }
}
