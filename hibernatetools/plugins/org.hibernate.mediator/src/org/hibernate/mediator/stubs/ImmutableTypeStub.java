package org.hibernate.mediator.stubs;

import org.hibernate.mediator.Messages;
import org.hibernate.type.ImmutableType;

public class ImmutableTypeStub extends NullableTypeStub {
	public static final String CL = "org.hibernate.type.ImmutableType"; //$NON-NLS-1$

	protected ImmutableType immutableType;

	protected ImmutableTypeStub(Object immutableType) {
		super(immutableType);
		if (immutableType == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.immutableType = (ImmutableType)immutableType;
	}
}
