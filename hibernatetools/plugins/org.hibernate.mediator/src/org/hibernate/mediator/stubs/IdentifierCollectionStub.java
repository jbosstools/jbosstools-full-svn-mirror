package org.hibernate.mediator.stubs;

public abstract class IdentifierCollectionStub extends CollectionStub {
	public static final String CL = "org.hibernate.mapping.IdentifierCollection"; //$NON-NLS-1$

	protected IdentifierCollectionStub(Object identifierCollection) {
		super(identifierCollection, CL);
	}

	protected IdentifierCollectionStub(Object identifierCollection, String cn) {
		super(identifierCollection, cn);
	}

}
