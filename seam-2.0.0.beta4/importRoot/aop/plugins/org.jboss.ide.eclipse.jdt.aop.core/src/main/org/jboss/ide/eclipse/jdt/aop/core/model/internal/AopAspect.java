/*
 * Created on Jan 11, 2005
 */
package org.jboss.ide.eclipse.jdt.aop.core.model.internal;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvice;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAspect;

/**
 * @author Marshall
 */
public class AopAspect implements IAopAspect {

	private IType aspectType;
	private ArrayList advice;
	
	public AopAspect (IJavaProject project, String fqClassName)
	{
		try {
			
			aspectType = JavaModelUtil.findType(project, fqClassName);
			
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		
		advice = new ArrayList();
	}
	
	public boolean hasAdvice (IMethod method) {
		for (Iterator iter = this.advice.iterator(); iter.hasNext(); )
		{
			IAopAdvice advice = (IAopAdvice) iter.next();
			
			if (advice.getAdvisingMethod().equals(method))
			{
				return true;
			}
		}
		
		return false;
	}

	public IAopAdvice addAdvice (IMethod adviceMethod)
	{
		AopAdvice advice = new AopAdvice (this, adviceMethod);
		this.advice.add(advice);
		
		return advice;
	}
	
	public void removeAdvice (IAopAdvice advice)
	{
		this.advice.remove(advice);
	}
	
	public IAopAdvice[] getAdvice() {
		return (IAopAdvice[]) advice.toArray(new IAopAdvice[advice.size()]);
	}

	public IType getType() {
		return aspectType;
	}
}
