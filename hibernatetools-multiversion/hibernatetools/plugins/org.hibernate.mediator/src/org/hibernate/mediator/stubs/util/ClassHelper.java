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
		Type[] types = cl.getInterfaces();
		for (Type type : types) {
			if (type instanceof Class<?>) {
				if (0 == name.compareTo(((Class<?>)type).getName())) {
					return true;
				}
				boolean b = isClassOrOffspring((Class<?>)type, name);
				if (b) {
					return true;
				}
			}
		}
		Type type = cl.getGenericSuperclass();
		if (type instanceof Class<?>) {
			boolean b = isClassOrOffspring((Class<?>)type, name);
			if (b) {
				return true;
			}
		}
		return false;
	}
}
