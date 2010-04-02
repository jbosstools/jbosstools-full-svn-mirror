package org.hibernate.mediator.stubs;

import org.hibernate.mediator.Messages;
import org.hibernate.type.PrimitiveType;

public class PrimitiveTypeStub extends ImmutableTypeStub {
	public static final String CL = "org.hibernate.type.PrimitiveType"; //$NON-NLS-1$

	protected PrimitiveType primitiveType;

	protected PrimitiveTypeStub(Object primitiveType) {
		super(primitiveType);
		if (primitiveType == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.primitiveType = (PrimitiveType)primitiveType;
	}
}
