package org.hibernate.mediator.stubs;

import org.hibernate.EntityMode;
import org.hibernate.mediator.Messages;

public class EntityModeStub {

	public static final EntityModeStub POJO = new EntityModeStub(EntityMode.POJO);
	public static final EntityModeStub DOM4J = new EntityModeStub(EntityMode.DOM4J);
	public static final EntityModeStub MAP = new EntityModeStub(EntityMode.MAP);

	protected EntityMode entityMode;

	protected EntityModeStub(Object entityMode) {
		if (entityMode == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.entityMode = (EntityMode)entityMode;
	}
}
