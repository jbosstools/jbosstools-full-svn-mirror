/*
 * Created on Jan 18, 2005
 */
package org.jboss.ide.eclipse.jdt.aop.core.pointcut;

import javassist.NotFoundException;

import org.eclipse.jdt.core.IField;
import org.jboss.aop.pointcut.Pointcut;
import org.jboss.aop.pointcut.ast.ASTSet;
import org.jboss.aop.pointcut.ast.ASTStart;
import org.jboss.aop.pointcut.ast.ParseException;

/**
 * @author Marshall
 */
public class JDTFieldSetMatcher extends JDTFieldMatcher {

	public JDTFieldSetMatcher(IField field, ASTStart start)
			throws NotFoundException
	{
		super (field, start);
		this.jdtField = field;
	}
	
	
	public Object visit(ASTSet node, Object data) {
		return node.jjtGetChild(0).jjtAccept(this, null);
	}
	
	protected Boolean resolvePointcut(Pointcut p) {
		try {
			JDTPointcutExpression expr = new JDTPointcutExpression(p);
			return new Boolean (expr.matchesSet(jdtField));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
