package org.hibernate.mediator.stubs;

public class OneToManyStub extends ValueStub {
	public static final String CL = "org.hibernate.mapping.OneToMany"; //$NON-NLS-1$

	protected OneToManyStub(Object oneToMany) {
		super(oneToMany, CL);
	}
	
	public static OneToManyStub newInstance(PersistentClassStub owner) {
		return new OneToManyStub(newInstance(CL, owner));
	}

	public void setAssociatedClass(PersistentClassStub associatedClass) {
		invoke("setAssociatedClass", associatedClass); //$NON-NLS-1$
	}

	public void setReferencedEntityName(String referencedEntityName) {
		invoke("setReferencedEntityName", referencedEntityName); //$NON-NLS-1$
	}

	public PersistentClassStub getAssociatedClass() {
		return PersistentClassStubFactory.createPersistentClassStub(invoke("getAssociatedClass")); //$NON-NLS-1$
	}
	
	public Object accept(ValueVisitorStub visitor) {
		return visitor.accept(this);
	}

	public String getReferencedEntityName() {
		return (String)invoke("getReferencedEntityName"); //$NON-NLS-1$
	}
}
