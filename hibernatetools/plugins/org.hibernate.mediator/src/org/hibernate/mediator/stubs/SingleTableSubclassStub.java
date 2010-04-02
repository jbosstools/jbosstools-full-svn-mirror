package org.hibernate.mediator.stubs;

import org.hibernate.mapping.SingleTableSubclass;
import org.hibernate.mediator.Messages;

public class SingleTableSubclassStub extends SubclassStub {
	public static final String CL = "org.hibernate.mapping.SingleTableSubclass"; //$NON-NLS-1$

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
