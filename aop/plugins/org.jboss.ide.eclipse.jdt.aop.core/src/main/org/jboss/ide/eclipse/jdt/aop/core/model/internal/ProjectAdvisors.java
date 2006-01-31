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
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvice;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvisor;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAspect;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopInterceptor;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTInterfaceIntroduction;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTTypedefExpression;

/**
 * @author Marshall
 */
public class ProjectAdvisors
{

   private Hashtable aspects;

   private ArrayList interceptors;

   private Hashtable typedefs;

   private Hashtable introductions;

   private IJavaProject project;

   public ProjectAdvisors(IJavaProject project)
   {
      this.project = project;
      aspects = new Hashtable();
      typedefs = new Hashtable();
      interceptors = new ArrayList();
      introductions = new Hashtable();
   }

   public boolean hasAspect(String fqClassName)
   {
      return aspects.containsKey(fqClassName);
   }

   public void removeAspect(IAopAspect aspect)
   {
      aspects.remove(aspect);
   }

   public IAopAspect addAspect(String fqClassName)
   {
      if (!hasAspect(fqClassName))
      {
         AopAspect aspect = new AopAspect(project, fqClassName);
         aspects.put(fqClassName, aspect);
         return aspect;
      }
      return (IAopAspect) aspects.get(fqClassName);
   }

   public void removeInterceptor(IAopInterceptor interceptor)
   {
      interceptors.remove(interceptor);
   }

   public IAopInterceptor addInterceptor(String fqClassName)
   {
      return addInterceptor(fqClassName, null);
   }

   public boolean hasInterceptor(String fqClassName)
   {
      for (Iterator iter = interceptors.iterator(); iter.hasNext();)
      {
         IAopInterceptor interceptor = (IAopInterceptor) iter.next();
         if (interceptor.getAdvisingType().getFullyQualifiedName().equals(fqClassName))
         {
            return true;
         }
      }

      return false;
   }

   public IAopInterceptor addInterceptor(String fqClassName, String name)
   {
      try
      {
         if (name == null || name == "")
            name = fqClassName;
         AopInterceptor interceptor = new AopInterceptor(project, fqClassName, name);
         interceptors.add(interceptor);

         return interceptor;
      }
      catch (JavaModelException e)
      {
         e.printStackTrace();
      }

      return null;
   }

   public IAopInterceptor findInterceptor(String name)
   {
      for (Iterator iter = interceptors.iterator(); iter.hasNext();)
      {
         IAopInterceptor interceptor = (IAopInterceptor) iter.next();
         if (interceptor.getName() != null && interceptor.getName().equals(name))
         {
            return interceptor;
         }
      }

      return null;
   }

   public void removeAdvice(IAopAdvice advice)
   {
      advice.getAspect().removeAdvice(advice);
   }

   public boolean hasAdvice(String fqClassName, String methodName)
   {
      AopAspect aspect = (AopAspect) aspects.get(fqClassName);
      if (aspect != null)
      {
         IMethod adviceMethod = AopCorePlugin.getDefault().findAdviceMethod(aspect.getType(), methodName);
         return aspect.hasAdvice(adviceMethod);
      }

      return false;
   }

   public IAopAdvice addAdvice(String fqClassName, String methodName)
   {
      AopAspect aspect = (AopAspect) aspects.get(fqClassName);
      if (aspect != null)
      {
         IMethod adviceMethod = AopCorePlugin.getDefault().findAdviceMethod(aspect.getType(), methodName);
         return aspect.addAdvice(adviceMethod);
      }

      return null;
   }

   public void removeAdvisor(IAopAdvisor advisor)
   {
      if (advisor.getType() == IAopAdvisor.ADVICE)
      {
         removeAdvice((IAopAdvice) advisor);
      }
      else if (advisor.getType() == IAopAdvisor.INTERCEPTOR)
      {
         removeInterceptor((IAopInterceptor) advisor);
      }
   }

   public IAopAdvisor[] getAllAdvisors()
   {
      ArrayList advisors = new ArrayList();
      IAopAspect aspects[] = getAspects();
      IAopInterceptor interceptors[] = getInterceptors();

      for (int i = 0; i < aspects.length; i++)
      {
         IAopAdvice advice[] = aspects[i].getAdvice();

         for (int j = 0; j < advice.length; j++)
         {
            advisors.add(advice[j]);
         }
      }
      for (int i = 0; i < interceptors.length; i++)
      {
         advisors.add(interceptors[i]);
      }
      return (IAopAdvisor[]) advisors.toArray(new IAopAdvisor[advisors.size()]);
   }

   public IAopAspect[] getAspects()
   {
      return (IAopAspect[]) aspects.values().toArray(new IAopAspect[aspects.size()]);
   }

   public IAopInterceptor[] getInterceptors()
   {
      return (IAopInterceptor[]) interceptors.toArray(new IAopInterceptor[interceptors.size()]);
   }

   /*
    * Again, pushing the limits of what is an advisor, but for the sake
    * of using classes that are already here and not overflowing the product:
    */

   public AopTypedef addTypedef(JDTTypedefExpression expression)
   {
      AopTypedef def = new AopTypedef(expression);
      typedefs.put(expression.getName(), def);
      return def;
   }

   public void removeTypedef(JDTTypedefExpression expression)
   {
      typedefs.remove(expression.getName());
   }

   public AopTypedef[] getTypedefs()
   {
      return (AopTypedef[]) typedefs.values().toArray(new AopTypedef[typedefs.size()]);
   }

   public AopTypedef getTypedef(String name)
   {
      return (AopTypedef) typedefs.get(name);
   }

   public AopInterfaceIntroduction addIntroduction(JDTInterfaceIntroduction intro)
   {
      AopInterfaceIntroduction aopIntro = new AopInterfaceIntroduction(intro);
      introductions.put(intro.getName(), aopIntro);
      return aopIntro;
   }

   public void removeIntroduction(JDTInterfaceIntroduction intro)
   {
      introductions.remove(intro.getName());
   }

   public AopInterfaceIntroduction[] getIntroductions()
   {
      return (AopInterfaceIntroduction[]) introductions.values().toArray(
            new AopInterfaceIntroduction[introductions.size()]);
   }

   public AopInterfaceIntroduction getIntroduction(String name)
   {
      return (AopInterfaceIntroduction) introductions.get(name);
   }

}
