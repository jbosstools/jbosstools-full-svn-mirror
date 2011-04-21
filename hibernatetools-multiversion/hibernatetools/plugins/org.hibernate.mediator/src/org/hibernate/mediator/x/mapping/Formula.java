package org.hibernate.mediator.x.mapping;


public class Formula extends Selectable {
	public static final String CL = "org.hibernate.mapping.Formula"; //$NON-NLS-1$

	protected Formula(Object formula) {
		super(formula, CL);
	}
}
