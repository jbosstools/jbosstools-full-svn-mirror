package org.hibernate.mediator.x.mapping;


public class ManyToOne extends ToOne {
	public static final String CL = "org.hibernate.mapping.ManyToOne"; //$NON-NLS-1$

	protected ManyToOne(Object manyToOne) {
		super(manyToOne, CL);
	}
	
	public static ManyToOne newInstance(TableStub table) {
		return new ManyToOne(newInstance(CL, table));
	}

	public String getReferencedEntityName() {
		return (String)invoke(mn());
	}

}
