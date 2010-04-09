package org.hibernate.mediator.x.mapping;


public class Bag extends CollectionStub {
	public static final String CL = "org.hibernate.mapping.Bag"; //$NON-NLS-1$

	protected Bag(Object bag) {
		super(bag, CL);
	}
	
	public static Bag newInstance(PersistentClass persistentClass) {
		return new Bag(newInstance(CL, persistentClass));
	}

	@Override
	public Object accept(ValueVisitor visitor) {
		return visitor.accept(this);
	}
}
