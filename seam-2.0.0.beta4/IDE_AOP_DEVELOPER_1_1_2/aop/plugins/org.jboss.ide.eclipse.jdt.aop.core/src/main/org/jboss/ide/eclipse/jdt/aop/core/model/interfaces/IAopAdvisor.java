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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaElement;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTPointcutExpression;

/**
 * @author Marshall
 */
public interface IAopAdvisor extends IAdaptable
{

   public static final int ADVICE = 0;

   public static final int INTERCEPTOR = 1;

   public static final int TYPEDEF = 2;

   public IJavaElement getAdvisingElement();

   public IAopAdvised[] getAdvised();

   public IAopAdvised getAdvised(IJavaElement element);

   public int getType();

   public void removeAdvised(IAopAdvised advised);

   public void addAdvised(IAopAdvised advised);

   public void addAdvised(IAopAdvised advised[]);

   public boolean advises(IJavaElement element);

   public boolean advises(IAopAdvised advised);

   public JDTPointcutExpression[] getAssignedPointcuts();

   public void assignPointcut(JDTPointcutExpression expression);

   public void unassignPointcut(JDTPointcutExpression expression);
}
