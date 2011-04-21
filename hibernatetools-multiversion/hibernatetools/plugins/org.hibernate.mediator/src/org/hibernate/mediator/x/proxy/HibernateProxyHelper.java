package org.hibernate.mediator.x.proxy;

import org.hibernate.mediator.base.HObject;

public class HibernateProxyHelper {
	public static final String CL = "org.hibernate.proxy.HibernateProxyHelper"; //$NON-NLS-1$
	
	public static Class<?> getClassWithoutInitializingProxy(Object object) {
		return (Class<?>)HObject.invokeStaticMethod(CL, "getClassWithoutInitializingProxy", object); //$NON-NLS-1$
	}
}
