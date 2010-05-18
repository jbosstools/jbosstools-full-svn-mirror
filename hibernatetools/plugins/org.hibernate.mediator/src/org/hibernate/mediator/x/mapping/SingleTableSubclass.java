package org.hibernate.mediator.x.mapping;


public class SingleTableSubclass extends Subclass {
	public static final String CL = "org.hibernate.mapping.SingleTableSubclass"; //$NON-NLS-1$

	protected SingleTableSubclass(Object singleTableSubclass) {
		super(singleTableSubclass, CL);
	}
	
	public static SingleTableSubclass newInstance(PersistentClass superclass) {
		return new SingleTableSubclass(newInstance(CL, superclass));
	}
}
