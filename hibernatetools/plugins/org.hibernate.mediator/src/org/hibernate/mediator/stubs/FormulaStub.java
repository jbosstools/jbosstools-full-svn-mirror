package org.hibernate.mediator.stubs;

public class FormulaStub extends SelectableStub {
	public static final String CL = "org.hibernate.mapping.Formula"; //$NON-NLS-1$

	protected FormulaStub(Object formula) {
		super(formula, CL);
	}
}
