package org.jboss.ide.eclipse.jdt.aop.core.model.internal;

import org.eclipse.jdt.core.IJavaElement;
import org.jboss.aop.pointcut.TypedefExpression;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTPointcutExpression;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTTypedefExpression;

/**
 * While this class pushes the limits for an advisor, 
 * I think it's a useful categorization as some type
 * definition references (or advises) some IType.
 * 
 * 
 * @author Rob Stryker
 *
 */
public class AopTypedef extends AopAdvisor {
	
	private JDTTypedefExpression tdExpr;
	
	public AopTypedef(JDTTypedefExpression expr) {
		super(null);
		this.tdExpr = expr;
	}

	
	public int getType() {
		return TYPEDEF;
	}
	
	public TypedefExpression getTypedef() {
		return tdExpr;
	}
	
	
	
	/*
	 * These are methods from the superclass that 
	 * don't apply to a typedef. Sadly, I feel like
	 * I'm breaking the model. 
	 */
	public IJavaElement getAdvisingElement() {
		return null;
	}

	public Object getAdapter(Class adapter)	{
		return null;
	}


	public JDTPointcutExpression[] getAssignedPointcuts () {
		return null;
	}
	
	public void assignPointcut(JDTPointcutExpression expression) {
	}
	
	public void unassignPointcut(JDTPointcutExpression expression) {
	}


}
