package org.hibernate.mediator.x.metadata;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.type.Type;
import org.hibernate.mediator.x.type.TypeFactory;

public class CollectionMetadata extends HObject {
	public static final String CL = "org.hibernate.metadata.CollectionMetadata"; //$NON-NLS-1$

	public CollectionMetadata(Object collectionMetadata) {
		super(collectionMetadata, CL);
	}

	public Type getElementType() {
		return TypeFactory.createTypeStub(invoke(mn()));
	}
}
