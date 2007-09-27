/*
 * Created on Oct 19, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.jdt.aop.core.model.internal;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.jboss.ide.eclipse.jdt.aop.core.model.IAopInterceptor;


public class AopInterceptor extends AopAdvisor implements IAopInterceptor
{
	private String name;
	
	public AopInterceptor (IJavaProject project, String fqClassName, String name)
		throws JavaModelException
	{
		super (JavaModelUtil.findType(project, fqClassName));
		this.name = name;
	}
	
	public AopInterceptor (IType interceptorType)
	{
		super(interceptorType);
	}

	public IType getAdvisingType ()
	{
		return (IType) getAdvisingElement();
	}
	
	public String getName() {
		return name;
	}
	
	public int getType() { return INTERCEPTOR; }
}