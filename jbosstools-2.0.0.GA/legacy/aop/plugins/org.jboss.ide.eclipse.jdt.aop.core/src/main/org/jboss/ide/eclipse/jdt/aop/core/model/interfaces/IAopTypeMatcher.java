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
package org.jboss.ide.eclipse.jdt.aop.core.model.interfaces;

import org.eclipse.jdt.core.IType;

/**
 * An interface declaring ITypes that are matched by some
 * type expression.
 * 
 * @author Rob Stryker
 */
public interface IAopTypeMatcher
{

   public static final int TYPEDEF = 0;

   public static final int INTRODUCTION = 1;

   public IType[] getMatched();

   public int getType();

   public void removeMatchedType(IType type);

   public void addMatchedType(IType type);

   public void addMatchedType(IType[] types);

   public boolean matches(IType type);

}
