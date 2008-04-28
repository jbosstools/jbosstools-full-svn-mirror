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

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.TwoPaneElementSelector;
import org.jboss.ide.eclipse.jdt.aop.ui.AopUiPlugin;

/**
 * @author Marshall
 */
public class MethodSelectionDialog extends TwoPaneElementSelector
{

   private IType type;

   public MethodSelectionDialog(Shell shell, IType type)
   {
      super(shell, new MethodLabelProvider(), new MethodLabelProvider(true));
      this.type = type;
   }

   private static class MethodLabelProvider extends LabelProvider
   {
      private boolean showClass;

      public MethodLabelProvider()
      {
         this(false);
      }

      public MethodLabelProvider(boolean showClass)
      {
         this.showClass = showClass;
      }

      public Image getImage(Object element)
      {
         if (!showClass)
            return AopUiPlugin.getDefault().getTreeImage((IJavaElement) element);
         else
            return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_CLASS);
      }

      public String getText(Object element)
      {

         try
         {
            IMethod method = (IMethod) element;
            if (!showClass)
            {
               String methodLabel = Signature.toString(method.getReturnType()) + " ";
               methodLabel += method.getElementName() + "(";
               String paramNames[] = method.getParameterNames();
               String paramTypes[] = method.getParameterTypes();

               for (int i = 0; i < paramNames.length; i++)
               {
                  methodLabel += Signature.toString(paramTypes[i]);
                  methodLabel += " " + paramNames[i];

                  if (i < paramNames.length - 1)
                  {
                     methodLabel += ", ";
                  }
               }
               methodLabel += ")";
               return methodLabel;
            }
            else
            {
               return method.getDeclaringType().getElementName();
            }
         }
         catch (JavaModelException e)
         {
            e.printStackTrace();
         }
         return null;
      }
   }

   public int open()
   {

      try
      {
         setElements(type.getMethods());
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }

      return super.open();
   }
}
