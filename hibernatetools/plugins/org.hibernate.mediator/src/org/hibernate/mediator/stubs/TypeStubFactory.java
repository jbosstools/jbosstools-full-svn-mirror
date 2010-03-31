package org.hibernate.mediator.stubs;

import java.lang.reflect.Type;

import org.hibernate.mediator.stubs.util.ClassHelper;

public class TypeStubFactory {
	@SuppressWarnings("unchecked")
	public static TypeStub createTypeStub(Object value) {
		if (value == null) {
			return null;
		}
		final Class cl = value.getClass();
		if (ClassHelper.isClassOrOffspring(cl, "org.hibernate.type.CollectionType")) { //$NON-NLS-1$
			return new CollectionTypeStub(value);
		//} else if (0 == "org.hibernate.type.Type".compareTo(cl.getName())) { //$NON-NLS-1$
		//	return new TypeStub(value);
		} else if (ClassHelper.isClassOrOffspring(cl, "org.hibernate.type.EntityType")) { //$NON-NLS-1$
			return new EntityTypeStub(value);
		} else if (ClassHelper.isClassOrOffspring(cl, "org.hibernate.type.NullableType")) { //$NON-NLS-1$
			return new NullableTypeStub(value);
		}
		return new AbstractTypeStub(value);
	}
}
