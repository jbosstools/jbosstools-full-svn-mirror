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

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModel;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvisor;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopTypeMatcher;

/**
 * @author Marshall
 */
public class AdvisedMembersContentProvider implements IStructuredContentProvider, ITreeContentProvider
{
   public static final Object[] NO_ADVISED_CHILDREN = new Object[]
   {"noadvisedchildren"};

   public static final String MATCHED_TYPEDEFS = "__MATCHED_TYPEDEFS__";

   public static final String MATCHED_INTRODUCTIONS = "__MATCHED_INTRODUCTIONS__";

   public Object[] getChildren(Object parentElement)
   {
      if (parentElement instanceof ICompilationUnit)
      {
         ICompilationUnit unit = (ICompilationUnit) parentElement;
         ArrayList types = new ArrayList();
         try
         {
            types.addAll(Arrays.asList(unit.getAllTypes()));
            types.remove(unit.findPrimaryType());
         }
         catch (JavaModelException jme)
         {
         }
         ArrayList children = new ArrayList();
         children.add(new TypeMatcherWrapper(MATCHED_TYPEDEFS, unit.findPrimaryType()));
         children.add(new TypeMatcherWrapper(MATCHED_INTRODUCTIONS, unit.findPrimaryType()));
         children.addAll(AopModel.instance().getAdvisedChildren(unit.findPrimaryType()));
         children.addAll(types);

         if (children.size() > 0)
            return children.toArray();
         else
         {
            return NO_ADVISED_CHILDREN;
         }

      }
      else if (parentElement instanceof IType)
      {
         IType type = (IType) parentElement;
         ArrayList children = new ArrayList();

         children.addAll(AopModel.instance().getAdvisedChildren(type));

         if (children.size() > 0)
            return children.toArray();
      }
      else if (parentElement instanceof IMethod || parentElement instanceof IField)
      {
         return AopModel.instance().getElementAdvisors((IJavaElement) parentElement);
      }

      else if (parentElement instanceof TypeMatcherWrapper)
      {

         TypeMatcherWrapper matcher = (TypeMatcherWrapper) parentElement;
         IType type = matcher.getItype();
         IAopTypeMatcher[] rets = new IAopTypeMatcher[]
         {};
         if (matcher.getType().equals(MATCHED_TYPEDEFS))
            rets = AopModel.instance().getTypedefTypeMatchers(matcher.getItype());
         else if (matcher.getType().equals(MATCHED_INTRODUCTIONS))
         {
            rets = AopModel.instance().getIntroductionTypeMatchers(matcher.getItype());
         }
         return rets;
      }

      return new Object[0];
   }

   public Object getParent(Object element)
   {
      if (element instanceof IMethod)
      {
         return ((IMethod) element).getDeclaringType();
      }

      else if (element instanceof IField)
      {
         return ((IField) element).getDeclaringType();
      }

      else if (element instanceof IType)
      {
         IType type = (IType) element;
         if (type.getParent() != null)
         {
            return type.getParent();
         }
      }

      return null;
   }

   public boolean hasChildren(Object element)
   {
      if (element instanceof ICompilationUnit)
      {
         ICompilationUnit unit = (ICompilationUnit) element;
         return true;
         //findAdvisedChildren(unit.findPrimaryType()).size() > 0;	
      }
      else if (element instanceof IType)
      {
         IType type = (IType) element;
         return AopModel.instance().getAdvisedChildren(type).size() > 0;
      }
      else if (element instanceof IMethod || element instanceof IField)
      {
         try
         {
            IAopAdvisor advisors[] = AopModel.instance().getElementAdvisors((IJavaElement) element);
            return !(advisors == null || advisors.length == 0);
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
      return false;
   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
   {
   }

   public Object[] getElements(Object inputElement)
   {
      return getChildren(inputElement);
   }

   public void dispose()
   {
   }

   public static class TypeMatcherWrapper
   {
      public String type;

      public IType itype;

      public TypeMatcherWrapper(String type, IType itype)
      {
         this.type = type;
         this.itype = itype;
      }

      public IType getItype()
      {
         return itype;
      }

      public String getType()
      {
         return type;
      }
   }
}