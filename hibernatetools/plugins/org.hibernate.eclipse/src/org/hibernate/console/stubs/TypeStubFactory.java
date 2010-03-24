package org.hibernate.console.stubs;

public class TypeStubFactory {
	@SuppressWarnings("unchecked")
	public static TypeStub createTypeStub(Object value) {
		final Class cl = value.getClass();
		if (0 == "org.hibernate.type.CollectionType".compareTo(cl.getName())) { //$NON-NLS-1$
			return new CollectionTypeStub(value);
		//} else if (0 == "org.hibernate.type.Type".compareTo(cl.getName())) { //$NON-NLS-1$
		//	return new TypeStub(value);
		} else if (0 == "org.hibernate.type.EntityType".compareTo(cl.getName())) { //$NON-NLS-1$
			return new EntityTypeStub(value);
		}
		return null;
	}
}
