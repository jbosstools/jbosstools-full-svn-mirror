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
package org.jboss.ide.eclipse.jdt.aop.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.jboss.aop.introduction.InterfaceIntroduction;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Advice;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aop;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aspect;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Binding;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Interceptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.InterceptorRef;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Introduction;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Pointcut;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Typedef;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTInterfaceIntroduction;
import org.jboss.ide.eclipse.jdt.aop.core.util.JaxbAopUtil;

/**
 * 
 * @author Rob Stryker
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AopModelUtils
{

   /**
    * Private method that returns all children of aop of the 
    * specified type.
    * @param clazz  The class type
    * @param aop The aop object
    * @return A list of matching nodes
    */

   private static List getTypeFromAop(Class clazz, Aop aop)
   {
      Iterator i = aop.getTopLevelElements().iterator();
      ArrayList list = new ArrayList();
      while (i.hasNext())
      {
         Object o = i.next();
         if (clazz.isAssignableFrom(o.getClass()))
         {
            list.add(o);
         }
      }
      return list;
   }

   public static List getPointcutsFromAop(Aop aop)
   {
      return getTypeFromAop(Pointcut.class, aop);
   }

   public static List getAspectsFromAop(Aop aop)
   {
      return getTypeFromAop(Aspect.class, aop);
   }

   public static List getInterceptorsFromAop(Aop aop)
   {
      return getTypeFromAop(Interceptor.class, aop);
   }

   public static List getBindingsFromAop(Aop aop)
   {
      return getTypeFromAop(Binding.class, aop);
   }

   public static List getTypedefsFromAop(Aop aop)
   {
      return getTypeFromAop(Typedef.class, aop);
   }

   public static List getIntroductionsFromAop(Aop aop)
   {
      return getTypeFromAop(Introduction.class, aop);
   }

   /**
    * Private method that returns all children of a binding 
    * of the specified type.
    * 
    * @param clazz The class type
    * @param binding The binding object
    * @return A list of matching nodes
    */
   private static List getFromBinding(Class clazz, Binding binding)
   {
      Iterator i = binding.getElements().iterator();
      ArrayList list = new ArrayList();
      while (i.hasNext())
      {
         Object o = i.next();
         if (clazz.isAssignableFrom(o.getClass()))
         {
            list.add(o);
         }
      }
      return list;
   }

   public static List getInterceptorsFromBinding(Binding binding)
   {
      return getFromBinding(Interceptor.class, binding);
   }

   public static List getInterceptorRefssFromBinding(Binding binding)
   {
      return getFromBinding(InterceptorRef.class, binding);
   }

   public static List getAdvicesFromBinding(Binding binding)
   {
      return getFromBinding(Advice.class, binding);
   }

   public static JDTInterfaceIntroduction toJDT(org.jboss.ide.eclipse.jdt.aop.core.jaxb.Introduction jaxbIntro)
   {

      JDTInterfaceIntroduction jdtIntro = new JDTInterfaceIntroduction();

      // make a copy of the provided jaxb-introduction into the aop's jdt type.
      String expr = jaxbIntro.getClazz() == null ? jaxbIntro.getExpr() : jaxbIntro.getClazz();
      boolean type = jaxbIntro.getClazz() == null
            ? JDTInterfaceIntroduction.TYPE_EXPR
            : JDTInterfaceIntroduction.TYPE_CLASS;

      jdtIntro.setClassExpression(expr, type);

      jdtIntro.setInterfaces(jaxbIntro.getInterfaces());
      ArrayList introductionMixins = jdtIntro.getMixins();
      java.util.List jaxbMixins = jaxbIntro.getMixin();
      for (Iterator i = jaxbMixins.iterator(); i.hasNext();)
      {
         org.jboss.ide.eclipse.jdt.aop.core.jaxb.Mixin jaxbMix = (org.jboss.ide.eclipse.jdt.aop.core.jaxb.Mixin) i
               .next();
         String tempInterfaces = jaxbMix.getInterfaces();
         String[] tempInterfacesArray = tempInterfaces == null ? new String[]
         {} : tempInterfaces.split(", ");
         InterfaceIntroduction.Mixin tempMixin = new InterfaceIntroduction.Mixin(jaxbMix.getClazz(),
               tempInterfacesArray, jaxbMix.getConstruction(), jaxbMix.isTransient());
         introductionMixins.add(tempMixin);
      }

      return jdtIntro;
   }

   public static org.jboss.ide.eclipse.jdt.aop.core.jaxb.Introduction toJaxb(JDTInterfaceIntroduction intro)
   {

      org.jboss.ide.eclipse.jdt.aop.core.jaxb.Introduction jaxbIntro = null;
      try
      {
         jaxbIntro = JaxbAopUtil.instance().getFactory().createAOPTypeIntroduction();

         List jaxbMixins = jaxbIntro.getMixin();

         // handle this part
         String expr = intro.getExpr();
         if (expr.indexOf('(') == -1)
         {
            jaxbIntro.setClazz(expr);
         }
         else
         {
            jaxbIntro.setExpr(expr);
         }

         if (intro.getInterfacesAsString() != "")
            jaxbIntro.setInterfaces(intro.getInterfacesAsString());

         // Now this part
         ArrayList jdtList = intro.getMixins();
         for (Iterator i = jdtList.iterator(); i.hasNext();)
         {
            Object o = i.next();
            if (o instanceof InterfaceIntroduction.Mixin)
            {
               InterfaceIntroduction.Mixin mixin = (InterfaceIntroduction.Mixin) o;

               org.jboss.ide.eclipse.jdt.aop.core.jaxb.Mixin jaxbMixin = JaxbAopUtil.instance().getFactory()
                     .createMixin();

               jaxbMixin.setClazz(mixin.getClassName());
               jaxbMixin.setConstruction(mixin.getConstruction());
               jaxbMixin.setInterfaces(intro.getMixinInterfacesAsString(mixin));
               jaxbMixins.add(jaxbMixin);
            }
         }
      }
      catch (JAXBException e)
      {
         e.printStackTrace();
      }

      return jaxbIntro != null ? jaxbIntro : null;
   }

}
