package org.hibernate.mediator.stubs;

public class ListStub extends IndexedCollectionStub {
	public static final String CL = "org.hibernate.mapping.List"; //$NON-NLS-1$

	protected ListStub(Object list) {
		super(list, CL);
	}

	protected ListStub(Object list, String cn) {
		super(list, cn);
	}
	
	public static ListStub newInstance(PersistentClassStub owner) {
		return new ListStub(newInstance(CL, owner));
	}

	@Override
	public Object accept(ValueVisitorStub visitor) {
		return visitor.accept(this);
	}

	public ValueStub getIndex() {
		return ValueStubFactory.createValueStub(invoke("getIndex")); //$NON-NLS-1$
	}

}
