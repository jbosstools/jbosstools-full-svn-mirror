package org.hibernate.mediator.x.mapping;


public class AnyStub extends SimpleValueStub {
	public static final String CL = "org.hibernate.mapping.Any"; //$NON-NLS-1$

	protected AnyStub(Object any) {
		super(any, CL);
	}

}
