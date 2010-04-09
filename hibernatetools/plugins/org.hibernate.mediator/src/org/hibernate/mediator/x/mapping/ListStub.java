package org.hibernate.mediator.x.mapping;


public class ListStub extends IndexedCollection {
	public static final String CL = "org.hibernate.mapping.List"; //$NON-NLS-1$

	protected ListStub(Object list) {
		super(list, CL);
	}

	protected ListStub(Object list, String cn) {
		super(list, cn);
	}
	
	public static ListStub newInstance(PersistentClass owner) {
		return new ListStub(newInstance(CL, owner));
	}

	@Override
	public Object accept(ValueVisitor visitor) {
		return visitor.accept(this);
	}

	public Value getIndex() {
		return ValueFactory.createValueStub(invoke(mn()));
	}

}
