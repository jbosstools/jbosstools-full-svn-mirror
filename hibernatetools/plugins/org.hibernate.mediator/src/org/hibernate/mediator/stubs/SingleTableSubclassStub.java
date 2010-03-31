package org.hibernate.mediator.stubs;

import org.hibernate.mapping.SingleTableSubclass;
import org.hibernate.mediator.Messages;

public class SingleTableSubclassStub extends SubclassStub {
	protected SingleTableSubclass singleTableSubclass;

	protected SingleTableSubclassStub(Object singleTableSubclass) {
		super(singleTableSubclass);
		if (singleTableSubclass == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.singleTableSubclass = (SingleTableSubclass)singleTableSubclass;
	}

	public static SingleTableSubclassStub newInstance(Object singleTableSubclass) {
		if (singleTableSubclass == null) {
			return null;
		}
		return new SingleTableSubclassStub(singleTableSubclass);
	}
	
}
