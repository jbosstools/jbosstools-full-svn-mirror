package org.hibernate.mediator.stubs;

public class MapStub extends IndexedCollectionStub {
	public static final String CL = "org.hibernate.mapping.Map"; //$NON-NLS-1$

	protected MapStub(Object map) {
		super(map, CL);
	}
	
	public static MapStub newInstance(PersistentClassStub owner) {
		return new MapStub(newInstance(CL, owner));
	}

	@Override
	public Object accept(ValueVisitorStub visitor) {
		return visitor.accept(this);
	}
}
