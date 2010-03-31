package org.hibernate.mediator.stubs;

import org.hibernate.mediator.Messages;
import org.hibernate.metadata.CollectionMetadata;

public class CollectionMetadataStub {
	protected CollectionMetadata collectionMetadata;

	protected CollectionMetadataStub(Object collectionMetadata) {
		if (collectionMetadata == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.collectionMetadata = (CollectionMetadata)collectionMetadata;
	}

	public TypeStub getElementType() {
		return TypeStubFactory.createTypeStub(collectionMetadata.getElementType());
	}
}
