package org.hibernate.mediator.stubs;

import org.hibernate.connection.ConnectionProvider;
import org.hibernate.mediator.Messages;

public class ConnectionProviderStub {
	public static final String CL = "org.hibernate.connection.ConnectionProvider"; //$NON-NLS-1$

	protected ConnectionProvider connectionProvider;

	protected ConnectionProviderStub(Object connectionProvider) {
		if (connectionProvider == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.connectionProvider = (ConnectionProvider)connectionProvider;
	}

	public void close() {
		connectionProvider.close();
	}
}
