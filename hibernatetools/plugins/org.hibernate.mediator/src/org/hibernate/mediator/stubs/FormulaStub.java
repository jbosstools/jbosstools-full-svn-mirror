package org.hibernate.mediator.stubs;

import org.hibernate.mapping.Formula;
import org.hibernate.mediator.Messages;

public class FormulaStub extends SelectableStub {
	public static final String CL = "org.hibernate.mapping.Formula"; //$NON-NLS-1$

	protected Formula formula;

	protected FormulaStub(Object formula) {
		super(formula);
		if (formula == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.formula = (Formula)formula;
	}
}
