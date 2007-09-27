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
package org.jboss.ide.eclipse.jdt.aop.core.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;

/**
 * @author Marshall
 */
public class AspectValidator
{

   public static final String METHOD_PATTERN = "[^\\(]+\\(.*?(Method.*?)?Invocation\\) Object";

   public static final String FIELD_PATTERN = "[^\\(]+\\(.*?(Field.*?)?Invocation\\) Object";

   public static final String CONSTRUCTOR_PATTERN = "[^\\(]+\\(.*?(Constructor.*?)?Invocation))\\) Object";

   public static boolean validateType(IType type)
   {
      try
      {

         String[] impls = type.getSuperInterfaceTypeSignatures();

         for (int i = 0; i < impls.length; i++)
         {
            if (impls[i].equals("org.jboss.aop.advice.Interceptor"))
            {
               return false;
            }
         }

         return true;
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return false;
      }
   }

   public static boolean validateMethod(IMethod method)
   {
      return validateType(method.getDeclaringType());
   }

   public static boolean validateMethodAdvice(IMethod advice)
   {
      return returnsObject(advice) && hasInvocationArg(advice, "Method");
   }

   public static boolean validateFieldAdvice(IMethod advice)
   {
      return returnsObject(advice) && hasInvocationArg(advice, "Field");
   }

   public static boolean validateMethodCalledByMethodAdvice(IMethod advice)
   {
      return returnsObject(advice) && hasInvocationArg(advice, "MethodCalledByMethod");
   }

   public static boolean validateConstructorAdvice(IMethod advice)
   {
      try
      {
         if (advice.isConstructor())
         {
            return returnsObject(advice) && hasInvocationArg(advice, "Constructor");
         }
      }
      catch (JavaModelException e)
      {
         e.printStackTrace();
      }

      return false;
   }

   private static boolean returnsObject(IMethod advice)
   {
      try
      {
         String returnType = JavaModelUtil.getResolvedTypeName(advice.getReturnType(), advice.getDeclaringType());
         return match(returnType, "java.lang", "Object");
      }
      catch (CoreException e)
      {
         e.printStackTrace();
      }
      return false;
   }

   private static boolean hasInvocationArg(IMethod advice, String prefix)
   {
      try
      {
         String paramTypes[] = advice.getParameterTypes();
         String firstParamType = JavaModelUtil.getResolvedTypeName(paramTypes[0], advice.getDeclaringType());
         return (paramTypes.length == 1 && ((match(firstParamType, "org.jboss.aop.joinpoint", prefix) && firstParamType
               .endsWith("Invocation")) || (match(firstParamType, "org.jboss.aop.joinpoint", "Invocation"))));
      }
      catch (JavaModelException e)
      {
         e.printStackTrace();
      }
      return false;
   }

   private static boolean match(String name, String pkg, String className)
   {
      return (name.startsWith(className) || name.startsWith(pkg + "." + className));
   }
}
