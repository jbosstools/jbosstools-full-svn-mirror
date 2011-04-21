package org.hibernate.mediator.x.mapping;


public abstract class KeyValue extends Value {
	public static final String CL = "org.hibernate.mapping.KeyValue"; //$NON-NLS-1$

	protected KeyValue(Object keyValue) {
		super(keyValue, CL);
	}

	protected KeyValue(Object keyValue, String cn) {
		super(keyValue, cn);
	}

	@Override
	public abstract Object accept(ValueVisitor visitor);

}
