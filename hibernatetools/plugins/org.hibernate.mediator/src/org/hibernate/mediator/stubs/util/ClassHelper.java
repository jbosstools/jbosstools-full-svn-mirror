package org.hibernate.mediator.stubs.util;

import java.lang.reflect.Type;

public class ClassHelper {
	public static boolean isClassOrOffspring(final Class<?> cl, final String name) {
		if (cl == null || name == null) {
			return false;
		}
		if (0 == name.compareTo(cl.getName())) {
			return true;
		}
		Type type = cl.getGenericSuperclass();
		if (type instanceof Class<?>) {
			return isClassOrOffspring((Class<?>)type, name);
		}
		return false;
	}
}
