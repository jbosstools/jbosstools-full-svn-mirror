package org.hibernate.mediator.x.mapping;


// look at org.hibernate.mapping.ValueVisitor
public interface ValueVisitor {
	Object accept(Bag bag);
	Object accept(IdentifierBag bag);
	Object accept(ListStub list);
	Object accept(PrimitiveArray primitiveArray);
	Object accept(Array list);
	Object accept(MapStub map);
	Object accept(OneToMany many);
	Object accept(SetStub set);
	Object accept(Any any);
	Object accept(SimpleValue value);
	Object accept(DependantValue value);
	Object accept(Component component);
	Object accept(ManyToOne mto);
	Object accept(OneToOne oto);
}
