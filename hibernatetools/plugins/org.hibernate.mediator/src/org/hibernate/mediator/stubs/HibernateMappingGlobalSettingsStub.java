package org.hibernate.mediator.stubs;

import org.hibernate.tool.hbm2x.HibernateMappingGlobalSettings;

public class HibernateMappingGlobalSettingsStub {
	
	protected HibernateMappingGlobalSettings hibernateMappingGlobalSettings;

	protected HibernateMappingGlobalSettingsStub(Object hibernateMappingGlobalSettings) {
		this.hibernateMappingGlobalSettings = (HibernateMappingGlobalSettings)hibernateMappingGlobalSettings;
	}
	
	public static HibernateMappingGlobalSettingsStub newInstance() {
		return new HibernateMappingGlobalSettingsStub(new HibernateMappingGlobalSettings());
	}

	public void setDefaultAccess(String string) {
		// TODO Auto-generated method stub
		
	}
}
