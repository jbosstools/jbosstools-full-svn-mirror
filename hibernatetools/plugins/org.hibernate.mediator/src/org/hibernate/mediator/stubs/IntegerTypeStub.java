package org.hibernate.mediator.stubs;

public class IntegerTypeStub extends PrimitiveTypeStub {
	public static final String CL = "org.hibernate.type.IntegerType"; //$NON-NLS-1$

	protected IntegerTypeStub(Object integerType) {
		super(integerType, CL);
	}
}
