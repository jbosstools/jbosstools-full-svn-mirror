package org.hibernate.mediator.stubs;

import org.hibernate.mapping.Bag;

public class BagStub extends CollectionStub {
	protected Bag bag;

	protected BagStub(Object bag) {
		super(bag);
		this.bag = (Bag)bag;
	}
	
	public static BagStub newInstance(PersistentClassStub owner) {
		return new BagStub(new Bag(owner.persistentClass));
	}

	@Override
	public Object accept(ValueVisitorStub visitor) {
		return visitor.accept(this);
	}
}
