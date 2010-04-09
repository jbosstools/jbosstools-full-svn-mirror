package org.hibernate.mediator.x.tool.hbm2x;

import org.hibernate.mediator.base.HObject;

public class HibernateMappingGlobalSettingsStub extends HObject {
	public static final String CL = "org.hibernate.tool.hbm2x.HibernateMappingGlobalSettings"; //$NON-NLS-1$

	protected HibernateMappingGlobalSettingsStub(Object hibernateMappingGlobalSettings) {
		super(hibernateMappingGlobalSettings, CL);
	}
	
	public static HibernateMappingGlobalSettingsStub newInstance() {
		return new HibernateMappingGlobalSettingsStub(HObject.newInstance(CL));
	}

	public void setDefaultAccess(String string) {
		invoke(mn(), string);
	}
}
