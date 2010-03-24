package org.hibernate.console.stubs;

import org.hibernate.mapping.OneToOne;

public class OneToOneStub extends ToOneStub {
	protected OneToOne oneToOne;

	protected OneToOneStub(Object oneToOne) {
		super(oneToOne);
		this.oneToOne = (OneToOne)oneToOne;
	}
	
	public static OneToOneStub newInstance(TableStub table, PersistentClassStub owner) {
		return new OneToOneStub(new OneToOne(table.table, owner.persistentClass));
	}

	public String getEntityName() {
		return oneToOne.getEntityName();
	}

}
