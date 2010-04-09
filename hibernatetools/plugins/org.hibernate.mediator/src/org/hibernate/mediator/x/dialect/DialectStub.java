package org.hibernate.mediator.x.dialect;

import java.sql.Connection;
import java.util.Properties;

import org.hibernate.mediator.HibernateConsoleRuntimeException;
import org.hibernate.mediator.base.HObject;

public class DialectStub extends HObject {
	public static final String CL = "org.hibernate.dialect.Dialect"; //$NON-NLS-1$

	protected DialectStub(Object dialect) {
		super(dialect, CL);
	}
	
	public static DialectStub newInstance(final String dialectName) {
		try {
			return new DialectStub(Class.forName(dialectName).newInstance());
		} catch (InstantiationException e) {
			throw new HibernateConsoleRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new HibernateConsoleRuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new HibernateConsoleRuntimeException(e);
		}
	}
	
	public static DialectStub newInstance(Properties properties, Connection connection) {
		Object dialect = invokeStaticMethod("org.hibernate.dialect.resolver.DialectFactory",  //$NON-NLS-1$
				"buildDialect", properties, connection); //$NON-NLS-1$
		return new DialectStub(dialect);
	}
	
	public String toString() {
		return Obj().toString();
	}
}
