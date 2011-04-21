package org.hibernate.mediator.x.type;

public class PrimitiveType extends ImmutableType {
	public static final String CL = "org.hibernate.type.PrimitiveType"; //$NON-NLS-1$

	protected PrimitiveType(Object primitiveType) {
		super(primitiveType, CL);
	}

	protected PrimitiveType(Object primitiveType, String cn) {
		super(primitiveType, cn);
	}
}
