package org.hibernate.mediator.stubs;

import java.sql.Connection;
import java.util.Properties;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.resolver.DialectFactory;
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
	
	public static DialectStub newInstance(Properties properties, Connection connection) {
		return new DialectStub(DialectFactory.buildDialect(properties, connection));
	}
	
	public String toString() {
		return dialect.toString();
	}
}
