package org.hibernate.console.stubs;

import org.hibernate.mapping.KeyValue;

public abstract class KeyValueStub extends ValueStub {
	protected KeyValue keyValue;

	protected KeyValueStub(Object keyValue) {
		super(keyValue);
		this.keyValue = (KeyValue)keyValue;
	}

	@Override
	public abstract Object accept(ValueVisitorStub visitor);

}
