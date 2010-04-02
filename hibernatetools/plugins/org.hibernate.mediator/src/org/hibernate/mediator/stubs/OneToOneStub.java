package org.hibernate.mediator.stubs;

public class OneToOneStub extends ToOneStub {
	public static final String CL = "org.hibernate.mapping.OneToOne"; //$NON-NLS-1$

	protected OneToOneStub(Object oneToOne) {
		super(oneToOne, CL);
	}
	
	public static OneToOneStub newInstance(TableStub table, PersistentClassStub owner) {
		return new OneToOneStub(newInstance(CL, table, owner));
	}

	public String getEntityName() {
		return (String)invoke("getEntityName"); //$NON-NLS-1$
	}

}
