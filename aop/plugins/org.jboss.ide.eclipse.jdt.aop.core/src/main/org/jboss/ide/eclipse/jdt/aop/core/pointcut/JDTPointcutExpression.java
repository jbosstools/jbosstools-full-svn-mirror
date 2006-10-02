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
package org.jboss.ide.eclipse.jdt.aop.core.pointcut;

import java.util.ArrayList;
import java.util.Iterator;

import javassist.NotFoundException;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.aop.pointcut.Pointcut;
import org.jboss.aop.pointcut.PointcutExpression;
import org.jboss.aop.pointcut.ast.ParseException;
import org.jboss.ide.eclipse.jdt.aop.core.matchers.JDTExecutionConstructorMatcher;
import org.jboss.ide.eclipse.jdt.aop.core.matchers.JDTExecutionMethodMatcher;
import org.jboss.ide.eclipse.jdt.aop.core.matchers.JDTFieldGetMatcher;
import org.jboss.ide.eclipse.jdt.aop.core.matchers.JDTFieldMatcher;
import org.jboss.ide.eclipse.jdt.aop.core.matchers.JDTFieldSetMatcher;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAdvisedCollector;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvised;

/**
 * @author Marshall
 */
public class JDTPointcutExpression extends PointcutExpression
{

   protected ArrayList advised;

   private IAdvisedCollector collector;

   public JDTPointcutExpression(Pointcut pointcut) throws ParseException
   {
      super(pointcut.getName(), pointcut.getExpr());

      this.advised = new ArrayList();
   }

   public boolean matchesGet(IField field)
   {
      try
      {
         JDTFieldMatcher matcher = new JDTFieldGetMatcher(field, ast);
         return matcher.matches();
      }
      catch (NotFoundException e)
      {
         return false;
      }
   }

   public boolean matchesSet(IField field)
   {
      try
      {
         JDTFieldMatcher matcher = new JDTFieldSetMatcher(field, ast);
         return matcher.matches();
      }
      catch (NotFoundException e)
      {
         return false;
      }
   }

   public boolean matchesExecution(IMethod method)
   {
      try
      {
         if (method.isConstructor())
         {
            JDTExecutionConstructorMatcher matcher = new JDTExecutionConstructorMatcher(method, ast);
            return matcher.matches();
         }
         else
         {
            JDTExecutionMethodMatcher matcher = new JDTExecutionMethodMatcher(method, ast);
            return matcher.matches();
         }
      }
      catch (JavaModelException e)
      {
         //e.printStackTrace();
      }
      catch (NotFoundException e)
      {
      }
      return false;
   }

   public IAopAdvised getAdvised(IField field, int type)
   {
      return getAdvised(((IMember) field), type);
   }

   public IAopAdvised getAdvised(IMethod method)
   {
      return getAdvised(((IMember) method), IAopAdvised.TYPE_METHOD_EXECUTION);
   }

   private IAopAdvised getAdvised(IMember member, int type)
   {
      Iterator i = advised.iterator();
      while (i.hasNext())
      {
         boolean there = false;
         IAopAdvised adv = (IAopAdvised) i.next();
         if (adv.getAdvisedElement().equals(member) && type == adv.getType())
         {
            return adv;
         }
      }
      return null;
   }

   public boolean equals(Object other)
   {
      if (other instanceof JDTPointcutExpression)
      {
         JDTPointcutExpression otherExpr = (JDTPointcutExpression) other;
         return (otherExpr.getExpr() != null && (otherExpr.getExpr().equals(expr)));
      }
      return false;
   }

   public void addAdvised(IAopAdvised advised)
   {
      this.advised.add(advised);
   }

   public void addAdvised(IAopAdvised advised[])
   {
      for (int i = 0; i < advised.length; i++)
      {
         addAdvised(advised[i]);
      }
   }

   public void removeAdvised(IAopAdvised advised)
   {
      this.advised.remove(advised);
   }

   public boolean hasAdvised(IAopAdvised advised)
   {
      return this.advised.contains(advised);
   }

   public IAopAdvised[] getAdvised()
   {
      return (IAopAdvised[]) advised.toArray(new IAopAdvised[advised.size()]);
   }

   public void setCollector(IAdvisedCollector collector)
   {
      this.collector = collector;
   }

   public IAdvisedCollector getCollector()
   {
      return collector;
   }
}
