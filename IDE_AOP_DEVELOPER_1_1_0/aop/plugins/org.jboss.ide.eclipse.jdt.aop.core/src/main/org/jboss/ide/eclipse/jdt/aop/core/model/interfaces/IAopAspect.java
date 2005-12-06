/*
 * Created on Jan 11, 2005
 */
package org.jboss.ide.eclipse.jdt.aop.core.model.interfaces;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

/**
 * @author Marshall
 */
public interface IAopAspect {

	/**
	 * @return A list of IAopAdvice for this Aspect
	 */
	public IAopAdvice[] getAdvice();
	
	/**
	 * @param advice
	 * @return Whether or not this aspect has the passed in advice.
	 */
	public boolean hasAdvice (IMethod advice);
	
	/**
	 * Add an advice method to this aspect
	 */
	public IAopAdvice addAdvice (IMethod method);
	
	/**
	 * Remove advice from this aspect
	 */
	public void removeAdvice (IAopAdvice advice);
	
	/**
	 * @return The JDT IType corresponding with this Aspect
	 */
	public IType getType();
}
