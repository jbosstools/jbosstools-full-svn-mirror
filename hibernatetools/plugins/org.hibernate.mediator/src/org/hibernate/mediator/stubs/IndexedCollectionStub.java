package org.hibernate.mediator.stubs;

public abstract class IndexedCollectionStub extends CollectionStub {
	public static final String CL = "org.hibernate.mapping.IndexedCollection"; //$NON-NLS-1$

	protected IndexedCollectionStub(Object indexedCollection) {
		super(indexedCollection, CL);
	}

	protected IndexedCollectionStub(Object indexedCollection, String cn) {
		super(indexedCollection, cn);
	}

	public void setIndex(ValueStub index) {
		invoke(mn(), index);
	}

}
