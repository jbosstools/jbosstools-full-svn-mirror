package org.hibernate.mediator.x.ejb.packaging;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.hibernate.mediator.HibernateConsoleRuntimeException;
import org.hibernate.mediator.stubs.util.ReflectHelper;

public class FileFilter extends Filter {

	public static final String CL = "org.hibernate.ejb.packaging.FileFilter"; //$NON-NLS-1$

	protected FileFilter(Object fileFilter) {
		super(fileFilter, CL);
	}

	public static FileFilter newInstance(boolean retrieveStream, final IAcceptHolder acceptHolder) {
		Class<?> clazz;
		try {
			clazz = ReflectHelper.classForName(CL);
		} catch (ClassNotFoundException e) {
			throw new HibernateConsoleRuntimeException(e);
		}
		MethodInterceptor mi = new MethodInterceptor() {
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				return acceptHolder.accept((String)args[0]);
			}
		};
		Enhancer e = createEnhancer(clazz, mi);
		Object packageFilter = e.create(new Class[] { boolean.class },
			new Object[] { retrieveStream } );
		return new FileFilter(packageFilter);
	}

}
