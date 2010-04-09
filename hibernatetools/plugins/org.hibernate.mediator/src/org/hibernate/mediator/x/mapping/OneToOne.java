package org.hibernate.mediator.x.mapping;


public class OneToOne extends ToOne {
	public static final String CL = "org.hibernate.mapping.OneToOne"; //$NON-NLS-1$

	protected OneToOne(Object oneToOne) {
		super(oneToOne, CL);
	}
	
	public static OneToOne newInstance(TableStub table, PersistentClass owner) {
		return new OneToOne(newInstance(CL, table, owner));
	}

	public String getEntityName() {
		return (String)invoke(mn());
	}

}
