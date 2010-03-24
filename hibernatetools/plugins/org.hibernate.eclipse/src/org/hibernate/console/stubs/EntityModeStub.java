package org.hibernate.console.stubs;

import org.hibernate.EntityMode;

public class EntityModeStub {

	public static final EntityModeStub POJO = new EntityModeStub(EntityMode.POJO);
	public static final EntityModeStub DOM4J = new EntityModeStub(EntityMode.DOM4J);
	public static final EntityModeStub MAP = new EntityModeStub(EntityMode.MAP);

	protected EntityMode entityMode;

	protected EntityModeStub(Object entityMode) {
		this.entityMode = (EntityMode)entityMode;
	}
}
