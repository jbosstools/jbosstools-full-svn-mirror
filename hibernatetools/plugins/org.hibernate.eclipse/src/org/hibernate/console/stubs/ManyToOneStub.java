package org.hibernate.console.stubs;

import org.hibernate.mapping.ManyToOne;

public class ManyToOneStub extends ToOneStub {
	protected ManyToOne manyToOne;

	protected ManyToOneStub(Object manyToOne) {
		super(manyToOne);
		this.manyToOne = (ManyToOne)manyToOne;
	}
	
	public static ManyToOneStub newInstance(TableStub table) {
		return new ManyToOneStub(new ManyToOne(table.table));
	}

	public String getReferencedEntityName() {
		return manyToOne.getReferencedEntityName();
	}

}
