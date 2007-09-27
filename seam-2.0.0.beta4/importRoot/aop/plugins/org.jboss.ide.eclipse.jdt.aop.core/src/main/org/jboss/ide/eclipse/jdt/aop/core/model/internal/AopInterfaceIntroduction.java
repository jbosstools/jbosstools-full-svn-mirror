package org.jboss.ide.eclipse.jdt.aop.core.model.internal;

import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopTypeMatcher;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTInterfaceIntroduction;

/**
 * While this class pushes the limits for an advisor, 
 * I think it's a useful categorization as some type
 * definition references (or advises) some IType.
 * 
 * 
 * @author Rob Stryker
 *
 */
public class AopInterfaceIntroduction extends AopTypeMatcher {
	
	private JDTInterfaceIntroduction expr;
	
	public AopInterfaceIntroduction(JDTInterfaceIntroduction expr) {
		super(IAopTypeMatcher.INTRODUCTION);
		this.expr = expr;
	}

	public JDTInterfaceIntroduction getIntroduction() {
		return expr;
	}
	
	public boolean equals (Object other) {
		if( other instanceof AopInterfaceIntroduction ) {
			AopInterfaceIntroduction otherr = (AopInterfaceIntroduction)other;
			return otherr.getIntroduction().equals(getIntroduction());
		} else {
			return false;
		}
	}
	

}
