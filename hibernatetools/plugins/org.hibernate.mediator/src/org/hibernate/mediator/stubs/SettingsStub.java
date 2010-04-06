package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public class SettingsStub extends HObject {
	public static final String CL = "org.hibernate.cfg.Settings"; //$NON-NLS-1$

	protected SettingsStub(Object settings) {
		super(settings, CL);
	}

	public ConnectionProviderStub getConnectionProvider() {
		return new ConnectionProviderStub(invoke(mn()));
	}

	public String getDefaultCatalogName() {
		return (String)invoke(mn());
	}

	public String getDefaultSchemaName() {
		return (String)invoke(mn());
	}
}
