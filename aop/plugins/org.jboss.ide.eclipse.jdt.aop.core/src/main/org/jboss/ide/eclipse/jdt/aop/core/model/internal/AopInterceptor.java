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
package org.jboss.ide.eclipse.jdt.aop.core.model.internal;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopInterceptor;

public class AopInterceptor extends AopAdvisor implements IAopInterceptor
{
   private String name;

   public AopInterceptor(IJavaProject project, String fqClassName, String name) throws JavaModelException
   {
      super(JavaModelUtil.findType(project, fqClassName));
      this.name = name;
   }

   public AopInterceptor(IType interceptorType)
   {
      super(interceptorType);
   }

   public IType getAdvisingType()
   {
      return (IType) getAdvisingElement();
   }

   public String getName()
   {
      return name;
   }

   public int getType()
   {
      return INTERCEPTOR;
   }
}