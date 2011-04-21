/*
 * Created on Jan 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.jdt.aop.core.pointcut;

import org.eclipse.jdt.core.IType;
import org.jboss.aop.pointcut.TypeMatcher;
import org.jboss.aop.pointcut.ast.ASTAllParameter;
import org.jboss.aop.pointcut.ast.ASTAnd;
import org.jboss.aop.pointcut.ast.ASTAttribute;
import org.jboss.aop.pointcut.ast.ASTBoolean;
import org.jboss.aop.pointcut.ast.ASTClass;
import org.jboss.aop.pointcut.ast.ASTComposite;
import org.jboss.aop.pointcut.ast.ASTConstructor;
import org.jboss.aop.pointcut.ast.ASTException;
import org.jboss.aop.pointcut.ast.ASTField;
import org.jboss.aop.pointcut.ast.ASTHas;
import org.jboss.aop.pointcut.ast.ASTHasField;
import org.jboss.aop.pointcut.ast.ASTMethod;
import org.jboss.aop.pointcut.ast.ASTNot;
import org.jboss.aop.pointcut.ast.ASTOr;
import org.jboss.aop.pointcut.ast.ASTParameter;
import org.jboss.aop.pointcut.ast.ASTStart;
import org.jboss.aop.pointcut.ast.ASTSub;
import org.jboss.aop.pointcut.ast.Node;
import org.jboss.aop.pointcut.ast.SimpleNode;
import org.jboss.aop.pointcut.ast.TypeExpressionParserVisitor;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JDTTypeMatcher implements TypeExpressionParserVisitor {

	private TypeMatcher matcherDelegate;
	private IType type;
	
	public JDTTypeMatcher (IType type)
	{
		matcherDelegate = new TypeMatcher(null, (Class) null);
		this.type = type;
	}
	
	public Object visit(ASTClass node, Object data) {
		return new Boolean(JDTPointcutUtil.matchesClassExpr(node.getClazz(), type));
	}
	
	public Object visit(ASTHas node, Object data) {
		Node n = node.jjtGetChild(0);
		if (n instanceof ASTMethod)
		{
			return new Boolean(JDTPointcutUtil.has(type, (ASTMethod) n));
		}
		else
		{
			return new Boolean(JDTPointcutUtil.has(type, (ASTConstructor) n));
		}
	}
	
	public Object visit(ASTHasField node, Object data) {
		ASTField f = (ASTField) node.jjtGetChild(0);
		return new Boolean(JDTPointcutUtil.has(type, f));
	}
	
	/**
	 * @param node
	 * @param data
	 * @return
	 */
	public Object visit(ASTAllParameter node, Object data) {
		return matcherDelegate.visit(node, data);
	}
	/**
	 * @param node
	 * @param data
	 * @return
	 */
	public Object visit(ASTAttribute node, Object data) {
		return matcherDelegate.visit(node, data);
	}
	/**
	 * @param node
	 * @param data
	 * @return
	 */
	public Object visit(ASTConstructor node, Object data) {
		return matcherDelegate.visit(node, data);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public Object visit(ASTException arg0, Object arg1) {
		return matcherDelegate.visit(arg0, arg1);
	}
	/**
	 * @param node
	 * @param data
	 * @return
	 */
	public Object visit(ASTField node, Object data) {
		return matcherDelegate.visit(node, data);
	}
	/**
	 * @param node
	 * @param data
	 * @return
	 */
	public Object visit(ASTMethod node, Object data) {
		return matcherDelegate.visit(node, data);
	}
	/**
	 * @param node
	 * @param data
	 * @return
	 */
	public Object visit(ASTParameter node, Object data) {
		return matcherDelegate.visit(node, data);
	}
	/**
	 * @param node
	 * @param data
	 * @return
	 */
	public Object visit(ASTAnd node, Object data) {
		return matcherDelegate.visit(node, data);
	}
	/**
	 * @param node
	 * @param data
	 * @return
	 */
	public Object visit(ASTBoolean node, Object data) {
		return matcherDelegate.visit(node, data);
	}
	/**
	 * @param node
	 * @param data
	 * @return
	 */
	public Object visit(ASTComposite node, Object data) {
		return matcherDelegate.visit(node, data);
	}
	/**
	 * @param node
	 * @param data
	 * @return
	 */
	public Object visit(ASTNot node, Object data) {
		return matcherDelegate.visit(node, data);
	}
	/**
	 * @param node
	 * @param data
	 * @return
	 */
	public Object visit(ASTOr node, Object data) {
		return matcherDelegate.visit(node, data);
	}
	/**
	 * @param node
	 * @param data
	 * @return
	 */
	public Object visit(ASTStart node, Object data) {
		return matcherDelegate.visit(node, data);
	}
	/**
	 * @param node
	 * @param data
	 * @return
	 */
	public Object visit(ASTSub node, Object data) {
		return matcherDelegate.visit(node, data);
	}
	/**
	 * @param node
	 * @param data
	 * @return
	 */
	public Object visit(SimpleNode node, Object data) {
		return matcherDelegate.visit(node, data);
	}
}
