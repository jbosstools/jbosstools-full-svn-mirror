/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
public class AspectValidator {
	
	public static final String METHOD_PATTERN = "[^\\(]+\\(.*?(Method.*?)?Invocation\\) Object";
	public static final String FIELD_PATTERN = "[^\\(]+\\(.*?(Field.*?)?Invocation\\) Object";
	public static final String CONSTRUCTOR_PATTERN = "[^\\(]+\\(.*?(Constructor.*?)?Invocation))\\) Object";
	
	public static boolean validateType (IType type)
	{
		try {
		
			String[] impls = type.getSuperInterfaceTypeSignatures();
		
			for (int i = 0; i < impls.length; i++)
			{
				if (impls[i].equals("org.jboss.aop.advice.Interceptor"))
				{
					return false;
				}
			}
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean validateMethod (IMethod method)
	{
		return validateType(method.getDeclaringType());
	}
	
	public static boolean validateMethodAdvice (IMethod advice)
	{
		return returnsObject(advice) && hasInvocationArg(advice, "Method");
	}
	
	public static boolean validateFieldAdvice (IMethod advice)
	{
		return returnsObject(advice) && hasInvocationArg(advice, "Field");
	}
	
	public static boolean validateMethodCalledByMethodAdvice (IMethod advice)
	{
		return returnsObject(advice) && hasInvocationArg(advice, "MethodCalledByMethod");
	}
	
	public static boolean validateConstructorAdvice (IMethod advice)
	{
		try {
			if (advice.isConstructor())
			{
				return returnsObject(advice) && hasInvocationArg(advice, "Constructor");
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private static boolean returnsObject (IMethod advice)
	{
		try {
			String returnType = JavaModelUtil.getResolvedTypeName(advice.getReturnType(), advice.getDeclaringType());
			return match(returnType, "java.lang", "Object");
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean hasInvocationArg (IMethod advice, String prefix)
	{
		try {
			String paramTypes[] = advice.getParameterTypes();
			String firstParamType = JavaModelUtil.getResolvedTypeName(paramTypes[0], advice.getDeclaringType());
			return (paramTypes.length == 1 &&
				((match(firstParamType, "org.jboss.aop.joinpoint", prefix) && firstParamType.endsWith("Invocation")) ||
				(match(firstParamType, "org.jboss.aop.joinpoint", "Invocation"))));
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean match(String name, String pkg, String className)
	{
		return (name.startsWith(className) || name.startsWith(pkg + "." + className));
	}
}
