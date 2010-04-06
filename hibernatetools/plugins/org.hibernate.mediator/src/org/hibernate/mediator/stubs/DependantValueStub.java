package org.hibernate.mediator.stubs;

public class DependantValueStub extends SimpleValueStub {
	public static final String CL = "org.hibernate.mapping.DependantValue"; //$NON-NLS-1$

	protected DependantValueStub(Object dependantValue) {
		super(dependantValue, CL);
	}

}
