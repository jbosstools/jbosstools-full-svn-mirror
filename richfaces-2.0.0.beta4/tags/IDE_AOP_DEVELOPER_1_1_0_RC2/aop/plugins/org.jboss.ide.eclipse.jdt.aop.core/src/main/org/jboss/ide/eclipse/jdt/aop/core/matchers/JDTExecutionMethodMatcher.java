/*
 * Created on Jan 18, 2005
 */
package org.jboss.ide.eclipse.jdt.aop.core.matchers;

import org.eclipse.jdt.core.IMethod;
import org.jboss.aop.pointcut.Pointcut;
import org.jboss.aop.pointcut.ast.ASTConstructor;
import org.jboss.aop.pointcut.ast.ASTExecution;
import org.jboss.aop.pointcut.ast.ASTField;
import org.jboss.aop.pointcut.ast.ASTHas;
import org.jboss.aop.pointcut.ast.ASTHasField;
import org.jboss.aop.pointcut.ast.ASTMethod;
import org.jboss.aop.pointcut.ast.ASTStart;
import org.jboss.aop.pointcut.ast.Node;
import org.jboss.aop.pointcut.ast.ParseException;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTPointcutExpression;

/**
 * @author Marshall
 */
public class JDTExecutionMethodMatcher extends JDTMethodMatcher {

	public JDTExecutionMethodMatcher(IMethod method, ASTStart start) {
		super(method, start);
	}
	
	public Object visit(ASTExecution node, Object data) {
		return node.jjtGetChild(0).jjtAccept(this, data);
	}

	protected Boolean resolvePointcut(Pointcut p) {
		
		try {
			JDTPointcutExpression expr = new JDTPointcutExpression(p);
			
			return new Boolean(expr.matchesExecution(jdtMethod));
		} catch (ParseException e) {
			throw new RuntimeException (e);
		}
	}
	
	public Object visit(ASTHas node, Object data) {
	
		Node n = node.jjtGetChild(0);
		if (n instanceof ASTMethod)
		{
			return new Boolean(JDTPointcutUtil.has(jdtMethod.getDeclaringType(), (ASTMethod) n));
		}
		else
		{
			return new Boolean(JDTPointcutUtil.has(jdtMethod.getDeclaringType(), (ASTConstructor) n));
		}
	}
	
	public Object visit(ASTHasField node, Object data) {
		ASTField f = (ASTField) node.jjtGetChild(0);
		
		return new Boolean(JDTPointcutUtil.has(jdtMethod.getDeclaringType(), f));
	}
}
