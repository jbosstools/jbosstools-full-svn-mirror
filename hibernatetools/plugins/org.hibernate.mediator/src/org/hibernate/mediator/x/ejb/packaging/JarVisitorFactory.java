package org.hibernate.mediator.x.ejb.packaging;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import org.hibernate.mediator.HibernateConsoleRuntimeException;
import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.stubs.util.ReflectHelper;
import org.hibernate.mediator.util.MReflectionUtils;

public class JarVisitorFactory extends HObject {

	public static final String CL = "org.hibernate.ejb.packaging.JarVisitorFactory"; //$NON-NLS-1$

	protected JarVisitorFactory(Object jarVisitorFactory) {
		super(jarVisitorFactory, CL);
	}

	public static URL getJarURLFromURLEntry(URL url, String entry) {
		return (URL)invokeStaticMethod(CL, mn(), url, entry);
	}

	
    public static Object invokeStaticGetVisitor(URL jarUrl, Filter[] filters) {
		Class<?>[] signature = new Class<?>[2];
		Object[] vals = new Object[2];
		//
		signature[0] = jarUrl.getClass();
		vals[0] = jarUrl;
		//
		Class<?> clazz;
		try {
			clazz = ReflectHelper.classForName(Filter.CL);
		} catch (ClassNotFoundException e) {
			throw new HibernateConsoleRuntimeException(e);
		}
		signature[1] = java.lang.reflect.Array.newInstance(clazz, 0).getClass();
		Object[] objFilters = (Object[])java.lang.reflect.Array.newInstance(clazz, filters.length);
		for (int i = 0; i < filters.length; i++) {
			objFilters[i] = filters[i].Obj();
		}
		vals[1] = objFilters;
		//
		try {
			clazz = ReflectHelper.classForName(CL);
		} catch (ClassNotFoundException e) {
			throw new HibernateConsoleRuntimeException(e);
		}
		Method method = MReflectionUtils.getMethod(clazz, "getVisitor", signature );
		Object res;
		try {
			res = method.invoke(null, vals);
		} catch (IllegalArgumentException e) {
			throw new HibernateConsoleRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new HibernateConsoleRuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new HibernateConsoleRuntimeException(e);
		}
		return res;
    }
	
	public static JarVisitor getVisitor(URL jarUrl, Filter[] filters) {
		//return new JarVisitor(invokeStaticMethod(CL, mn(), jarUrl, filters));
		return new JarVisitor(invokeStaticGetVisitor(jarUrl, filters));
	}
}
