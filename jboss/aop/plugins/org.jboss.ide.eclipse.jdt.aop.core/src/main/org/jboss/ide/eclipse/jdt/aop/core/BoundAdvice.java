/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.core;

import org.eclipse.jdt.core.IMethod;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.impl.AdviceImpl;

/**
 * @author Marshall
 */
public class BoundAdvice extends AdviceImpl {

	private IMethod method;
	private BoundPointcut pointcut;
	private boolean checked;
	
	public IMethod getMethod() {
		return method;
	}
	
	public void setMethod(IMethod method) {
		this.method = method;

		setAspect(method.getDeclaringType().getFullyQualifiedName());
		setName(method.getElementName());
	}
	
	public BoundPointcut getPointcut() {
		return pointcut;
	}
	
	public void setPointcut(BoundPointcut pointcut) {
		this.pointcut = pointcut;
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
