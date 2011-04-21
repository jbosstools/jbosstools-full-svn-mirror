package org.hibernate.mediator.x.mapping;

import org.hibernate.mediator.stubs.util.ClassHelper;


public class PersistentClassFactory {
	@SuppressWarnings("unchecked")
	public static PersistentClass createPersistentClassStub(Object value) {
		if (value == null) {
			return null;
		}
		final Class cl = value.getClass();
		if (ClassHelper.isClassOrOffspring(cl, JoinedSubclass.CL)) {
			return new JoinedSubclass(value);
		} else if (ClassHelper.isClassOrOffspring(cl, SingleTableSubclass.CL)) {
			return new SingleTableSubclass(value);
		} else if (ClassHelper.isClassOrOffspring(cl, UnionSubclass.CL)) {
			return new UnionSubclass(value);
		} else if (ClassHelper.isClassOrOffspring(cl, Subclass.CL)) {
			return new Subclass(value);
		} else if (ClassHelper.isClassOrOffspring(cl, RootClass.CL)) {
			return new RootClass(value);
		}
		return null;
	}

	public static PersistentClass createPersistentClassStub(PersistentClass value) {
		return createPersistentClassStub(value.Obj());
	}
}
