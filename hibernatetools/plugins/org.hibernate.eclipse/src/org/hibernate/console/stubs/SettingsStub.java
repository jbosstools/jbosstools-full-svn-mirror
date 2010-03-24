package org.hibernate.console.stubs;

import org.hibernate.cfg.Settings;

public class SettingsStub {
	protected Settings settings;

	protected SettingsStub(Object settings) {
		this.settings = (Settings)settings;
	}

	public ConnectionProviderStub getConnectionProvider() {
		return new ConnectionProviderStub(settings.getConnectionProvider());
	}

	public String getDefaultCatalogName() {
		return settings.getDefaultCatalogName();
	}

	public String getDefaultSchemaName() {
		return settings.getDefaultSchemaName();
	}
}
