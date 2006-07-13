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
import java.util.Arrays;

import org.eclipse.jdt.core.IType;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopTypeMatcher;

/**
 * @author Rob Stryker
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AopTypeMatcher implements IAopTypeMatcher
{

   protected ArrayList matchedTypes;

   private int myType;

   public AopTypeMatcher(int type)
   {
      matchedTypes = new ArrayList();
      this.myType = type;
   }

   public IType[] getMatched()
   {
      IType[] types = new IType[matchedTypes.size()];
      for (int i = 0; i < matchedTypes.size(); i++)
      {
         types[i] = (IType) matchedTypes.get(i);
      }
      return types;
   }

   public int getType()
   {
      return this.myType;
   }

   public void removeMatchedType(IType type)
   {
      matchedTypes.remove(type);
   }

   public void addMatchedType(IType type)
   {
      matchedTypes.add(type);
   }

   public void addMatchedType(IType[] types)
   {
      matchedTypes.addAll(Arrays.asList(types));
   }

   public boolean matches(IType type)
   {
      if (matchedTypes.contains(type))
         return true;
      return false;
   }

}
