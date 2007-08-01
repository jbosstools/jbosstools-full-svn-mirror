/*
 * Created on Sep 22, 2004
 */
package org.jboss.ide.eclipse.jdt.aop.core.model;

import org.eclipse.jdt.core.IType;

/**
 * @author Marshall
 */
public interface IAopInterceptor extends IAopAdvisor {

	/**
	 * @return The JDT IType representing this Interceptor
	 */
	public IType getAdvisingType();
	
	/**
	 * @return The name of this Interceptor (for Interceptor-Refs)
	 */
	public String getName();
}
