package org.hibernate.mediator.x.cfg;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.connection.ConnectionProvider;

public class Settings extends HObject {
	public static final String CL = "org.hibernate.cfg.Settings"; //$NON-NLS-1$

	protected Settings(Object settings) {
		super(settings, CL);
	}

	public ConnectionProvider getConnectionProvider() {
		return new ConnectionProvider(invoke(mn()));
	}

	public String getDefaultCatalogName() {
		return (String)invoke(mn());
	}

	public String getDefaultSchemaName() {
		return (String)invoke(mn());
	}
}
