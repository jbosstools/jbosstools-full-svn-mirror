/*
 * Created on Jan 11, 2005
 */
package org.jboss.ide.eclipse.jdt.aop.core.matchers;

import javassist.NotFoundException;

import org.eclipse.jdt.core.IField;
import org.jboss.aop.pointcut.Pointcut;
import org.jboss.aop.pointcut.ast.ASTGet;
import org.jboss.aop.pointcut.ast.ASTStart;
import org.jboss.aop.pointcut.ast.ParseException;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTPointcutExpression;

/**
 * @author Marshall
 */
public class JDTFieldGetMatcher extends JDTFieldMatcher {
	
	public JDTFieldGetMatcher (IField field, ASTStart start) throws NotFoundException
	{
		super (field, start);
		this.jdtField = field;
	}
	
	public Object visit(ASTGet node, Object data) {
		return node.jjtGetChild(0).jjtAccept(this, null);
	}
	
	protected Boolean resolvePointcut(Pointcut p) {
		try {
			JDTPointcutExpression expr = new JDTPointcutExpression(p);
			return new Boolean (expr.matchesGet(jdtField));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
