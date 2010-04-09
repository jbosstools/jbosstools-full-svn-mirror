package org.hibernate.mediator.x.mapping;


public class PersistentClassStubFactory {
	@SuppressWarnings("unchecked")
	public static PersistentClassStub createPersistentClassStub(Object value) {
		if (value == null) {
			return null;
		}
		final Class cl = value.getClass();
		if (0 == RootClassStub.CL.compareTo(cl.getName())) {
			return new RootClassStub(value);
		//} else if (0 == PersistentClassStub.CL.compareTo(cl.getName())) {
		//	return new PersistentClassStub(value);
		} else if (0 == SubclassStub.CL.compareTo(cl.getName())) {
			return new SubclassStub(value);
		} else if (0 == JoinedSubclassStub.CL.compareTo(cl.getName())) {
			return new JoinedSubclassStub(value);
		} else if (0 == SingleTableSubclassStub.CL.compareTo(cl.getName())) {
			return new SingleTableSubclassStub(value);
		} else if (0 == UnionSubclassStub.CL.compareTo(cl.getName())) {
			return new UnionSubclassStub(value);
		}
		return null;
	}

	public static PersistentClassStub createPersistentClassStub(PersistentClassStub value) {
		return createPersistentClassStub(value.Obj());
	}
}
