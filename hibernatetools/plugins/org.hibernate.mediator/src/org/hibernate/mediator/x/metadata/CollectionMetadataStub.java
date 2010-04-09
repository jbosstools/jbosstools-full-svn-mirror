package org.hibernate.mediator.x.metadata;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.type.TypeStub;
import org.hibernate.mediator.x.type.TypeStubFactory;

public class CollectionMetadataStub extends HObject {
	public static final String CL = "org.hibernate.metadata.CollectionMetadata"; //$NON-NLS-1$

	public CollectionMetadataStub(Object collectionMetadata) {
		super(collectionMetadata, CL);
	}

	public TypeStub getElementType() {
		return TypeStubFactory.createTypeStub(invoke(mn()));
	}
}
