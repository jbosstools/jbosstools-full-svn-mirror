package org.hibernate.mediator.x.tool.hbm2x.pojo;

import org.hibernate.mediator.base.HObject;

public class POJOClass extends HObject {
	public static final String CL = "org.hibernate.tool.hbm2x.pojo.POJOClass"; //$NON-NLS-1$

	protected POJOClass(Object pojoClass) {
		super(pojoClass, CL);
	}

	public String getQualifiedDeclarationName() {
		return (String)invoke(mn());
	}

}
