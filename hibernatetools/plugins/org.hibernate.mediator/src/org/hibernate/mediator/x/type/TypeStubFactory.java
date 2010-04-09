package org.hibernate.mediator.x.type;

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
		} else if (ClassHelper.isClassOrOffspring(cl, IntegerTypeStub.CL)) {
			return new IntegerTypeStub(value);
		} else if (ClassHelper.isClassOrOffspring(cl, PrimitiveTypeStub.CL)) {
			return new PrimitiveTypeStub(value);
		} else if (ClassHelper.isClassOrOffspring(cl, ImmutableTypeStub.CL)) {
			return new ImmutableTypeStub(value);
		} else if (ClassHelper.isClassOrOffspring(cl, NullableTypeStub.CL)) {
			return new NullableTypeStub(value);
		}
		return new AbstractTypeStub(value);
	}
	
	public static NullableTypeStub createNTS(Object value) {
		TypeStub ts = createTypeStub(value);
		if (ts == null) {
			return null;
		}
		return (NullableTypeStub)ts;
	}
}
