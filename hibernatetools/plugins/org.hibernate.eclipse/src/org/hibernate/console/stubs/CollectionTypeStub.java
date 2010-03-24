package org.hibernate.console.stubs;

import org.hibernate.type.CollectionType;

public class CollectionTypeStub extends AbstractTypeStub {
	
	protected CollectionType collectionType;

	protected CollectionTypeStub(Object collectionType) {
		super(collectionType);
		this.collectionType = (CollectionType)collectionType;
	}

	public String getRole() {
		return collectionType.getRole();
	}

	public boolean isArrayType() {
		return collectionType.isArrayType();
	}

}
