package org.hibernate.mediator.stubs;

import org.hibernate.mediator.Messages;
import org.hibernate.type.NullableType;

public class NullableTypeStub extends AbstractTypeStub {
	
	protected NullableType nullableType;

	protected NullableTypeStub(Object nullableType) {
		super(nullableType);
		if (nullableType == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.nullableType = (NullableType)nullableType;
	}

	public String toString(Object value) {
		return nullableType.toString(value);
	}

	public Object fromStringValue(String xml) {
		return nullableType.fromStringValue(xml);
	}
}
