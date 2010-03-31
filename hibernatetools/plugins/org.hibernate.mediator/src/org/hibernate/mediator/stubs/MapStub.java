package org.hibernate.mediator.stubs;

import org.hibernate.mapping.Map;
import org.hibernate.mediator.Messages;

public class MapStub extends IndexedCollectionStub {
	protected Map map;

	protected MapStub(Object map) {
		super(map);
		if (map == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.map = (Map)map;
	}
	
	public static MapStub newInstance(PersistentClassStub owner) {
		return new MapStub(new Map(owner.persistentClass));
	}

	@Override
	public Object accept(ValueVisitorStub visitor) {
		return visitor.accept(this);
	}
}
