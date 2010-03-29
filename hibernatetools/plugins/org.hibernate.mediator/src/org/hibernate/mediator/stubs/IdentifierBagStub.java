package org.hibernate.mediator.stubs;

import org.hibernate.mapping.IdentifierBag;

public class IdentifierBagStub extends IdentifierCollectionStub {
	protected IdentifierBag identifierBag;

	protected IdentifierBagStub(Object identifierBag) {
		super(identifierBag);
		this.identifierBag = (IdentifierBag)identifierBag;
	}

	@Override
	public Object accept(ValueVisitorStub visitor) {
		return visitor.accept(this);
	}

}
