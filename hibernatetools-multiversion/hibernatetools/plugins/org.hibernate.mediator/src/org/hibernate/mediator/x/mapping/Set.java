package org.hibernate.mediator.x.mapping;


public class Set extends Collection {
	public static final String CL = "org.hibernate.mapping.Set"; //$NON-NLS-1$

	protected Set(Object set) {
		super(set, CL);
	}
	
	public static Set newInstance(PersistentClass owner) {
		return new Set(newInstance(CL, owner));
	}

	@Override
	public Object accept(ValueVisitor visitor) {
		return visitor.accept(this);
	}

}
