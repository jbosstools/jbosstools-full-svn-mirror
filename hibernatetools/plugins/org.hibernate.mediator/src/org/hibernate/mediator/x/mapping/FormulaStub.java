package org.hibernate.mediator.x.mapping;


public class FormulaStub extends SelectableStub {
	public static final String CL = "org.hibernate.mapping.Formula"; //$NON-NLS-1$

	protected FormulaStub(Object formula) {
		super(formula, CL);
	}
}
