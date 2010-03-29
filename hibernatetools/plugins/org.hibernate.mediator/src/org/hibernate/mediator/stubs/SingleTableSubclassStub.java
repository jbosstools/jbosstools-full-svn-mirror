package org.hibernate.mediator.stubs;

import org.hibernate.mapping.SingleTableSubclass;

public class SingleTableSubclassStub extends SubclassStub {
	protected SingleTableSubclass singleTableSubclass;

	protected SingleTableSubclassStub(Object singleTableSubclass) {
		super(singleTableSubclass);
		this.singleTableSubclass = (SingleTableSubclass)singleTableSubclass;
	}

	public static SingleTableSubclassStub newInstance(Object singleTableSubclass) {
		return new SingleTableSubclassStub(singleTableSubclass);
	}
	
}
