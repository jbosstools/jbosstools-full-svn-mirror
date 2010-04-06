package org.hibernate.mediator.stubs;

public class CollectionTypeStub extends AbstractTypeStub {
	public static final String CL = "org.hibernate.type.CollectionType"; //$NON-NLS-1$

	protected CollectionTypeStub(Object collectionType) {
		super(collectionType, CL);
	}

	public String getRole() {
		return (String)invoke(mn());
	}

	public boolean isArrayType() {
		return (Boolean)invoke(mn());
	}

}
