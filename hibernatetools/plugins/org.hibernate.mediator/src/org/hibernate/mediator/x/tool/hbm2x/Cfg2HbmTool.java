package org.hibernate.mediator.x.tool.hbm2x;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.mapping.PersistentClass;
import org.hibernate.mediator.x.mapping.Property;

public class Cfg2HbmTool extends HObject {
	public static final String CL = "org.hibernate.tool.hbm2x.Cfg2HbmTool"; //$NON-NLS-1$

	protected Cfg2HbmTool(Object cfg2HbmTool) {
		super(cfg2HbmTool, CL);
	}
	
	public static Cfg2HbmTool newInstance() {
		return new Cfg2HbmTool(newInstance(CL));
	}

	public String getTag(PersistentClass persistentClass) {
		return (String)invoke(mn(), persistentClass);
	}

	public String getTag(Property property) {
		return (String)invoke(mn(), property);
	}
}
