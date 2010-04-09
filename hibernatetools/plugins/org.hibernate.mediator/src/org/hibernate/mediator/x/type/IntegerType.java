package org.hibernate.mediator.x.type;


public class IntegerType extends PrimitiveType {
	public static final String CL = "org.hibernate.type.IntegerType"; //$NON-NLS-1$

	protected IntegerType(Object integerType) {
		super(integerType, CL);
	}
}
