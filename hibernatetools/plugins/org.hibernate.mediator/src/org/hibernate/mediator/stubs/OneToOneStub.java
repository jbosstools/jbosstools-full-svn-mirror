package org.hibernate.mediator.stubs;

import org.hibernate.mapping.OneToOne;
import org.hibernate.mediator.Messages;

public class OneToOneStub extends ToOneStub {
	public static final String CL = "org.hibernate.mapping.OneToOne"; //$NON-NLS-1$

	protected OneToOne oneToOne;

	protected OneToOneStub(Object oneToOne) {
		super(oneToOne);
		if (oneToOne == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.oneToOne = (OneToOne)oneToOne;
	}
	
	public static OneToOneStub newInstance(TableStub table, PersistentClassStub owner) {
		return new OneToOneStub(new OneToOne(table.table, owner.persistentClass));
	}

	public String getEntityName() {
		return oneToOne.getEntityName();
	}

}
