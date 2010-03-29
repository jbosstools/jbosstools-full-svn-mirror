package org.hibernate.mediator.stubs.util;

//org.hibernate.util.ReflectHelper
public final class ReflectHelper {

	@SuppressWarnings("unchecked")
	public static Class classForName(String name) throws ClassNotFoundException {
		try {
			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			if (contextClassLoader != null) {
				return contextClassLoader.loadClass(name);
			}
		} catch (Throwable ignore) {
		}
		return Class.forName(name);
	}

	@SuppressWarnings("unchecked")
	public static Class classForName(String name, Class caller) throws ClassNotFoundException {
		try {
			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			if (contextClassLoader != null) {
				return contextClassLoader.loadClass(name);
			}
		} catch (Throwable ignore) {
		}
		return Class.forName(name, true, caller.getClassLoader());
	}
}
