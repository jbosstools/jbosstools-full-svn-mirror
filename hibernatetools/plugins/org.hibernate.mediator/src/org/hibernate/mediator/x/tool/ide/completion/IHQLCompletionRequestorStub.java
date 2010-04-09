package org.hibernate.mediator.x.tool.ide.completion;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.hibernate.mediator.HibernateConsoleRuntimeException;
import org.hibernate.mediator.stubs.util.ReflectHelper;

public abstract class IHQLCompletionRequestorStub {
	public static final String CL = "org.hibernate.tool.ide.completion.IHQLCompletionRequestor"; //$NON-NLS-1$

	protected Object hqlCompletionRequestor;

	public IHQLCompletionRequestorStub() {
		Class<?> clazz;
		try {
			clazz = ReflectHelper.classForName(CL);
		} catch (ClassNotFoundException e) {
			throw new HibernateConsoleRuntimeException(e);
		}
		InvocationHandler handler = new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if ("accept".equals(method.getName())) { //$NON-NLS-1$
					return IHQLCompletionRequestorStub.this.accept(new HQLCompletionProposalStub(args[0]));
				} else if ("completionFailure".equals(method.getName())) { //$NON-NLS-1$
					IHQLCompletionRequestorStub.this.completionFailure(args[0].toString());
				}
				return null;
			}
		};
		hqlCompletionRequestor = Proxy.newProxyInstance(clazz.getClassLoader(),
				new Class[] { clazz }, handler);
	}

	public abstract boolean accept(HQLCompletionProposalStub proposal);

	public abstract void completionFailure(String errorMessage);
}
