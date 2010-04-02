package org.hibernate.mediator.base;

import java.beans.Expression;

import org.hibernate.mediator.Messages;
import org.hibernate.mediator.stubs.HibernateConsoleRuntimeException;
import org.hibernate.mediator.stubs.util.ClassHelper;
import org.hibernate.mediator.stubs.util.ReflectHelper;

public class HObject {
	// store object
	protected final Object obj;

	// TODO: get rid of this!!!
	private HObject(Object obj) {
		this.obj = obj;
	}

	//
	public HObject(Object obj, String cn) {
		if (obj == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		if (obj instanceof HObject) {
			this.obj = ((HObject) obj).obj;
		} else {
			this.obj = obj;
		}
		if (!ClassHelper.isClassOrOffspring(Cl(), cn)) {
			throw new HibernateConsoleRuntimeException(Messages.HObject_incompatible_object_types);
		}
	}

	// type of store object
	public final Class<?> Cl() {
		return obj.getClass();
	}

	//
	public final Object Obj() {
		return obj;
	}

	//
	public Object invoke(final String methodName) {
		return invoke(methodName, new HObject[0]);
	}

	// TODO: get rid of Object -> HObject
	public Object invoke(final String methodName, Object param0) {
		return invoke(methodName, new HObject[] { new HObject(param0) });
	}

	// TODO: get rid of Object -> HObject
	public Object invoke(final String methodName, Object param0, Object param1) {
		return invoke(methodName, new HObject[] { new HObject(param0), new HObject(param1) });
	}

	// TODO: get rid of Object -> HObject
	public Object invoke(final String methodName, Object param0, Object param1, Object param2) {
		return invoke(methodName, new HObject[] { new HObject(param0), new HObject(param1),
				new HObject(param2) });
	}

	// TODO: get rid of Object -> HObject
	public Object invoke(final String methodName, Object param0, Object param1, Object param2,
			Object param3) {
		return invoke(methodName, new HObject[] { new HObject(param0), new HObject(param1),
				new HObject(param2), new HObject(param3) });
	}

	// TODO: get rid of Object -> HObject
	public Object invoke(final String methodName, Object param0, Object param1, Object param2,
			Object param3, Object param4) {
		return invoke(methodName, new HObject[] { new HObject(param0), new HObject(param1),
				new HObject(param2), new HObject(param3), new HObject(param4) });
	}

	// TODO: get rid of Object -> HObject
	public Object invoke(final String methodName, Object param0, Object param1, Object param2,
			Object param3, Object param4, Object param5) {
		return invoke(methodName,
				new HObject[] { new HObject(param0), new HObject(param1), new HObject(param2),
						new HObject(param3), new HObject(param4), new HObject(param5) });
	}

	//
	public Object invoke(final String methodName, HObject param) {
		return invoke(methodName, new HObject[] { param });
	}

	//
	public Object invoke(final String methodName, HObject[] params) {
		// Class<?>[] signature = new Class<?>[params.length];
		Object[] vals = new Object[params.length];
		for (int i = 0; i < params.length; i++) {
			// signature[i] = params[i].Cl();
			vals[i] = params[i].obj;
		}
		Expression expression = new Expression(obj, methodName, vals);
		try {
			return expression.getValue();
		} catch (Exception e) {
			throw new HibernateConsoleRuntimeException(e);
		}
	}

	//
	public static Object newInstance(final String cn) {
		return newInstance(cn, new HObject[0]);
	}

	// TODO: get rid of Object -> HObject
	public static Object newInstance(final String cn, Object param0) {
		return newInstance(cn, new HObject[] { new HObject(param0) });
	}

	// TODO: get rid of Object -> HObject
	public static Object newInstance(final String cn, Object param0, Object param1) {
		return newInstance(cn, new HObject[] { new HObject(param0), new HObject(param1) });
	}

	// TODO: get rid of Object -> HObject
	public static Object newInstance(final String cn, Object param0, Object param1, Object param2) {
		return newInstance(cn, new HObject[] { new HObject(param0), new HObject(param1),
				new HObject(param2) });
	}

	//
	public static Object newInstance(final String cn, HObject[] params) {
		Class<?> clazz;
		try {
			clazz = ReflectHelper.classForName(cn);
		} catch (ClassNotFoundException e) {
			throw new HibernateConsoleRuntimeException(e);
		}
		// Class<?>[] signature = new Class<?>[params.length];
		Object[] vals = new Object[params.length];
		for (int i = 0; i < params.length; i++) {
			// signature[i] = params[i].Cl();
			vals[i] = params[i].obj;
		}
		Expression expression = new Expression(clazz, "new", vals); //$NON-NLS-1$
		try {
			return expression.getValue();
		} catch (Exception e) {
			throw new HibernateConsoleRuntimeException(e);
		}
	}
	//
	public static String mn() {
		StackTraceElement[] stes = Thread.currentThread().getStackTrace();
		return trace(stes);
	}
	//
	public static String trace(StackTraceElement e[]) {
		boolean doNext1 = false, doNext2 = false;
		for (StackTraceElement s : e) {
			if (doNext2) {
				return s.getMethodName();
			}
			if (doNext1) {
				doNext2 = true;
				continue;
			}
			doNext1 = s.getMethodName().equals("getStackTrace"); //$NON-NLS-1$
		}
		return ""; //$NON-NLS-1$
	}
}
