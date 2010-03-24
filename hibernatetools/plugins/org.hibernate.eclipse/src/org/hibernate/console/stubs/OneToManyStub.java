package org.hibernate.console.stubs;

import org.hibernate.mapping.OneToMany;

public class OneToManyStub extends ValueStub {
	protected OneToMany oneToMany;

	protected OneToManyStub(Object oneToMany) {
		super(oneToMany);
		this.oneToMany = (OneToMany)oneToMany;
	}
	
	public static OneToManyStub newInstance(PersistentClassStub owner) {
		return new OneToManyStub(new OneToMany(owner.persistentClass));
	}

	public void setAssociatedClass(PersistentClassStub associatedClass) {
		oneToMany.setAssociatedClass(associatedClass.persistentClass);
	}

	public void setReferencedEntityName(String referencedEntityName) {
		oneToMany.setReferencedEntityName(referencedEntityName);
	}

	public PersistentClassStub getAssociatedClass() {
		return PersistentClassStubFactory.createPersistentClassStub(oneToMany.getAssociatedClass());
	}
	
	public Object accept(ValueVisitorStub visitor) {
		return visitor.accept(this);
	}

	public String getReferencedEntityName() {
		return oneToMany.getReferencedEntityName();
	}
}
