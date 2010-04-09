package org.hibernate.mediator.x.mapping;


public class PersistentClassFactory {
	@SuppressWarnings("unchecked")
	public static PersistentClass createPersistentClassStub(Object value) {
		if (value == null) {
			return null;
		}
		final Class cl = value.getClass();
		if (0 == RootClass.CL.compareTo(cl.getName())) {
			return new RootClass(value);
		//} else if (0 == PersistentClassStub.CL.compareTo(cl.getName())) {
		//	return new PersistentClassStub(value);
		} else if (0 == Subclass.CL.compareTo(cl.getName())) {
			return new Subclass(value);
		} else if (0 == JoinedSubclass.CL.compareTo(cl.getName())) {
			return new JoinedSubclass(value);
		} else if (0 == SingleTableSubclass.CL.compareTo(cl.getName())) {
			return new SingleTableSubclass(value);
		} else if (0 == UnionSubclass.CL.compareTo(cl.getName())) {
			return new UnionSubclass(value);
		}
		return null;
	}

	public static PersistentClass createPersistentClassStub(PersistentClass value) {
		return createPersistentClassStub(value.Obj());
	}
}
