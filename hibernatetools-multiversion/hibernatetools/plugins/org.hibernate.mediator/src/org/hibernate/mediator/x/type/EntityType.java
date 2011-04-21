package org.hibernate.mediator.x.type;


public class EntityType extends AbstractType {
	public static final String CL = "org.hibernate.type.EntityType"; //$NON-NLS-1$

	protected EntityType(Object entityType) {
		super(entityType, CL);
	}

	public String getAssociatedEntityName() {
		return (String)invoke(mn());
	}

	public boolean isOneToOne() {
		return (Boolean)invoke(mn());
	}
}
