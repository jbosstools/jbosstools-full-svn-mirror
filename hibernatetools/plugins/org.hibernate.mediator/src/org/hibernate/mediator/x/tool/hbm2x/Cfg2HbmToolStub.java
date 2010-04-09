package org.hibernate.mediator.x.tool.hbm2x;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.mapping.PersistentClassStub;
import org.hibernate.mediator.x.mapping.PropertyStub;

public class Cfg2HbmToolStub extends HObject {
	public static final String CL = "org.hibernate.tool.hbm2x.Cfg2HbmTool"; //$NON-NLS-1$

	protected Cfg2HbmToolStub(Object cfg2HbmTool) {
		super(cfg2HbmTool, CL);
	}
	
	public static Cfg2HbmToolStub newInstance() {
		return new Cfg2HbmToolStub(newInstance(CL));
	}

	public String getTag(PersistentClassStub persistentClass) {
		return (String)invoke(mn(), persistentClass);
	}

	public String getTag(PropertyStub property) {
		return (String)invoke(mn(), property);
	}
}
