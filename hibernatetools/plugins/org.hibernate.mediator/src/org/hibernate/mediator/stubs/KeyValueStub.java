package org.hibernate.mediator.stubs;

public abstract class KeyValueStub extends ValueStub {
	public static final String CL = "org.hibernate.mapping.KeyValue"; //$NON-NLS-1$

	protected KeyValueStub(Object keyValue) {
		super(keyValue, CL);
	}

	protected KeyValueStub(Object keyValue, String cn) {
		super(keyValue, cn);
	}

	@Override
	public abstract Object accept(ValueVisitorStub visitor);

}
