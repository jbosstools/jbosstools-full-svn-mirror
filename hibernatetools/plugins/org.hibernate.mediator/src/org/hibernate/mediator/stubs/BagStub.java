package org.hibernate.mediator.stubs;

public class BagStub extends CollectionStub {
	public static final String CL = "org.hibernate.mapping.Bag"; //$NON-NLS-1$

	protected BagStub(Object bag) {
		super(bag, CL);
	}
	
	public static BagStub newInstance(PersistentClassStub persistentClass) {
		return new BagStub(newInstance(CL, persistentClass));
	}

	@Override
	public Object accept(ValueVisitorStub visitor) {
		return visitor.accept(this);
	}
}
