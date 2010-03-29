package org.hibernate.mediator.stubs;

import org.hibernate.connection.ConnectionProvider;

public class ConnectionProviderStub {
	protected ConnectionProvider connectionProvider;

	protected ConnectionProviderStub(Object connectionProvider) {
		this.connectionProvider = (ConnectionProvider)connectionProvider;
	}

	public void close() {
		connectionProvider.close();
	}
}
