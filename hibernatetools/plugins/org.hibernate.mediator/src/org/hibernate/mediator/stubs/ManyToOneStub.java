package org.hibernate.mediator.stubs;

import org.hibernate.mapping.ManyToOne;
import org.hibernate.mediator.Messages;

public class ManyToOneStub extends ToOneStub {
	public static final String CL = "org.hibernate.mapping.ManyToOne"; //$NON-NLS-1$

	protected ManyToOne manyToOne;

	protected ManyToOneStub(Object manyToOne) {
		super(manyToOne);
		if (manyToOne == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.manyToOne = (ManyToOne)manyToOne;
	}
	
	public static ManyToOneStub newInstance(TableStub table) {
		return new ManyToOneStub(new ManyToOne(table.table));
	}

	public String getReferencedEntityName() {
		return manyToOne.getReferencedEntityName();
	}

}
