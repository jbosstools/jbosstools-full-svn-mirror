package org.hibernate.mediator.x.ejb.packaging;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.hibernate.mediator.base.HObject;

public class Filter extends HObject {

	public static final String CL = "org.hibernate.ejb.packaging.Filter"; //$NON-NLS-1$

	protected Filter(Object filter) {
		super(filter, CL);
	}

	protected Filter(Object filter, String cn) {
		super(filter, cn);
	}
	
	public interface IAcceptHolder {
		public boolean accept(String javaElementName);
	}

	
	private static final CallbackFilter baseNodeFilter = new CallbackFilter() {
		public int accept(Method method) {
			if ("accept".equals(method.getName() ) ) {
				return 1;
			}
			return 0;
		}
	};
	
	public static Enhancer createEnhancer(Class<?> clazz, MethodInterceptor mi) {
		Enhancer e = new Enhancer();
        e.setSuperclass(clazz);
		MethodInterceptor miDef = new MethodInterceptor() {
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				return proxy.invokeSuper(obj, args);
			}
		};
        e.setCallbacks(new Callback[] { miDef, mi });
        e.setCallbackFilter(baseNodeFilter);
        return e;
	}
}
