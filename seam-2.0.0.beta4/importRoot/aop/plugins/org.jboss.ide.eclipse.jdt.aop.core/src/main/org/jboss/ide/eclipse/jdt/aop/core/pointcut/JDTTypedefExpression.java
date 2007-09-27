/*
 * Created on Jan 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.jdt.aop.core.pointcut;

import org.eclipse.jdt.core.IType;
import org.jboss.aop.pointcut.Typedef;
import org.jboss.aop.pointcut.TypedefExpression;
import org.jboss.aop.pointcut.ast.ParseException;
import org.jboss.ide.eclipse.jdt.aop.core.matchers.JDTTypeMatcher;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JDTTypedefExpression extends TypedefExpression {

	
	public JDTTypedefExpression (Typedef typedef)
		throws ParseException
	{
		super(typedef.getName(), typedef.getExpr());
		
	}
	
	public boolean matches (IType type)
	{
		JDTTypeMatcher matcher = new JDTTypeMatcher(type);
		return ((Boolean) ast.jjtAccept(matcher, null)).booleanValue();
	}
}
