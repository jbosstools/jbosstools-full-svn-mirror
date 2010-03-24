package org.hibernate.console.stubs;

import org.hibernate.mapping.Set;

public class SetStub extends CollectionStub {
	protected Set set;

	protected SetStub(Object set) {
		super(set);
		this.set = (Set)set;
	}
	
	public static SetStub newInstance(PersistentClassStub owner) {
		return new SetStub(new Set(owner.persistentClass));
	}

	@Override
	public Object accept(ValueVisitorStub visitor) {
		return visitor.accept(this);
	}

}
