package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public class HibernateProxyHelperStub {
	public static final String CL = "org.hibernate.proxy.HibernateProxyHelper"; //$NON-NLS-1$
	
	public static Class<?> getClassWithoutInitializingProxy(Object object) {
		return (Class<?>)HObject.invokeStaticMethod(CL, "getClassWithoutInitializingProxy"); //$NON-NLS-1$
	}
}
