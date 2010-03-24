package org.hibernate.console.stubs;

public class PersistentClassStubFactory {
	@SuppressWarnings("unchecked")
	public static PersistentClassStub createPersistentClassStub(Object value) {
		final Class cl = value.getClass();
		if (0 == "org.hibernate.mapping.RootClass".compareTo(cl.getName())) { //$NON-NLS-1$
			return new RootClassStub(value);
		//} else if (0 == "org.hibernate.mapping.PersistentClass".compareTo(cl.getName())) { //$NON-NLS-1$
		//	return new PersistentClassStub(value);
		} else if (0 == "org.hibernate.mapping.Subclass".compareTo(cl.getName())) { //$NON-NLS-1$
			return new SubclassStub(value);
		} else if (0 == "org.hibernate.mapping.JoinedSubclass".compareTo(cl.getName())) { //$NON-NLS-1$
			return new JoinedSubclassStub(value);
		} else if (0 == "org.hibernate.mapping.SingleTableSubclass".compareTo(cl.getName())) { //$NON-NLS-1$
			return new SingleTableSubclassStub(value);
		//} else if (0 == "org.hibernate.mapping.UnionSubclass".compareTo(cl.getName())) { //$NON-NLS-1$
		//	return new UnionSubclassStub(value);
		}
		return null;
	}

	public static PersistentClassStub createPersistentClassStub(PersistentClassStub value) {
		return createPersistentClassStub(value.persistentClass);
	}
}
