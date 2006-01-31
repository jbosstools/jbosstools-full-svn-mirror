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

import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopTypeMatcher;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTInterfaceIntroduction;

/**
 * While this class pushes the limits for an advisor, 
 * I think it's a useful categorization as some type
 * definition references (or advises) some IType.
 * 
 * 
 * @author Rob Stryker
 *
 */
public class AopInterfaceIntroduction extends AopTypeMatcher
{

   private JDTInterfaceIntroduction expr;

   public AopInterfaceIntroduction(JDTInterfaceIntroduction expr)
   {
      super(IAopTypeMatcher.INTRODUCTION);
      this.expr = expr;
   }

   public JDTInterfaceIntroduction getIntroduction()
   {
      return expr;
   }

   public boolean equals(Object other)
   {
      if (other instanceof AopInterfaceIntroduction)
      {
         AopInterfaceIntroduction otherr = (AopInterfaceIntroduction) other;
         return otherr.getIntroduction().equals(getIntroduction());
      }
      else
      {
         return false;
      }
   }

}
