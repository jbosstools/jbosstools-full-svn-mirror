package org.hibernate.mediator.stubs;

import org.hibernate.mediator.Messages;
import org.hibernate.tool.hbm2x.HibernateMappingGlobalSettings;

public class HibernateMappingGlobalSettingsStub {
	public static final String CL = "org.hibernate.tool.hbm2x.HibernateMappingGlobalSettings"; //$NON-NLS-1$

	protected HibernateMappingGlobalSettings hibernateMappingGlobalSettings;

	protected HibernateMappingGlobalSettingsStub(Object hibernateMappingGlobalSettings) {
		if (hibernateMappingGlobalSettings == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.hibernateMappingGlobalSettings = (HibernateMappingGlobalSettings)hibernateMappingGlobalSettings;
	}
	
	public static HibernateMappingGlobalSettingsStub newInstance() {
		return new HibernateMappingGlobalSettingsStub(new HibernateMappingGlobalSettings());
	}

	public void setDefaultAccess(String string) {
		hibernateMappingGlobalSettings.setDefaultAccess(string);
	}
}
