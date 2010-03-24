package org.hibernate.console.stubs;

import org.hibernate.type.PrimitiveType;

public class PrimitiveTypeStub extends ImmutableTypeStub {
	
	protected PrimitiveType primitiveType;

	protected PrimitiveTypeStub(Object primitiveType) {
		super(primitiveType);
		this.primitiveType = (PrimitiveType)primitiveType;
	}
}
