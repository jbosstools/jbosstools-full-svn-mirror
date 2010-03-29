package org.hibernate.mediator.stubs;

import org.hibernate.dialect.Dialect;

public class DialectStub {
	protected Dialect dialect;

	protected DialectStub(Object dialect) {
		this.dialect = (Dialect)dialect;
	}
	
	public static DialectStub newInstance(final String dialectName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return new DialectStub(Class.forName(dialectName).newInstance());
	}
}
