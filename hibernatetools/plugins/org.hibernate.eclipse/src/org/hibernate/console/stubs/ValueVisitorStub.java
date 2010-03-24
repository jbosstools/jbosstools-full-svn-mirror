package org.hibernate.console.stubs;

// look at org.hibernate.mapping.ValueVisitor
public interface ValueVisitorStub {
	Object accept(BagStub bag);
	Object accept(IdentifierBagStub bag);
	Object accept(ListStub list);
	Object accept(PrimitiveArrayStub primitiveArray);
	Object accept(ArrayStub list);
	Object accept(MapStub map);
	Object accept(OneToManyStub many);
	Object accept(SetStub set);
	Object accept(AnyStub any);
	Object accept(SimpleValueStub value);
	Object accept(DependantValueStub value);
	Object accept(ComponentStub component);
	Object accept(ManyToOneStub mto);
	Object accept(OneToOneStub oto);
}
