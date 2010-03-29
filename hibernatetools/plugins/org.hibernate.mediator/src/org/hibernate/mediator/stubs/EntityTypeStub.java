package org.hibernate.mediator.stubs;

import org.hibernate.type.EntityType;

public class EntityTypeStub extends AbstractTypeStub {
	protected EntityType entityType;

	protected EntityTypeStub(Object entityType) {
		super(entityType);
		this.entityType = (EntityType)entityType;
	}

	public String getAssociatedEntityName() {
		return entityType.getAssociatedEntityName();
	}

	public boolean isOneToOne() {
		return entityType.isOneToOne();
	}
}
