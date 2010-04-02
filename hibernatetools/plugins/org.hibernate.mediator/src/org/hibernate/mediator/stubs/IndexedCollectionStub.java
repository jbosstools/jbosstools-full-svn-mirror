package org.hibernate.mediator.stubs;

import org.hibernate.mapping.IndexedCollection;
import org.hibernate.mediator.Messages;

public abstract class IndexedCollectionStub extends CollectionStub {
	public static final String CL = "org.hibernate.mapping.IndexedCollection"; //$NON-NLS-1$

	protected IndexedCollection indexedCollection;

	protected IndexedCollectionStub(Object indexedCollection) {
		super(indexedCollection);
		if (indexedCollection == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.indexedCollection = (IndexedCollection)indexedCollection;
	}

	public void setIndex(ValueStub index) {
		indexedCollection.setIndex(index.value);
	}

}
