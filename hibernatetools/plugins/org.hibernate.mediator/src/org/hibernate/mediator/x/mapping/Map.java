package org.hibernate.mediator.x.mapping;


public class Map extends IndexedCollection {
	public static final String CL = "org.hibernate.mapping.Map"; //$NON-NLS-1$

	protected Map(Object map) {
		super(map, CL);
	}
	
	public static Map newInstance(PersistentClass owner) {
		return new Map(newInstance(CL, owner));
	}

	@Override
	public Object accept(ValueVisitor visitor) {
		return visitor.accept(this);
	}
}
