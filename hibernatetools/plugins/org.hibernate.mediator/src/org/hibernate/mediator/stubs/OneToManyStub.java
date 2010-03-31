package org.hibernate.mediator.stubs;

import org.hibernate.mapping.OneToMany;
import org.hibernate.mediator.Messages;

public class OneToManyStub extends ValueStub {
	protected OneToMany oneToMany;

	protected OneToManyStub(Object oneToMany) {
		super(oneToMany);
		if (oneToMany == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
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
