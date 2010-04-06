package org.hibernate.mediator.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.hibernate.mediator.stubs.HibernateConsoleRuntimeException;
import org.hibernate.mediator.stubs.util.ReflectHelper;

public class MReflectionUtils {
	
    @SuppressWarnings("unchecked")
	public static Method getMethod(Class targetClass, String methodName, Class[] argClasses) {
		Class<?> clazz;
		try {
			clazz = ReflectHelper.classForName("java.beans.ReflectionUtils"); //$NON-NLS-1$
		} catch (ClassNotFoundException e) {
			throw new HibernateConsoleRuntimeException(e);
		}
		Method method = null;
		try {
			method = clazz.getMethod("getMethod", new Class[] { Class.class, String.class, Class[].class } ); //$NON-NLS-1$
		} catch (SecurityException e) {
			throw new HibernateConsoleRuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new HibernateConsoleRuntimeException(e);
		}
		method.setAccessible(true);
		Method res;
		try {
			res = (Method)method.invoke(null, targetClass, methodName, argClasses);
		} catch (IllegalArgumentException e) {
			throw new HibernateConsoleRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new HibernateConsoleRuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new HibernateConsoleRuntimeException(e);
		}
		return res;
	}
}
