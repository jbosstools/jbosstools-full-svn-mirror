package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public class CollectionMetadataStub extends HObject {
	public static final String CL = "org.hibernate.metadata.CollectionMetadata"; //$NON-NLS-1$

	protected CollectionMetadataStub(Object collectionMetadata) {
		super(collectionMetadata, CL);
	}

	public TypeStub getElementType() {
		return TypeStubFactory.createTypeStub(invoke(mn()));
	}
}
