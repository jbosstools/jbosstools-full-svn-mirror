package org.hibernate.mediator.x.mapping;


public class MapStub extends IndexedCollection {
	public static final String CL = "org.hibernate.mapping.Map"; //$NON-NLS-1$

	protected MapStub(Object map) {
		super(map, CL);
	}
	
	public static MapStub newInstance(PersistentClass owner) {
		return new MapStub(newInstance(CL, owner));
	}

	@Override
	public Object accept(ValueVisitor visitor) {
		return visitor.accept(this);
	}
}
