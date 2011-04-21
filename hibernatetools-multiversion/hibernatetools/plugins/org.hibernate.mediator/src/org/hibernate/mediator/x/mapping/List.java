package org.hibernate.mediator.x.mapping;


public class List extends IndexedCollection {
	public static final String CL = "org.hibernate.mapping.List"; //$NON-NLS-1$

	protected List(Object list) {
		super(list, CL);
	}

	protected List(Object list, String cn) {
		super(list, cn);
	}
	
	public static List newInstance(PersistentClass owner) {
		return new List(newInstance(CL, owner));
	}

	@Override
	public Object accept(ValueVisitor visitor) {
		return visitor.accept(this);
	}

	public Value getIndex() {
		return ValueFactory.createValueStub(invoke(mn()));
	}

}
