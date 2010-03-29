package org.hibernate.mediator.stubs;

import org.hibernate.mapping.IndexedCollection;

public abstract class IndexedCollectionStub extends CollectionStub {
	
	protected IndexedCollection indexedCollection;

	protected IndexedCollectionStub(Object indexedCollection) {
		super(indexedCollection);
		this.indexedCollection = (IndexedCollection)indexedCollection;
	}

	public void setIndex(ValueStub index) {
		indexedCollection.setIndex(index.value);
	}

}
