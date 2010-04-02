package org.hibernate.mediator.stubs;

public class SetStub extends CollectionStub {
	public static final String CL = "org.hibernate.mapping.Set"; //$NON-NLS-1$

	protected SetStub(Object set) {
		super(set, CL);
	}
	
	public static SetStub newInstance(PersistentClassStub owner) {
		return new SetStub(newInstance(CL, owner));
	}

	@Override
	public Object accept(ValueVisitorStub visitor) {
		return visitor.accept(this);
	}

}
