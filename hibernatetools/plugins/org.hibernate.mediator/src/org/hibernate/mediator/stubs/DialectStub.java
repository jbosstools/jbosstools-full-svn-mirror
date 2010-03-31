package org.hibernate.mediator.stubs;

import org.hibernate.dialect.Dialect;
import org.hibernate.mediator.Messages;

public class DialectStub {
	protected Dialect dialect;

	protected DialectStub(Object dialect) {
		if (dialect == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.dialect = (Dialect)dialect;
	}
	
	public static DialectStub newInstance(final String dialectName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return new DialectStub(Class.forName(dialectName).newInstance());
	}
}
