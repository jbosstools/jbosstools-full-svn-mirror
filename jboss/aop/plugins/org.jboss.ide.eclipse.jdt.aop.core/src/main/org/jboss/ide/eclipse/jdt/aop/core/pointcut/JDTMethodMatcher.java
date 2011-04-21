/*
 * Created on Jan 10, 2005
 */
package org.jboss.ide.eclipse.jdt.aop.core.pointcut;

import java.util.ArrayList;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.jboss.aop.AspectManager;
import org.jboss.aop.pointcut.MatcherHelper;
import org.jboss.aop.pointcut.Pointcut;
import org.jboss.aop.pointcut.ast.ASTAll;
import org.jboss.aop.pointcut.ast.ASTAttribute;
import org.jboss.aop.pointcut.ast.ASTMethod;
import org.jboss.aop.pointcut.ast.ASTParameter;
import org.jboss.aop.pointcut.ast.ASTStart;
import org.jboss.aop.pointcut.ast.ClassExpression;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaMethod;

/**
 * @author Marshall
 */
public class JDTMethodMatcher extends MatcherHelper {

	protected ASTStart start;
	protected IMethod jdtMethod;
	
	public JDTMethodMatcher (IMethod method, ASTStart start)
	{
		super(start, AspectManager.instance());
		
		this.start = start;
		this.jdtMethod = method;
	}
	
	protected Boolean resolvePointcut(Pointcut p) {
		throw new RuntimeException ("SHOULD NOT BE CALLED");
	}
	
	public Object visit (ASTMethod node, Object data)
	{
		return matches(node);
	}

	public Boolean matches (ASTMethod node)
	{
		try {
			if (node.getAttributes().size() > 0)
			{
				for (int i = 0; i < node.getAttributes().size(); i++)
				{
					ASTAttribute attr = (ASTAttribute) node.getAttributes().get(i);
					if (! JDTPointcutUtil.matchModifiers(attr, jdtMethod.getFlags()))
						return Boolean.FALSE;
				}
			}
			
			String fqReturnType = JavaModelUtil.getResolvedTypeName(jdtMethod.getReturnType(), jdtMethod.getDeclaringType());
			IType returnType = null;
			try {
				returnType = JavaModelUtil.findType(jdtMethod.getJavaProject(), fqReturnType);
			} catch( JavaModelException jme ) {
			}
			if (returnType == null)
			{
				// Either this is a primitive type, or it really wasn't found. Check for primitive and return false if it doesn't match
				if (!JDTPointcutUtil.matchesClassExprPrimitive(node.getReturnType(), fqReturnType)) return Boolean.FALSE;
			}
			else {
				if (! JDTPointcutUtil.matchesClassExpr(node.getReturnType(), returnType)) return Boolean.FALSE;
			}
			
			if (! JDTPointcutUtil.matchesClassExpr(node.getClazz(), jdtMethod.getDeclaringType())) return Boolean.FALSE;
			
			if (node.getMethodIdentifier().isAnnotation())
			{
				JavaMethod qDoxMethod = QDoxMatcher.matchMethod(jdtMethod, QDoxMatcher.METHOD);
				if (qDoxMethod == null) return Boolean.FALSE;
				DocletTag[] tags = qDoxMethod.getTags();
				for( int k = 0; k < tags.length; k++ ) {
					if( node.getMethodIdentifier().getOriginal()
							.equals(tags[k].getName()))
							return Boolean.TRUE;        						
				}
		        return Boolean.FALSE;
			}
			else
			{
				if (!node.getMethodIdentifier().matches(jdtMethod.getElementName())) return Boolean.FALSE;
			}
			
			ArrayList nodeExceptions = node.getExceptions();
			if (!JDTPointcutUtil.matchExceptions(nodeExceptions, jdtMethod, jdtMethod.getExceptionTypes()))
			{
				return Boolean.FALSE;
			}
			
			if (node.isAnyParameters()) return Boolean.TRUE;
			
			if (node.getParameters().size() != jdtMethod.getParameterNames().length) return Boolean.FALSE;
			
			String jdtMethodParameterTypes[] = jdtMethod.getParameterTypes();
			
			String paramTypeNames[] = new String[jdtMethodParameterTypes.length];
			System.arraycopy(jdtMethodParameterTypes, 0, paramTypeNames, 0, jdtMethodParameterTypes.length);
			
			IType paramTypes[] = new IType[paramTypeNames.length];
			
			for (int i = 0; i < paramTypeNames.length; i++)
			{
				String fqParamTypeName = JavaModelUtil.getResolvedTypeName(paramTypeNames[i], jdtMethod.getDeclaringType());
				IType paramType = null;
				try {
					paramType = JavaModelUtil.findType(jdtMethod.getJavaProject(), fqParamTypeName);
				} catch( JavaModelException jme) {
					
				} finally {
					if (paramType == null) // Probably a primitive 
						paramTypeNames[i] = fqParamTypeName;
				}
				
				
				paramTypes[i] = paramType;
			}
			
			for (int i = 0; i < paramTypes.length; i++)
			{
				ASTParameter ast = (ASTParameter) node.getParameters().get(i);
				ClassExpression expr = ast.getType();
				if (paramTypes[i] == null)
				{
					if (!JDTPointcutUtil.matchesClassExprPrimitive(ast.getType(), paramTypeNames[i])) 
						return Boolean.FALSE;
				}
				else {
					if (! JDTPointcutUtil.matchesClassExpr(expr, paramTypes[i])) return Boolean.FALSE;
				}
			}
			
		} catch (JavaModelException e) {
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	public Object visit(ASTAll node, Object data) {
		if (node.getClazz().isAnnotation())
		{
			JavaMethod qDoxMethod = QDoxMatcher.matchMethod(jdtMethod, QDoxMatcher.METHOD);
			if (qDoxMethod == null) return Boolean.FALSE;
			DocletTag[] tags = qDoxMethod.getTags();
			for( int k = 0; k < tags.length; k++ ) {
				if( node.getClasseExpression().equals(tags[k].getName())) {
					//System.out.println("match on " + node.getClasseExpression());
					return Boolean.TRUE;
				}
			}
			return Boolean.FALSE;
		}
		else if (node.getClazz().isInstanceOf())
		{
			if (! JDTPointcutUtil.subtypeOf(jdtMethod.getDeclaringType(), node.getClazz())) return Boolean.FALSE;
		}
		else if (node.getClazz().isTypedef())
		{
			if (! JDTPointcutUtil.matchesTypedef(jdtMethod.getDeclaringType(), node.getClazz())) return Boolean.FALSE;
		}
		else if (! node.getClazz().matches(jdtMethod.getDeclaringType().getFullyQualifiedName()))
		{
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	
}
