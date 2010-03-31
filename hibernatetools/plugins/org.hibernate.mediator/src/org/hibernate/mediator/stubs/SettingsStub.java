package org.hibernate.mediator.stubs;

import org.hibernate.cfg.Settings;
import org.hibernate.mediator.Messages;

public class SettingsStub {
	protected Settings settings;

	protected SettingsStub(Object settings) {
		if (settings == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
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
