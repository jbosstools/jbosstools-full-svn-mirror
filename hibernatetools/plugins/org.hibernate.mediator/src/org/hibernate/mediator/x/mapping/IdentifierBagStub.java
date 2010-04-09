package org.hibernate.mediator.x.mapping;


public class IdentifierBagStub extends IdentifierCollectionStub {
	public static final String CL = "org.hibernate.mapping.IdentifierBag"; //$NON-NLS-1$

	protected IdentifierBagStub(Object identifierBag) {
		super(identifierBag, CL);
	}

	@Override
	public Object accept(ValueVisitorStub visitor) {
		return visitor.accept(this);
	}

}
