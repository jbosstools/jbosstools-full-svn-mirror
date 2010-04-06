package org.hibernate.mediator.stubs;

public class SingleTableSubclassStub extends SubclassStub {
	public static final String CL = "org.hibernate.mapping.SingleTableSubclass"; //$NON-NLS-1$

	protected SingleTableSubclassStub(Object singleTableSubclass) {
		super(singleTableSubclass, CL);
	}
}
