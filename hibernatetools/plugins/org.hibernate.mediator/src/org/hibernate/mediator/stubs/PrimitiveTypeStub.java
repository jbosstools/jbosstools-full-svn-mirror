package org.hibernate.mediator.stubs;

public class PrimitiveTypeStub extends ImmutableTypeStub {
	public static final String CL = "org.hibernate.type.PrimitiveType"; //$NON-NLS-1$

	protected PrimitiveTypeStub(Object primitiveType) {
		super(primitiveType, CL);
	}

	protected PrimitiveTypeStub(Object primitiveType, String cn) {
		super(primitiveType, cn);
	}
}
