package org.hibernate.mediator.stubs;

import org.hibernate.mapping.Set;
import org.hibernate.mediator.Messages;

public class SetStub extends CollectionStub {
	public static final String CL = "org.hibernate.mapping.Set"; //$NON-NLS-1$

	protected Set set;

	protected SetStub(Object set) {
		super(set);
		if (set == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.set = (Set)set;
	}
	
	public static SetStub newInstance(PersistentClassStub owner) {
		return new SetStub(new Set(owner.persistentClass));
	}

	@Override
	public Object accept(ValueVisitorStub visitor) {
		return visitor.accept(this);
	}

}
