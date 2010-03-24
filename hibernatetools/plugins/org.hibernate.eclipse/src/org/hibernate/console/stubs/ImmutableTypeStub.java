package org.hibernate.console.stubs;

import org.hibernate.type.ImmutableType;

public class ImmutableTypeStub extends NullableTypeStub {
	
	protected ImmutableType immutableType;

	protected ImmutableTypeStub(Object immutableType) {
		super(immutableType);
		this.immutableType = (ImmutableType)immutableType;
	}
}
