package org.hibernate.mediator.stubs;

public class ImmutableTypeStub extends NullableTypeStub {
	public static final String CL = "org.hibernate.type.ImmutableType"; //$NON-NLS-1$

	protected ImmutableTypeStub(Object immutableType) {
		super(immutableType, CL);
	}

	protected ImmutableTypeStub(Object immutableType, String cn) {
		super(immutableType, cn);
	}
}
