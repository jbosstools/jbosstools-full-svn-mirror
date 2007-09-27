/*
 * Created on Jan 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.jdt.aop.core.matchers;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javassist.NotFoundException;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.jboss.aop.AspectManager;
import org.jboss.aop.pointcut.MatcherHelper;
import org.jboss.aop.pointcut.Pointcut;
import org.jboss.aop.pointcut.ast.ASTAll;
import org.jboss.aop.pointcut.ast.ASTAttribute;
import org.jboss.aop.pointcut.ast.ASTConstructor;
import org.jboss.aop.pointcut.ast.ASTParameter;
import org.jboss.aop.pointcut.ast.ASTStart;
import org.jboss.aop.pointcut.ast.ClassExpression;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.matchers.JDTMethodMatcher.TempBool;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JDTConstructorMatcher extends MatcherHelper {

	protected IMethod constructor;
	
	public JDTConstructorMatcher (IMethod constructor, ASTStart start) throws NotFoundException
	{
		super (start, AspectManager.instance());
		this.start = start;
		this.constructor = constructor;
	}
	
	protected Boolean resolvePointcut(Pointcut p) {
		throw new RuntimeException("SHOULD NOT BE CALLED");
	}

	public Object visit(ASTConstructor node, Object data) {
		return matches(node);
	}
	
	public Boolean matches (ASTConstructor node)
	{
		try {
			if (node.getAttributes().size() > 0)
			{
				for (int i = 0; i < node.getAttributes().size(); i++)
				{
					ASTAttribute attr = (ASTAttribute) node.getAttributes().get(i);
					if (! JDTPointcutUtil.matchModifiers(attr, constructor.getFlags())) return Boolean.FALSE;
				}
			}
				
			if (! JDTPointcutUtil.matchesClassExpr(node.getClazz(), constructor.getDeclaringType())) return Boolean.FALSE;
			
			if (node.getConstructorAnnotation() !=  null)
			{
				if( AopCorePlugin.getDefault().hasJava50CompilerCompliance(constructor.getJavaProject())) {
				    ASTParser c = ASTParser.newParser(AST.JLS3);
				    c.setSource(constructor.getCompilationUnit().getSource().toCharArray());
				    c.setResolveBindings(true);
			        CompilationUnit beanAstUnit = (CompilationUnit) c.createAST(null);
			        AST ast = beanAstUnit.getAST();
			        
			        final String targetAnnot = node.getConstructorAnnotation().getOriginal().substring(1);
			        final String targetMethodName = constructor.getElementName();
			        
			        final TempBool tempBool = new TempBool(false);
			        

			        beanAstUnit.accept(new ASTVisitor () {
			            public boolean visit(MarkerAnnotation node) {
			                Name name = node.getTypeName();
			                String annotationName = name.getFullyQualifiedName();
			                if( annotationName.equals(targetAnnot)) {
			                	tempBool.setValue(true);
			                } else {
			                }
			                return true;
			            }
			            
			            public boolean visit(FieldDeclaration node ) {
			            	return false;
			            }

			            public boolean visit(MethodDeclaration node) {
			            	if( node.getName().getFullyQualifiedName().equals(targetMethodName)) {
			            		return true;
			            	} else {
			            		return false;
			            	}
			            }
			        });
			        return tempBool.getBool();
				} else {

					JavaMethod qDoxMethod = QDoxMatcher.matchMethod(constructor, QDoxMatcher.CONSTRUCTOR);
					// qDoxMethod will be null if there's an exception, such as a compile exception.
					//System.out.println(qDoxMethod);
					if( qDoxMethod == null ) return Boolean.FALSE;
					DocletTag[] tags = qDoxMethod.getTags();
					//System.out.println("node: " + node.getConstructorAnnotation().getOriginal());
					for( int k = 0; k < tags.length; k++ ) {
						//System.out.println("   tags: " + tags[k].getName() );
						if( node.getConstructorAnnotation().getOriginal().equals( 
								tags[k].getName()))
							return Boolean.TRUE;
					}
			        return Boolean.FALSE;
				}
			}
			
			ArrayList nodeExceptions = node.getExceptions();
			if (! JDTPointcutUtil.matchExceptions(nodeExceptions, constructor, constructor.getExceptionTypes())) return Boolean.FALSE;
			
			if (node.isAnyParameters()) return Boolean.TRUE;
			if (node.getParameters().size() != constructor.getParameterNames().length) return Boolean.FALSE;
			
			String jdtMethodParameterTypes[] = constructor.getParameterTypes();
			
			String paramTypeNames[] = new String[jdtMethodParameterTypes.length];
			System.arraycopy(jdtMethodParameterTypes, 0, paramTypeNames, 0, jdtMethodParameterTypes.length);
			IType paramTypes[] = new IType[paramTypeNames.length];
			
			for (int i = 0; i < paramTypeNames.length; i++)
			{
				String fqParamTypeName = JavaModelUtil.getResolvedTypeName(paramTypeNames[i], constructor.getDeclaringType());
				IType paramType = null;
				try {
					paramType = JavaModelUtil.findType(constructor.getJavaProject(), fqParamTypeName);
				} catch( JavaModelException jme ) {
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
			JavaMethod qDoxMethod = QDoxMatcher.matchMethod(constructor, QDoxMatcher.CONSTRUCTOR);
			// qDoxMethod will be null if there's an exception, such as a compile exception.
			if( qDoxMethod == null ) return Boolean.FALSE;

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
			if (! JDTPointcutUtil.subtypeOf(constructor.getDeclaringType(), node.getClazz())) return Boolean.FALSE;
		}
		else if (node.getClazz().isTypedef())
		{
			if (! JDTPointcutUtil.matchesTypedef(constructor.getDeclaringType(), node.getClazz())) return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	
}
