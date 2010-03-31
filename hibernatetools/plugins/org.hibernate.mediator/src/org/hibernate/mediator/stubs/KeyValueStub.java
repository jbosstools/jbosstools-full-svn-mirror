package org.hibernate.mediator.stubs;

import org.hibernate.mapping.KeyValue;
import org.hibernate.mediator.Messages;

public abstract class KeyValueStub extends ValueStub {
	protected KeyValue keyValue;

	protected KeyValueStub(Object keyValue) {
		super(keyValue);
		if (keyValue == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.keyValue = (KeyValue)keyValue;
	}

	@Override
	public abstract Object accept(ValueVisitorStub visitor);

}
