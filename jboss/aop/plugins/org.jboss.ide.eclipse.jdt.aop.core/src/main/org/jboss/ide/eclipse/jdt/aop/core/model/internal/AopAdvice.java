/*
 * Created on Oct 19, 2004
 */
package org.jboss.ide.eclipse.jdt.aop.core.model.internal;

import org.eclipse.jdt.core.IMethod;
import org.jboss.ide.eclipse.jdt.aop.core.model.IAopAdvice;
import org.jboss.ide.eclipse.jdt.aop.core.model.IAopAspect;


public class AopAdvice extends AopAdvisor implements IAopAdvice
{
	protected IAopAspect aspect;
	
	public AopAdvice (IAopAspect aspect, IMethod method)
	{
		super (method);
		
		this.aspect = aspect;
	}

	public IMethod getAdvisingMethod ()
	{
		return (IMethod) getAdvisingElement();
	}
	
	public IAopAspect getAspect() {
		return aspect;
	}
	
	public int getType() { return ADVICE; }
}