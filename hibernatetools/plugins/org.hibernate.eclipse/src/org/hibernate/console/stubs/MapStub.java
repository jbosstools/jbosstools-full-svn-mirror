package org.hibernate.console.stubs;

import org.hibernate.mapping.Map;

public class MapStub extends IndexedCollectionStub {
	protected Map map;

	protected MapStub(Object map) {
		super(map);
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
