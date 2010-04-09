package org.hibernate.mediator.x.mapping;


public class PrimitiveArrayStub extends ArrayStub {
	public static final String CL = "org.hibernate.mapping.PrimitiveArray"; //$NON-NLS-1$

	protected PrimitiveArrayStub(Object primitiveArray) {
		super(primitiveArray, CL);
	}
	
	public static PrimitiveArrayStub newInstance(PersistentClassStub owner) {
		return new PrimitiveArrayStub(newInstance(CL, owner));
	}

	public void setElement(ValueStub element) {
		invoke(mn(), element);
	}

}
