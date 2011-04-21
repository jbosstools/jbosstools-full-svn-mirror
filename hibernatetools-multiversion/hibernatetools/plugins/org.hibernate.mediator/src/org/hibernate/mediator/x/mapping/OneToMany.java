package org.hibernate.mediator.x.mapping;


public class OneToMany extends Value {
	public static final String CL = "org.hibernate.mapping.OneToMany"; //$NON-NLS-1$

	protected OneToMany(Object oneToMany) {
		super(oneToMany, CL);
	}
	
	public static OneToMany newInstance(PersistentClass owner) {
		return new OneToMany(newInstance(CL, owner));
	}

	public void setAssociatedClass(PersistentClass associatedClass) {
		invoke(mn(), associatedClass);
	}

	public void setReferencedEntityName(String referencedEntityName) {
		invoke(mn(), referencedEntityName);
	}

	public PersistentClass getAssociatedClass() {
		return PersistentClassFactory.createPersistentClassStub(invoke(mn()));
	}
	
	public Object accept(ValueVisitor visitor) {
		return visitor.accept(this);
	}

	public String getReferencedEntityName() {
		return (String)invoke(mn());
	}
}
