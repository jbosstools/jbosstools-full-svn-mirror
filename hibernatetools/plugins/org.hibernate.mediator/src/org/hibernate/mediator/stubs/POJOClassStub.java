package org.hibernate.mediator.stubs;

import org.hibernate.tool.hbm2x.pojo.POJOClass;

public class POJOClassStub {
	
	protected POJOClass pojoClass;

	protected POJOClassStub(Object pojoClass) {
		this.pojoClass = (POJOClass)pojoClass;
	}

	public String getQualifiedDeclarationName() {
		return pojoClass.getQualifiedDeclarationName();
	}

}
