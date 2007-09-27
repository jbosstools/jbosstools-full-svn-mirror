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

import org.eclipse.jdt.core.IJavaElement;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvised;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvisor;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTPointcutExpression;

public abstract class AopAdvisor implements IAopAdvisor, Comparable
{
   protected IJavaElement element;

   protected ArrayList advised;

   protected ArrayList expressions;

   public AopAdvisor(IJavaElement advisingElement)
   {
      this.element = advisingElement;

      advised = new ArrayList();
      expressions = new ArrayList();
   }

   public IAopAdvised[] getAdvised()
   {
      return (IAopAdvised[]) advised.toArray(new IAopAdvised[advised.size()]);
   }

   public IAopAdvised getAdvised(IJavaElement element)
   {
      for (Iterator iter = advised.iterator(); iter.hasNext();)
      {
         IAopAdvised advised = (IAopAdvised) iter.next();
         if (advised.getAdvisedElement().equals(element))
         {
            return advised;
         }
      }

      return null;
   }

   public int compareTo(Object other)
   {
      if (this.equals(other))
      {
         return 0;
      }
      else
         return -1;
   }

   public boolean equals(Object other)
   {
      if (other instanceof IAopAdvisor)
      {
         IAopAdvisor otherAdvisor = (IAopAdvisor) other;

         if (otherAdvisor.getAdvisingElement() != null)
         {
            return otherAdvisor.getAdvisingElement().equals(element);
         }
      }
      return false;
   }

   public IJavaElement getAdvisingElement()
   {
      return element;
   }

   public void removeAdvised(IAopAdvised advised)
   {
      this.advised.remove(advised);
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

   /**
    * This method checks whether an element is advised by any 
    * pointcut expression.
    * It checks through the internal list, and 
    * wraps the getAdvised(IJavaElement) method
    */
   public boolean advises(IJavaElement element)
   {
      IAopAdvised adv = getAdvised(element);
      boolean retval;
      if (adv == null)
         retval = false;
      else
         retval = true;
      return retval;
      //		return getAdvised(element) == null ? false : true;
   }

   /**
    * This method checks our internal list to see if the 
    * parameter is in it. 
    */
   public boolean advises(IAopAdvised advised)
   {
      return this.advised.contains(advised);
   }

   public JDTPointcutExpression[] getAssignedPointcuts()
   {
      return (JDTPointcutExpression[]) expressions.toArray(new JDTPointcutExpression[expressions.size()]);
   }

   public void assignPointcut(JDTPointcutExpression expression)
   {
      expressions.add(expression);
   }

   public void unassignPointcut(JDTPointcutExpression expression)
   {
      expressions.remove(expression);
   }

   public Object getAdapter(Class adapter)
   {
      if (adapter.equals(IJavaElement.class))
      {
         return element;
      }
      return null;
   }
}