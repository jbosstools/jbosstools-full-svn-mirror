package org.hibernate.mediator.x.mapping;


public class IdentifierBag extends IdentifierCollection {
	public static final String CL = "org.hibernate.mapping.IdentifierBag"; //$NON-NLS-1$

	protected IdentifierBag(Object identifierBag) {
		super(identifierBag, CL);
	}

	@Override
	public Object accept(ValueVisitor visitor) {
		return visitor.accept(this);
	}

}
