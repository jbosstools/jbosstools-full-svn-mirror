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
package org.jboss.ide.eclipse.jdt.aop.core.model.internal;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvice;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAspect;

/**
 * @author Marshall
 */
public class AopAspect implements IAopAspect
{

   private IType aspectType;

   private ArrayList advice;

   public AopAspect(IJavaProject project, String fqClassName)
   {
      try
      {

         aspectType = JavaModelUtil.findType(project, fqClassName);

      }
      catch (JavaModelException e)
      {
         e.printStackTrace();
      }

      advice = new ArrayList();
   }

   public boolean hasAdvice(IMethod method)
   {
      for (Iterator iter = this.advice.iterator(); iter.hasNext();)
      {
         IAopAdvice advice = (IAopAdvice) iter.next();

         if (advice.getAdvisingMethod().equals(method))
         {
            return true;
         }
      }

      return false;
   }

   public IAopAdvice addAdvice(IMethod adviceMethod)
   {
      AopAdvice advice = new AopAdvice(this, adviceMethod);
      this.advice.add(advice);

      return advice;
   }

   public void removeAdvice(IAopAdvice advice)
   {
      this.advice.remove(advice);
   }

   public IAopAdvice[] getAdvice()
   {
      return (IAopAdvice[]) advice.toArray(new IAopAdvice[advice.size()]);
   }

   public IType getType()
   {
      return aspectType;
   }
}
