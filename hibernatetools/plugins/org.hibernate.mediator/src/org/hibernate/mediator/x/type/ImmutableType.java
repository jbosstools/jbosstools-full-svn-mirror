package org.hibernate.mediator.x.type;

public class ImmutableType extends NullableType {
	public static final String CL = "org.hibernate.type.ImmutableType"; //$NON-NLS-1$

	protected ImmutableType(Object immutableType) {
		super(immutableType, CL);
	}

	protected ImmutableType(Object immutableType, String cn) {
		super(immutableType, cn);
	}
}
