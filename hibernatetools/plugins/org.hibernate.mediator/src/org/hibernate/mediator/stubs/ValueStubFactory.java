package org.hibernate.mediator.stubs;

public class ValueStubFactory {
	@SuppressWarnings("unchecked")
	public static ValueStub createValueStub(Object value) {
		if (value == null) {
			return null;
		}
		final Class cl = value.getClass();
		if (0 == BagStub.CL.compareTo(cl.getName())) {
			return new BagStub(value);
		//} else if (0 == "org.hibernate.mapping.IdentifierCollection".compareTo(cl.getName())) {
		//	return new IdentifierCollectionStub(value);
		} else if (0 == IdentifierBagStub.CL.compareTo(cl.getName())) {
			return new IdentifierBagStub(value);
		//} else if (0 == "org.hibernate.mapping.IndexedCollection".compareTo(cl.getName())) {
		//	return new IndexedCollectionStub(value);
		} else if (0 == ListStub.CL.compareTo(cl.getName())) {
			return new ListStub(value);
		} else if (0 == ArrayStub.CL.compareTo(cl.getName())) {
			return new ArrayStub(value);
		} else if (0 == PrimitiveArrayStub.CL.compareTo(cl.getName())) {
			return new PrimitiveArrayStub(value);
		} else if (0 == MapStub.CL.compareTo(cl.getName())) {
			return new MapStub(value);
		} else if (0 == SetStub.CL.compareTo(cl.getName())) {
			return new SetStub(value);
		} else if (0 == OneToManyStub.CL.compareTo(cl.getName())) {
			return new OneToManyStub(value);
		//} else if (0 == KeyValueStub.CL.compareTo(cl.getName())) {
		//	return new KeyValueStub(value);
		} else if (0 == SimpleValueStub.CL.compareTo(cl.getName())) {
			return new SimpleValueStub(value);
		} else if (0 == AnyStub.CL.compareTo(cl.getName())) {
			return new AnyStub(value);
		} else if (0 == ComponentStub.CL.compareTo(cl.getName())) {
			return new ComponentStub(value);
		} else if (0 == DependantValueStub.CL.compareTo(cl.getName())) {
			return new DependantValueStub(value);
		} else if (0 == ToOneStub.CL.compareTo(cl.getName())) {
			return new ToOneStub(value);
		} else if (0 == ManyToOneStub.CL.compareTo(cl.getName())) {
			return new ManyToOneStub(value);
		} else if (0 == OneToOneStub.CL.compareTo(cl.getName())) {
			return new OneToOneStub(value);
		}
		return null;
	}
}
