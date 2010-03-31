package org.hibernate.mediator.stubs;

import org.hibernate.mapping.IdentifierCollection;
import org.hibernate.mediator.Messages;

public abstract class IdentifierCollectionStub extends CollectionStub {
	protected IdentifierCollection identifierCollection;

	protected IdentifierCollectionStub(Object identifierCollection) {
		super(identifierCollection);
		if (identifierCollection == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.identifierCollection = (IdentifierCollection)identifierCollection;
	}

}
