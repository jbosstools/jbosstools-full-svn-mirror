package org.hibernate.mediator.x.cfg.reveng;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.hibernate.mediator.HibernateConsoleRuntimeException;
import org.hibernate.mediator.stubs.util.ReflectHelper;

public abstract class ProgressListener {
	public static final String CL = "org.hibernate.cfg.reveng.ProgressListener"; //$NON-NLS-1$

	protected Object progressListener;
	
	protected ProgressListener() {
		Class<?> clazz;
		try {
			clazz = ReflectHelper.classForName(CL);
		} catch (ClassNotFoundException e) {
			throw new HibernateConsoleRuntimeException(e);
		}
		InvocationHandler handler = new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if ("startSubTask".equals(method.getName())) { //$NON-NLS-1$
					ProgressListener.this.startSubTask(args[0].toString());
				}
				return null;
			}
		};
		progressListener = Proxy.newProxyInstance(clazz.getClassLoader(),
				new Class[] { clazz }, handler);
	}

	public abstract void startSubTask(String name);
}
