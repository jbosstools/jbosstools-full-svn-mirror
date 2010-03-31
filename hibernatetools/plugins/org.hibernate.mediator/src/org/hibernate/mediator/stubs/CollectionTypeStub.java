package org.hibernate.mediator.stubs;

import org.hibernate.mediator.Messages;
import org.hibernate.type.CollectionType;

public class CollectionTypeStub extends AbstractTypeStub {
	
	protected CollectionType collectionType;

	protected CollectionTypeStub(Object collectionType) {
		super(collectionType);
		if (collectionType == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.collectionType = (CollectionType)collectionType;
	}

	public String getRole() {
		return collectionType.getRole();
	}

	public boolean isArrayType() {
		return collectionType.isArrayType();
	}

}
