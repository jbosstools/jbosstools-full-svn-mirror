package org.hibernate.mediator.x.mapping;


public class OneToManyStub extends ValueStub {
	public static final String CL = "org.hibernate.mapping.OneToMany"; //$NON-NLS-1$

	protected OneToManyStub(Object oneToMany) {
		super(oneToMany, CL);
	}
	
	public static OneToManyStub newInstance(PersistentClassStub owner) {
		return new OneToManyStub(newInstance(CL, owner));
	}

	public void setAssociatedClass(PersistentClassStub associatedClass) {
		invoke(mn(), associatedClass);
	}

	public void setReferencedEntityName(String referencedEntityName) {
		invoke(mn(), referencedEntityName);
	}

	public PersistentClassStub getAssociatedClass() {
		return PersistentClassStubFactory.createPersistentClassStub(invoke(mn()));
	}
	
	public Object accept(ValueVisitorStub visitor) {
		return visitor.accept(this);
	}

	public String getReferencedEntityName() {
		return (String)invoke(mn());
	}
}
