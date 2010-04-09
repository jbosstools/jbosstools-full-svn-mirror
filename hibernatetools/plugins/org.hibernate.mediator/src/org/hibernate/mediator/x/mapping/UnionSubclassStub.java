package org.hibernate.mediator.x.mapping;


public class UnionSubclassStub extends SubclassStub {
	public static final String CL = "org.hibernate.mapping.UnionSubclass"; //$NON-NLS-1$

	protected UnionSubclassStub(Object unionSubclass) {
		super(unionSubclass, CL);
	}
}
