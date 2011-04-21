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
package org.jboss.ide.eclipse.jdt.aop.core.model.interfaces;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

/**
 * @author Marshall
 */
public interface IAopAspect
{

   /**
    * @return A list of IAopAdvice for this Aspect
    */
   public IAopAdvice[] getAdvice();

   /**
    * @param advice
    * @return Whether or not this aspect has the passed in advice.
    */
   public boolean hasAdvice(IMethod advice);

   /**
    * Add an advice method to this aspect
    */
   public IAopAdvice addAdvice(IMethod method);

   /**
    * Remove advice from this aspect
    */
   public void removeAdvice(IAopAdvice advice);

   /**
    * @return The JDT IType corresponding with this Aspect
    */
   public IType getType();
}
