package org.hibernate.mediator.x.type;

public class CollectionType extends AbstractType {
	public static final String CL = "org.hibernate.type.CollectionType"; //$NON-NLS-1$

	protected CollectionType(Object collectionType) {
		super(collectionType, CL);
	}

	public String getRole() {
		return (String)invoke(mn());
	}

	public boolean isArrayType() {
		return (Boolean)invoke(mn());
	}

}
