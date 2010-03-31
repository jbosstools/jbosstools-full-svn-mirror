package org.hibernate.mediator.stubs;

import org.hibernate.mapping.Any;
import org.hibernate.mediator.Messages;

public class AnyStub extends SimpleValueStub {
	protected Any any;

	protected AnyStub(Object any) {
		super(any);
		if (any == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.any = (Any)any;
	}

}
