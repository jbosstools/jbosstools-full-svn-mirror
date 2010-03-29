package org.hibernate.mediator.stubs;

import org.hibernate.mapping.IdentifierCollection;

public abstract class IdentifierCollectionStub extends CollectionStub {
	protected IdentifierCollection identifierCollection;

	protected IdentifierCollectionStub(Object identifierCollection) {
		super(identifierCollection);
		this.identifierCollection = (IdentifierCollection)identifierCollection;
	}

}
