package org.hibernate.mediator.x.mapping;


public class DependantValue extends SimpleValue {
	public static final String CL = "org.hibernate.mapping.DependantValue"; //$NON-NLS-1$

	protected DependantValue(Object dependantValue) {
		super(dependantValue, CL);
	}

}
