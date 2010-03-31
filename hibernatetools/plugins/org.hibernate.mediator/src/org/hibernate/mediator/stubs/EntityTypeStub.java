package org.hibernate.mediator.stubs;

import org.hibernate.mediator.Messages;
import org.hibernate.type.EntityType;

public class EntityTypeStub extends AbstractTypeStub {
	protected EntityType entityType;

	protected EntityTypeStub(Object entityType) {
		super(entityType);
		if (entityType == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.entityType = (EntityType)entityType;
	}

	public String getAssociatedEntityName() {
		return entityType.getAssociatedEntityName();
	}

	public boolean isOneToOne() {
		return entityType.isOneToOne();
	}
}
