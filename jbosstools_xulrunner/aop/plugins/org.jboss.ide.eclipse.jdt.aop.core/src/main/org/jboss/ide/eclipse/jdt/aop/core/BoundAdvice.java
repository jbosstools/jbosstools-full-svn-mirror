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
package org.jboss.ide.eclipse.jdt.aop.core;

import org.eclipse.jdt.core.IMethod;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.impl.AdviceImpl;

/**
 * @author Marshall
 */
public class BoundAdvice extends AdviceImpl
{

   private IMethod method;

   private BoundPointcut pointcut;

   private boolean checked;

   public IMethod getMethod()
   {
      return method;
   }

   public void setMethod(IMethod method)
   {
      this.method = method;

      setAspect(method.getDeclaringType().getFullyQualifiedName());
      setName(method.getElementName());
   }

   public BoundPointcut getPointcut()
   {
      return pointcut;
   }

   public void setPointcut(BoundPointcut pointcut)
   {
      this.pointcut = pointcut;
   }

   public boolean isChecked()
   {
      return checked;
   }

   public void setChecked(boolean checked)
   {
      this.checked = checked;
   }
}
