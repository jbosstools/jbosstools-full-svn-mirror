/*
 * Created on Jan 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.jdt.aop.core.pointcut;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;

import javassist.NotFoundException;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.jboss.aop.AspectManager;
import org.jboss.aop.pointcut.Typedef;
import org.jboss.aop.pointcut.ast.ASTAttribute;
import org.jboss.aop.pointcut.ast.ASTConstructor;
import org.jboss.aop.pointcut.ast.ASTException;
import org.jboss.aop.pointcut.ast.ASTField;
import org.jboss.aop.pointcut.ast.ASTMethod;
import org.jboss.aop.pointcut.ast.ClassExpression;
import org.jboss.aop.pointcut.ast.ParseException;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JDTPointcutUtil {

	public static boolean matchModifiers (ASTAttribute need, int have)
	{
	      if (Modifier.isAbstract(need.attribute) && need.not) return !org.eclipse.jdt.core.dom.Modifier.isAbstract(have);
	      if (Modifier.isAbstract(need.attribute) && !need.not) return org.eclipse.jdt.core.dom.Modifier.isAbstract(have);
	      if (Modifier.isFinal(need.attribute) && need.not) return !org.eclipse.jdt.core.dom.Modifier.isFinal(have);
	      if (Modifier.isFinal(need.attribute) && !need.not) return org.eclipse.jdt.core.dom.Modifier.isFinal(have);
//	      if (Modifier.isInterface(need.attribute) && need.not) return !org.eclipse.jdt.core.dom.Modifier.isInterface(have);
//	      if (Modifier.isInterface(need.attribute) && !need.not) return org.eclipse.jdt.core.dom.Modifier.isInterface(have);
	      if (Modifier.isNative(need.attribute) && need.not) return !org.eclipse.jdt.core.dom.Modifier.isNative(have);
	      if (Modifier.isNative(need.attribute) && !need.not) return org.eclipse.jdt.core.dom.Modifier.isNative(have);
	      if (Modifier.isPrivate(need.attribute) && need.not) return !org.eclipse.jdt.core.dom.Modifier.isPrivate(have);
	      if (Modifier.isPrivate(need.attribute) && !need.not) return org.eclipse.jdt.core.dom.Modifier.isPrivate(have);
	      if (Modifier.isProtected(need.attribute) && need.not) return !org.eclipse.jdt.core.dom.Modifier.isProtected(have);
	      if (Modifier.isProtected(need.attribute) && !need.not) return org.eclipse.jdt.core.dom.Modifier.isProtected(have);
	      if (Modifier.isPublic(need.attribute) && need.not) return !org.eclipse.jdt.core.dom.Modifier.isPublic(have);
	      if (Modifier.isPublic(need.attribute) && !need.not) return org.eclipse.jdt.core.dom.Modifier.isPublic(have);
	      if (Modifier.isStatic(need.attribute) && need.not) return !org.eclipse.jdt.core.dom.Modifier.isStatic(have);
	      if (Modifier.isStatic(need.attribute) && !need.not) return org.eclipse.jdt.core.dom.Modifier.isStatic(have);
//	      if (Modifier.isStrict(need.attribute) && need.not) return !org.eclipse.jdt.core.dom.Modifier.isStrict(have);
//	      if (Modifier.isStrict(need.attribute) && !need.not) return org.eclipse.jdt.core.dom.Modifier.isStrict(have);
	      if (Modifier.isSynchronized(need.attribute) && need.not) return !org.eclipse.jdt.core.dom.Modifier.isSynchronized(have);
	      if (Modifier.isSynchronized(need.attribute) && !need.not) return org.eclipse.jdt.core.dom.Modifier.isSynchronized(have);
	      if (Modifier.isTransient(need.attribute) && need.not) return !org.eclipse.jdt.core.dom.Modifier.isTransient(have);
	      if (Modifier.isTransient(need.attribute) && !need.not) return org.eclipse.jdt.core.dom.Modifier.isTransient(have);
	      if (Modifier.isVolatile(need.attribute) && need.not) return !org.eclipse.jdt.core.dom.Modifier.isVolatile(have);
	      if (Modifier.isVolatile(need.attribute) && !need.not) return org.eclipse.jdt.core.dom.Modifier.isVolatile(have);
	      
	      return true;
	}
	
	public static boolean matchesClassExprPrimitive (ClassExpression classExpr, String primitiveType)
	{
		try {
			
			if (classExpr.isSimple())
			{
				return classExpr.matches(primitiveType);
			}
			else return false;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static boolean matchesClassExpr (ClassExpression classExpr, IType type)
	{
		try
		{
			if (classExpr.isAnnotation())
			{
				return false;
				// We'll need to hook in a concrete data type for JDK1.5 annotations here
			}
			else if (classExpr.isInstanceOf())
			{
				return JDTPointcutUtil.subtypeOf(type, classExpr);
			}
			else if (classExpr.isTypedef())
			{
				return JDTPointcutUtil.matchesTypedef(type, classExpr);
			}
			else 
			{
				return (classExpr.matches(type.getFullyQualifiedName()));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static boolean subtypeOf (IType type, ClassExpression instanceOf)
	{
		if (type == null) return false;
		if (instanceOf.isInstanceOfAnnotated())
		{
			// Plugin annotation code at a later time..
			return false;
		}
		else if (instanceOf.matches(type.getFullyQualifiedName()))
		{
			return true;
		}
		
		try {
			IType superTypes[] = JavaModelUtil.getAllSuperTypes(type, new NullProgressMonitor());
			
			for (int i = 0; i < superTypes.length; i++)
			{
				if (subtypeOf(superTypes[i], instanceOf)) return true;
			}
			if (type.isInterface()) return false;
			
			return false;
		} catch (JavaModelException e) {
			throw new RuntimeException (e);
		}
	}
	
	public static boolean matchesTypedef (IType type, ClassExpression classExpr)
	{
		String original = classExpr.getOriginal();
		String typedefName = original.substring("$typedef{".length(), original.lastIndexOf("}"));
		Typedef typedef = AspectManager.instance().getTypedef(typedefName);
		try {
			return new JDTTypedefExpression(typedef).matches(type);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static boolean matchExceptions (ArrayList nodeExceptions, IMethod method, String[] exceptions)
	{
		try {
			if (nodeExceptions.size() > exceptions.length) return false;
			
			for (Iterator it = nodeExceptions.iterator(); it.hasNext(); )
			{
				boolean found = false;
				ASTException ex = (ASTException) it.next();
				for (int i = 0; i < exceptions.length; i++)
				{
					String fqExceptionName = JavaModelUtil.getResolvedTypeName(exceptions[i], method.getDeclaringType());
					if (ex.getType().matches(fqExceptionName))
					{
						found = true;
						break;
					}
				}
				
				if (!found) return false;
			}
			
			return true;
		} catch (JavaModelException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static IType getSuperType (IType type)
	{
		try {
			String superClassName = type.getSuperclassName();
			
			if (superClassName != null)
			{
				String fqSuperClassName = JavaModelUtil.getResolvedTypeName(superClassName, type);
				IType superType = JavaModelUtil.findType(type.getJavaProject(), fqSuperClassName);
				
				return superType;
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static boolean has (IType target, ASTMethod method)
	{
		return has(target, method, true);
	}
	
	public static boolean has (IType target, ASTMethod method, boolean checkSuper)
	{
		try
		{
			IMethod methods[] = target.getMethods();
			
			for (int i = 0; i < methods.length; i++)
			{
				if (!methods[i].isConstructor())
				{
					JDTMethodMatcher matcher = new JDTMethodMatcher(methods[i], null);
					if (matcher.matches(method).booleanValue()) return true;
				}
			}
			
			if (checkSuper)
			{
				IType superType = getSuperType(target);
				if (superType != null)
				{
					return has(superType, method, checkSuper);
				}
			}
		}
		catch (JavaModelException e)
		{
			throw new RuntimeException(e);
		}
		return false;
	}
	
	public static boolean has (IType target, ASTField field)
	{
		return has(target, field, true);
	}

	public static boolean has (IType target, ASTField field, boolean checkSuper)
	{
		try {
			IField fields[] = target.getFields();
			
			for (int i = 0; i < fields.length; i++)
			{
				JDTFieldGetMatcher matcher = new JDTFieldGetMatcher(fields[i], null);
				if (((Boolean) field.jjtAccept(matcher, null)).booleanValue()) return true;
			}
			
			if (checkSuper)
			{
				IType superType = getSuperType(target);
				if (superType != null)
				{
					return has(superType, field, checkSuper);
				}
			}
			
			return false;
		} catch (JavaModelException e) {
			throw new RuntimeException(e);
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public static boolean has (IType target, ASTConstructor constructor)
	{
		try {
			IMethod methods[] = target.getMethods();
			
			for (int i = 0; i < methods.length; i++)
			{
				if (methods[i].isConstructor())
				{
					JDTConstructorMatcher matcher = new JDTConstructorMatcher(methods[i], null);
					if (matcher.matches(constructor).booleanValue()) return true;
				}
					
			}
			return false;
		} catch (JavaModelException e) {
			throw new RuntimeException(e);
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
		
	}
}
