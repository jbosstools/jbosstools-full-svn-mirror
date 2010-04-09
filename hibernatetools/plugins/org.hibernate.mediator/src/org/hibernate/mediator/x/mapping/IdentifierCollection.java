package org.hibernate.mediator.x.mapping;


public abstract class IdentifierCollection extends CollectionStub {
	public static final String CL = "org.hibernate.mapping.IdentifierCollection"; //$NON-NLS-1$

	protected IdentifierCollection(Object identifierCollection) {
		super(identifierCollection, CL);
	}

	protected IdentifierCollection(Object identifierCollection, String cn) {
		super(identifierCollection, cn);
	}

}
