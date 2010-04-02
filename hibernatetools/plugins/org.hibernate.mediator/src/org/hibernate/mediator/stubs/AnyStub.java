package org.hibernate.mediator.stubs;

import org.hibernate.mapping.Any;
import org.hibernate.mediator.Messages;

public class AnyStub extends SimpleValueStub {
	public static final String CL = "org.hibernate.mapping.Any"; //$NON-NLS-1$

	protected Any any;

	protected AnyStub(Object any) {
		super(any);
		if (any == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.any = (Any)any;
	}

}
