package org.hibernate.mediator.stubs;

import org.hibernate.connection.ConnectionProvider;
import org.hibernate.mediator.Messages;

public class ConnectionProviderStub {
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
