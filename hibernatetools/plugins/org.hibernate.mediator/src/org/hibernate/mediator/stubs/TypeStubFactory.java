package org.hibernate.mediator.stubs;

import java.lang.reflect.Type;

public class TypeStubFactory {
	@SuppressWarnings("unchecked")
	public static TypeStub createTypeStub(Object value) {
		final Class cl = value.getClass();
		if (isClassOrOffspring(cl, "org.hibernate.type.CollectionType")) { //$NON-NLS-1$
			return new CollectionTypeStub(value);
		//} else if (0 == "org.hibernate.type.Type".compareTo(cl.getName())) { //$NON-NLS-1$
		//	return new TypeStub(value);
		} else if (isClassOrOffspring(cl, "org.hibernate.type.EntityType")) { //$NON-NLS-1$
			return new EntityTypeStub(value);
		} else if (isClassOrOffspring(cl, "org.hibernate.type.NullableType")) { //$NON-NLS-1$
			return new NullableTypeStub(value);
		}
		return new AbstractTypeStub(value);
	}
	
	protected static boolean isClassOrOffspring(final Class<?> cl, final String name) {
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
