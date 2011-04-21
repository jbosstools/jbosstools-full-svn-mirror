package org.hibernate.mediator.x.mapping;


public class PrimitiveArray extends Array {
	public static final String CL = "org.hibernate.mapping.PrimitiveArray"; //$NON-NLS-1$

	protected PrimitiveArray(Object primitiveArray) {
		super(primitiveArray, CL);
	}
	
	public static PrimitiveArray newInstance(PersistentClass owner) {
		return new PrimitiveArray(newInstance(CL, owner));
	}

	public void setElement(Value element) {
		invoke(mn(), element);
	}

}
