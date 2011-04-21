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
package org.jboss.ide.eclipse.jdt.aop.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Binding;

/**
 * @author Marshall
 */
public class BoundPointcut
{
   private Binding binding;

   private IJavaElement element;

   private ArrayList advice;

   private ArrayList interceptors;

   private Hashtable options;

   private String name;

   public static final String FIELD_READ = "fieldread";

   public static final String FIELD_WRITE = "fieldwrite";

   public static final String USE_METHOD_CALLED_BY_METHOD = "usemethodcalledbymethod";

   public static final String USE_METHOD_CALLED_BY_CONSTRUCTOR = "usemethodcalledbyconstructor";

   public static final String USE_CONSTRUCTOR_CALLED_BY_METHOD = "useconstructorcalledbymethod";

   public static final String USE_CONSTRUCTOR_CALLED_BY_CONSTRUCTOR = "useconstructorcalledbyconstructor";

   public static final String METHOD_CALLED_BY_METHOD = "methodcalledbymethod";

   public static final String METHOD_CALLED_BY_CONSTRUCTOR = "methodcalledbyconstructor";

   public static final String CONSTRUCTOR_CALLED_BY_METHOD = "constructorcalledbymethod";

   public static final String CONSTRUCTOR_CALLED_BY_CONSTRUCTOR = "constructorcalledbyconstructor";

   public BoundPointcut(IJavaElement element)
   {
      this.element = element;
      options = new Hashtable();

      if (element instanceof IMethod)
      {
         setOption(USE_METHOD_CALLED_BY_METHOD, Boolean.FALSE);
         setOption(USE_METHOD_CALLED_BY_CONSTRUCTOR, Boolean.FALSE);
      }
      else if (element instanceof IField)
      {
         setOption(FIELD_READ, Boolean.TRUE);
         setOption(FIELD_WRITE, Boolean.TRUE);
      }
   }

   public BoundPointcut(Binding binding)
   {
      this.binding = binding;
      options = new Hashtable();
   }

   public IJavaElement getElement()
   {
      return element;
   }

   public void setElement(IJavaElement element)
   {
      this.element = element;
   }

   public Object getOption(String name)
   {
      return options.get(name);
   }

   public boolean getBooleanOption(String name)
   {
      Boolean b = (Boolean) getOption(name);
      return b.booleanValue();
   }

   public void setOption(String name, Object value)
   {
      options.put(name, value);
   }

   public String toString()
   {
      try
      {
         return getPointcut();
      }
      catch (Exception e)
      {
         return "";
      }
   }

   /**
    * Returns a string version of the pointcut.
    * 
    * @return
    * @throws Exception
    */
   public String getPointcut() throws Exception
   {
      /*
       * Either the element is set, or the binding is set.
       * It will not be the case that both are set.
       * 
       */
      if (element != null)
      {
         if (element.getElementType() == IJavaElement.FIELD)
         {
            boolean get = getBooleanOption(FIELD_READ);
            boolean set = getBooleanOption(FIELD_WRITE);
            String aopSignature = AopCorePlugin.getDefault().getAopSignature(element);

            if (get && !set)
            {
               return "get(" + aopSignature + ")";
            }
            else if (set && !get)
            {
               return "set(" + aopSignature + ")";
            }
            else
            {
               return "field(" + aopSignature + ")";
            }
         }
         else if (element.getElementType() == IJavaElement.METHOD)
         {
            IMethod method = (IMethod) element;

            boolean useCalledByMethod = getBooleanOption(USE_METHOD_CALLED_BY_METHOD);
            String aopSignature = AopCorePlugin.getDefault().getAopSignature(element);
            String pointcut = "";

            if (useCalledByMethod)
            {
               IMethod calledBy = (IMethod) getOption(METHOD_CALLED_BY_METHOD);
               String calledBySignature = AopCorePlugin.getDefault().getAopSignature(calledBy);

               return "call(" + aopSignature + ") and withincode(" + calledBySignature + ")";
            }
            else
            {
               return "execution(" + aopSignature + ")";
            }
         }
      }
      else if (binding != null)
      {
         return binding.getPointcut();
      }

      return null;
   }

   public ArrayList getAdvice()
   {
      return advice;
   }

   public void setAdvice(ArrayList advice)
   {
      this.advice = advice;

      Iterator aIter = advice.iterator();
      while (aIter.hasNext())
      {
         BoundAdvice boundAdvice = (BoundAdvice) aIter.next();
         boundAdvice.setPointcut(this);
      }
   }

   public ArrayList getInterceptors()
   {
      return interceptors;
   }

   public void setInterceptors(ArrayList interceptors)
   {
      this.interceptors = interceptors;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public Binding getBinding()
   {
      return binding;
   }

   public void setBinding(Binding binding)
   {
      this.binding = binding;
   }
}
