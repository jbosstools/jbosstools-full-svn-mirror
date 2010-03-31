package org.hibernate.mediator.stubs;

import org.hibernate.mapping.Bag;
import org.hibernate.mediator.Messages;

public class BagStub extends CollectionStub {
	protected Bag bag;

	protected BagStub(Object bag) {
		super(bag);
		if (bag == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
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
