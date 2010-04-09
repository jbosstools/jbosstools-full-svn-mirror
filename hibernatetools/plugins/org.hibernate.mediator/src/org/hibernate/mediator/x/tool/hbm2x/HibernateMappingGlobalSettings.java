package org.hibernate.mediator.x.tool.hbm2x;

import org.hibernate.mediator.base.HObject;

public class HibernateMappingGlobalSettings extends HObject {
	public static final String CL = "org.hibernate.tool.hbm2x.HibernateMappingGlobalSettings"; //$NON-NLS-1$

	protected HibernateMappingGlobalSettings(Object hibernateMappingGlobalSettings) {
		super(hibernateMappingGlobalSettings, CL);
	}
	
	public static HibernateMappingGlobalSettings newInstance() {
		return new HibernateMappingGlobalSettings(HObject.newInstance(CL));
	}

	public void setDefaultAccess(String string) {
		invoke(mn(), string);
	}
}
