package org.jboss.ide.eclipse.jdt.aop.core.model.internal;

import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopTypeMatcher;
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
public class AopTypedef extends AopTypeMatcher {
	
	private JDTTypedefExpression tdExpr;
	
	public AopTypedef(JDTTypedefExpression expr) {
		super(IAopTypeMatcher.TYPEDEF);
		this.tdExpr = expr;
	}

	
	public int getType() {
		return IAopTypeMatcher.TYPEDEF;
	}
	
	public JDTTypedefExpression getTypedef() {
		return tdExpr;
	}
	
	public boolean equals (Object other) {
		if( other instanceof AopTypedef ) {
			AopTypedef otherr = (AopTypedef)other;
			return otherr.getTypedef().equals(getTypedef());
		} else {
			return false;
		}
	}
	

}
