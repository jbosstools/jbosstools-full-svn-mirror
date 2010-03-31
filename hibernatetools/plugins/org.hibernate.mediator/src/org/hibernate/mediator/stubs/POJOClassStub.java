package org.hibernate.mediator.stubs;

import org.hibernate.mediator.Messages;
import org.hibernate.tool.hbm2x.pojo.POJOClass;

public class POJOClassStub {
	
	protected POJOClass pojoClass;

	protected POJOClassStub(Object pojoClass) {
		if (pojoClass == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.pojoClass = (POJOClass)pojoClass;
	}

	public String getQualifiedDeclarationName() {
		return pojoClass.getQualifiedDeclarationName();
	}

}
