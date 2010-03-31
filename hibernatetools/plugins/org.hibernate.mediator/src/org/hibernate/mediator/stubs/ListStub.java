package org.hibernate.mediator.stubs;

import org.hibernate.mapping.List;
import org.hibernate.mediator.Messages;

public class ListStub extends IndexedCollectionStub {
	protected List list;

	protected ListStub(Object list) {
		super(list);
		if (list == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
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
