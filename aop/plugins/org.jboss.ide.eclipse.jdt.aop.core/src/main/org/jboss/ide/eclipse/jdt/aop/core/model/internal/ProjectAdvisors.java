/*
 * Created on Jan 11, 2005
 */
package org.jboss.ide.eclipse.jdt.aop.core.model.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvice;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvisor;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAspect;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopInterceptor;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTTypedefExpression;

/**
 * @author Marshall
 */
public class ProjectAdvisors {

	private Hashtable aspects;
	private ArrayList interceptors;
	private Hashtable typedefs;
	private IJavaProject project;
	
	public ProjectAdvisors (IJavaProject project)
	{
		this.project = project;
		aspects = new Hashtable();
		typedefs = new Hashtable();
		interceptors = new ArrayList();
	}
	
	public boolean hasAspect (String fqClassName)
	{
		return aspects.containsKey(fqClassName);
	}
	
	public void removeAspect (IAopAspect aspect)
	{
		aspects.remove(aspect);
	}
	
	public IAopAspect addAspect (String fqClassName)
	{
		if (! hasAspect(fqClassName))
		{
			AopAspect aspect = new AopAspect (project, fqClassName);
			aspects.put(fqClassName, aspect);
			return aspect;
		}
		return (IAopAspect) aspects.get(fqClassName);
	}
	
	public void removeInterceptor (IAopInterceptor interceptor)
	{
		interceptors.remove(interceptor);
	}
	
	public IAopInterceptor addInterceptor (String fqClassName)
	{
		return addInterceptor (fqClassName, null);
	}
	
	public boolean hasInterceptor (String fqClassName)
	{
		for (Iterator iter = interceptors.iterator(); iter.hasNext(); ) 
		{
			IAopInterceptor interceptor = (IAopInterceptor) iter.next();
			if (interceptor.getAdvisingType().getFullyQualifiedName().equals(fqClassName))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public IAopInterceptor addInterceptor (String fqClassName, String name)
	{
		try {
			if( name == null || name == "") name = fqClassName;
			AopInterceptor interceptor = new AopInterceptor (project, fqClassName, name);
			interceptors.add(interceptor);
			
			return interceptor;
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public IAopInterceptor findInterceptor (String name)
	{
		for (Iterator iter = interceptors.iterator(); iter.hasNext(); )
		{
			IAopInterceptor interceptor = (IAopInterceptor) iter.next();
			if (interceptor.getName() != null && interceptor.getName().equals(name))
			{
				return interceptor;
			}
		}
		
		return null;
	}
	
	public void removeAdvice (IAopAdvice advice)
	{
		advice.getAspect().removeAdvice(advice);
	}
	
	public boolean hasAdvice (String fqClassName, String methodName)
	{
		AopAspect aspect = (AopAspect) aspects.get(fqClassName);
		if (aspect != null)
		{
			IMethod adviceMethod = AopCorePlugin.getDefault().findAdviceMethod(aspect.getType(), methodName);
			return aspect.hasAdvice(adviceMethod);
		}
		
		return false;
	}
	
	public IAopAdvice addAdvice (String fqClassName, String methodName)
	{
		AopAspect aspect = (AopAspect) aspects.get(fqClassName);
		if (aspect != null)
		{
			IMethod adviceMethod = AopCorePlugin.getDefault().findAdviceMethod(aspect.getType(), methodName);
			return aspect.addAdvice(adviceMethod);
		}
		
		return null;
	}
	
	public void removeAdvisor (IAopAdvisor advisor)
	{
		if (advisor.getType() == IAopAdvisor.ADVICE)
		{
			removeAdvice ((IAopAdvice) advisor);
		}
		else if (advisor.getType() == IAopAdvisor.INTERCEPTOR)
		{
			removeInterceptor ((IAopInterceptor) advisor);
		}
	}
	
	public IAopAdvisor[] getAllAdvisors ()
	{
		ArrayList advisors = new ArrayList();
		IAopAspect aspects[] = getAspects();
		IAopInterceptor interceptors[] = getInterceptors();
		
		for (int i = 0; i < aspects.length; i++)
		{
			IAopAdvice advice[] = aspects[i].getAdvice();
			
			for (int j = 0; j < advice.length; j++)
			{
				advisors.add(advice[j]);
			}
		}
		for (int i = 0; i < interceptors.length; i++)
		{
			advisors.add(interceptors[i]);
		}
		return (IAopAdvisor[]) advisors.toArray(new IAopAdvisor[advisors.size()]);
	}
	
	public IAopAspect[] getAspects ()
	{
		return (IAopAspect[]) aspects.values().toArray(new IAopAspect[aspects.size()]);
	}
	
	public IAopInterceptor[] getInterceptors ()
	{
		return (IAopInterceptor[]) interceptors.toArray(new IAopInterceptor[interceptors.size()]);
	}
	
	
	
	/*
	 * Again, pushing the limits of what is an advisor, but for the sake
	 * of using classes that are already here and not overflowing the product:
	 */
	
	public AopTypedef addTypedef(JDTTypedefExpression expression ) {
		AopTypedef def = new AopTypedef(expression);
		typedefs.put(expression.getName(), def);
		return def;
	}
	
	public void removeTypedef(JDTTypedefExpression expression ) {
		typedefs.remove(expression.getName());
	}
	
	public AopTypedef[] getTypedefs ()
	{
		return (AopTypedef[]) typedefs.values().toArray(new AopTypedef[typedefs.size()]);
	}

}
