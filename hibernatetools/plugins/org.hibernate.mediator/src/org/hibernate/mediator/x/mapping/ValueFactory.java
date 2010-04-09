package org.hibernate.mediator.x.mapping;


public class ValueFactory {
	@SuppressWarnings("unchecked")
	public static Value createValueStub(Object value) {
		if (value == null) {
			return null;
		}
		final Class cl = value.getClass();
		if (0 == Bag.CL.compareTo(cl.getName())) {
			return new Bag(value);
		//} else if (0 == "org.hibernate.mapping.IdentifierCollection".compareTo(cl.getName())) {
		//	return new IdentifierCollectionStub(value);
		} else if (0 == IdentifierBag.CL.compareTo(cl.getName())) {
			return new IdentifierBag(value);
		//} else if (0 == "org.hibernate.mapping.IndexedCollection".compareTo(cl.getName())) {
		//	return new IndexedCollectionStub(value);
		} else if (0 == ListStub.CL.compareTo(cl.getName())) {
			return new ListStub(value);
		} else if (0 == Array.CL.compareTo(cl.getName())) {
			return new Array(value);
		} else if (0 == PrimitiveArray.CL.compareTo(cl.getName())) {
			return new PrimitiveArray(value);
		} else if (0 == MapStub.CL.compareTo(cl.getName())) {
			return new MapStub(value);
		} else if (0 == SetStub.CL.compareTo(cl.getName())) {
			return new SetStub(value);
		} else if (0 == OneToMany.CL.compareTo(cl.getName())) {
			return new OneToMany(value);
		//} else if (0 == KeyValueStub.CL.compareTo(cl.getName())) {
		//	return new KeyValueStub(value);
		} else if (0 == SimpleValue.CL.compareTo(cl.getName())) {
			return new SimpleValue(value);
		} else if (0 == Any.CL.compareTo(cl.getName())) {
			return new Any(value);
		} else if (0 == Component.CL.compareTo(cl.getName())) {
			return new Component(value);
		} else if (0 == DependantValue.CL.compareTo(cl.getName())) {
			return new DependantValue(value);
		} else if (0 == ToOne.CL.compareTo(cl.getName())) {
			return new ToOne(value);
		} else if (0 == ManyToOne.CL.compareTo(cl.getName())) {
			return new ManyToOne(value);
		} else if (0 == OneToOne.CL.compareTo(cl.getName())) {
			return new OneToOne(value);
		}
		return null;
	}
}
