package org.hibernate.mediator.x.mapping;


public abstract class IndexedCollection extends CollectionStub {
	public static final String CL = "org.hibernate.mapping.IndexedCollection"; //$NON-NLS-1$

	protected IndexedCollection(Object indexedCollection) {
		super(indexedCollection, CL);
	}

	protected IndexedCollection(Object indexedCollection, String cn) {
		super(indexedCollection, cn);
	}

	public void setIndex(Value index) {
		invoke(mn(), index);
	}

}
