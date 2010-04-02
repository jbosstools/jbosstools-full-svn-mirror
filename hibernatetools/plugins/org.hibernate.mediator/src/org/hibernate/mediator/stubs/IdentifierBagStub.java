package org.hibernate.mediator.stubs;

import org.hibernate.mapping.IdentifierBag;
import org.hibernate.mediator.Messages;

public class IdentifierBagStub extends IdentifierCollectionStub {
	public static final String CL = "org.hibernate.mapping.IdentifierBag"; //$NON-NLS-1$

	protected IdentifierBag identifierBag;

	protected IdentifierBagStub(Object identifierBag) {
		super(identifierBag);
		if (identifierBag == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.identifierBag = (IdentifierBag)identifierBag;
	}

	@Override
	public Object accept(ValueVisitorStub visitor) {
		return visitor.accept(this);
	}

}
