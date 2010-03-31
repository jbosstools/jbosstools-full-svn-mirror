package org.hibernate.mediator.stubs;

import org.hibernate.mapping.Formula;
import org.hibernate.mediator.Messages;

public class FormulaStub extends SelectableStub {
	
	protected Formula formula;

	protected FormulaStub(Object formula) {
		super(formula);
		if (formula == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.formula = (Formula)formula;
	}
}
