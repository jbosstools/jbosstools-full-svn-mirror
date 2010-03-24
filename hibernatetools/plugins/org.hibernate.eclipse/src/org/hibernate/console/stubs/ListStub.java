package org.hibernate.console.stubs;

import org.hibernate.mapping.List;

public class ListStub extends IndexedCollectionStub {
	protected List list;

	protected ListStub(Object list) {
		super(list);
		this.list = (List)list;
	}
	
	public static ListStub newInstance(PersistentClassStub owner) {
		return new ListStub(new List(owner.persistentClass));
	}

	@Override
	public Object accept(ValueVisitorStub visitor) {
		return visitor.accept(this);
	}

	public ValueStub getIndex() {
		return ValueStubFactory.createValueStub(list.getIndex());
	}

}
