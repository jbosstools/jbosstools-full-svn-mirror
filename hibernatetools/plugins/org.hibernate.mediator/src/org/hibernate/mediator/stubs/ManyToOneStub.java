package org.hibernate.mediator.stubs;

public class ManyToOneStub extends ToOneStub {
	public static final String CL = "org.hibernate.mapping.ManyToOne"; //$NON-NLS-1$

	protected ManyToOneStub(Object manyToOne) {
		super(manyToOne, CL);
	}
	
	public static ManyToOneStub newInstance(TableStub table) {
		return new ManyToOneStub(newInstance(CL, table));
	}

	public String getReferencedEntityName() {
		return (String)invoke("getReferencedEntityName"); //$NON-NLS-1$
	}

}
