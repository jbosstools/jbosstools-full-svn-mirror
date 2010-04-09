package org.hibernate.mediator.x.type;


public class AbstractType extends Type {
	public static final String CL = "org.hibernate.type.AbstractType"; //$NON-NLS-1$

	protected AbstractType(Object abstractType) {
		super(abstractType, CL);
	}

	protected AbstractType(Object abstractType, String cn) {
		super(abstractType, cn);
	}
}
