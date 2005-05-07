package org.jboss.ide.eclipse.jdt.aop.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Advice;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aop;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aspect;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Binding;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Interceptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.InterceptorRef;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Pointcut;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Typedef;

/**
 * 
 * @author Rob Stryker
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AopModelUtils {

	
	/**
	 * Private method that returns all children of aop of the 
	 * specified type.
	 * @param clazz  The class type
	 * @param aop The aop object
	 * @return A list of matching nodes
	 */
	
	
	private static List getTypeFromAop(Class clazz, Aop aop) {
		Iterator i = aop.getTopLevelElements().iterator();
		ArrayList list = new ArrayList();
		while( i.hasNext() ) {
			Object o = i.next();
			if( clazz.isAssignableFrom(o.getClass())) {
				list.add(o);
			}
		}
		return list;
	}

	
	public static List getPointcutsFromAop(Aop aop) {
		return getTypeFromAop(Pointcut.class, aop);
	}
	
	public static List getAspectsFromAop(Aop aop) {
		return getTypeFromAop(Aspect.class, aop);
	}
	
	public static List getInterceptorsFromAop(Aop aop) {
		return getTypeFromAop(Interceptor.class, aop);
	}
	
	public static List getBindingsFromAop(Aop aop) {
		return getTypeFromAop(Binding.class, aop);
	}
	
	public static List getTypedefsFromAop(Aop aop) {
		return getTypeFromAop(Typedef.class, aop);
	}
	
	
	
	/**
	 * Private method that returns all children of a binding 
	 * of the specified type.
	 * 
	 * @param clazz The class type
	 * @param binding The binding object
	 * @return A list of matching nodes
	 */
	private static List getFromBinding(Class clazz, Binding binding) {
		Iterator i = binding.getElements().iterator();
		ArrayList list = new ArrayList();
		while( i.hasNext() ) {
			Object o = i.next();
			if( clazz.isAssignableFrom(o.getClass())) {
				list.add(o);
			}
		}
		return list;
	}
	
	
	public static List getInterceptorsFromBinding(Binding binding) {
		return getFromBinding(Interceptor.class, binding);
	}
	public static List getInterceptorRefssFromBinding(Binding binding) {
		return getFromBinding(InterceptorRef.class, binding);
	}
	public static List getAdvicesFromBinding(Binding binding) {
		return getFromBinding(Advice.class, binding);
	}
	

}
