package org.hibernate.mediator.stubs;

import org.hibernate.mediator.stubs.util.ClassHelper;

public class TypeStubFactory {
	@SuppressWarnings("unchecked")
	public static TypeStub createTypeStub(Object value) {
		if (value == null) {
			return null;
		}
		final Class cl = value.getClass();
		if (ClassHelper.isClassOrOffspring(cl, CollectionTypeStub.CL)) {
			return new CollectionTypeStub(value);
		//} else if (0 == TypeStub.CL.compareTo(cl.getName())) {
		//	return new TypeStub(value);
		} else if (ClassHelper.isClassOrOffspring(cl, EntityTypeStub.CL)) {
			return new EntityTypeStub(value);
		} else if (ClassHelper.isClassOrOffspring(cl, NullableTypeStub.CL)) {
			return new NullableTypeStub(value);
		}
		return new AbstractTypeStub(value);
	}
}
