package org.hibernate.mediator.x.type;

import org.hibernate.mediator.stubs.util.ClassHelper;

public class TypeFactory {
	@SuppressWarnings("unchecked")
	public static Type createTypeStub(Object value) {
		if (value == null) {
			return null;
		}
		final Class cl = value.getClass();
		if (ClassHelper.isClassOrOffspring(cl, CollectionType.CL)) {
			return new CollectionType(value);
		//} else if (0 == TypeStub.CL.compareTo(cl.getName())) {
		//	return new TypeStub(value);
		} else if (ClassHelper.isClassOrOffspring(cl, EntityType.CL)) {
			return new EntityType(value);
		} else if (ClassHelper.isClassOrOffspring(cl, IntegerType.CL)) {
			return new IntegerType(value);
		} else if (ClassHelper.isClassOrOffspring(cl, PrimitiveType.CL)) {
			return new PrimitiveType(value);
		} else if (ClassHelper.isClassOrOffspring(cl, ImmutableType.CL)) {
			return new ImmutableType(value);
		} else if (ClassHelper.isClassOrOffspring(cl, NullableType.CL)) {
			return new NullableType(value);
		}
		return new AbstractType(value);
	}
	
	public static NullableType createNTS(Object value) {
		Type ts = createTypeStub(value);
		if (ts == null) {
			return null;
		}
		return (NullableType)ts;
	}
}
