package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public class Cfg2HbmToolStub extends HObject {
	public static final String CL = "org.hibernate.tool.hbm2x.Cfg2HbmTool"; //$NON-NLS-1$

	protected Cfg2HbmToolStub(Object cfg2HbmTool) {
		super(cfg2HbmTool, CL);
	}
	
	public static Cfg2HbmToolStub newInstance() {
		return new Cfg2HbmToolStub(newInstance(CL));
	}

	public String getTag(PersistentClassStub persistentClass) {
		return (String)invoke("getTag", persistentClass); //$NON-NLS-1$
	}

	public String getTag(PropertyStub property) {
		return (String)invoke("getTag", property); //$NON-NLS-1$
	}
}
