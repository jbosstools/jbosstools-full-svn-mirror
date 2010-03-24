package org.hibernate.console.stubs;

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
