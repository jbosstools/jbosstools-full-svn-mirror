/*
 * Created on Sep 22, 2004
 */
package org.jboss.ide.eclipse.jdt.aop.core.model.interfaces;

import org.eclipse.jdt.core.IMethod;

/**
 * @author Marshall
 */
public interface IAopAdvice extends IAopAdvisor {

	/**
	 * @return The JDT IMethod that represents this Advice
	 */
	public IMethod getAdvisingMethod();
	
	/**
	 * Return the aspect that this advice is a part of.
	 * @return
	 */
	public IAopAspect getAspect();
}
