package org.dom4j.mediator.x;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.hibernate.mediator.HibernateConsoleRuntimeException;
import org.hibernate.mediator.stubs.util.ClassHelper;
import org.hibernate.mediator.stubs.util.ReflectHelper;

public abstract class Visitor {

	public static final String CL = "org.dom4j.Visitor"; //$NON-NLS-1$

	protected Object visitor;

	public Visitor() {
		Class<?> clazz;
		try {
			clazz = ReflectHelper.classForName(CL);
		} catch (ClassNotFoundException e) {
			throw new HibernateConsoleRuntimeException(e);
		}
		InvocationHandler handler = new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if ("visit".equals(method.getName())) { //$NON-NLS-1$
					Visitor.visit(Visitor.this, args[0]);
				}
				return null;
			}
		};
		visitor = Proxy.newProxyInstance(clazz.getClassLoader(),
			new Class[] { clazz }, handler);
	}

	// type of store object
	public final Class<?> Cl() {
		return visitor.getClass();
	}

	//
	public final Object Obj() {
		return visitor;
	}
	
    @SuppressWarnings("unchecked")
	protected static void visit(Visitor visitor, Object obj) {
		if (obj == null) {
			return;
		}
		final Class cl = obj.getClass();
		if (ClassHelper.isClassOrOffspring(cl, Document.CL)) {
			visitor.visit(new Document(obj));
		} else if (ClassHelper.isClassOrOffspring(cl, Element.CL)) {
			visitor.visit(new Element(obj));
		} else if (ClassHelper.isClassOrOffspring(cl, Attribute.CL)) {
			visitor.visit(new Attribute(obj));
		}
    }
	
    public void visit(Document document) {
    }

    public void visit(Element node) {
    }

    public void visit(Attribute node) {
    }
}
