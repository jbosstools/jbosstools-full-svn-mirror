package org.hibernate.mediator.stubs;

public class AbstractTypeStub extends TypeStub {
	public static final String CL = "org.hibernate.type.AbstractType"; //$NON-NLS-1$

	protected AbstractTypeStub(Object abstractType) {
		super(abstractType, CL);
	}

	protected AbstractTypeStub(Object abstractType, String cn) {
		super(abstractType, cn);
	}
}
