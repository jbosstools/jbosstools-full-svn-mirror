package org.hibernate.mediator.x.connection;

import org.hibernate.mediator.base.HObject;

public class ConnectionProviderStub extends HObject {
	public static final String CL = "org.hibernate.connection.ConnectionProvider"; //$NON-NLS-1$

	public ConnectionProviderStub(Object connectionProvider) {
		super(connectionProvider, CL);
	}

	public void close() {
		invoke(mn());
	}
}
