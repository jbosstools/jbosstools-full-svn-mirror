package org.hibernate.mediator.stubs;

import org.hibernate.mediator.Messages;
import org.hibernate.type.AbstractType;

public class AbstractTypeStub extends TypeStub {
	protected AbstractType abstractType;

	protected AbstractTypeStub(Object abstractType) {
		super(abstractType);
		if (abstractType == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.abstractType = (AbstractType)abstractType;
	}
}
