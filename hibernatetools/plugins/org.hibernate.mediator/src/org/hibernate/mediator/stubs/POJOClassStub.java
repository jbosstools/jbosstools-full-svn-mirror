package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public class POJOClassStub extends HObject {
	public static final String CL = "org.hibernate.tool.hbm2x.pojo.POJOClass"; //$NON-NLS-1$

	protected POJOClassStub(Object pojoClass) {
		super(pojoClass, CL);
	}

	public String getQualifiedDeclarationName() {
		return (String)invoke(mn());
	}

}
