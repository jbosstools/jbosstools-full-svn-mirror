package org.hibernate.mediator.stubs;

import org.hibernate.metadata.CollectionMetadata;

public class CollectionMetadataStub {
	protected CollectionMetadata collectionMetadata;

	protected CollectionMetadataStub(Object collectionMetadata) {
		this.collectionMetadata = (CollectionMetadata)collectionMetadata;
	}

	public TypeStub getElementType() {
		return TypeStubFactory.createTypeStub(collectionMetadata.getElementType());
	}
}
