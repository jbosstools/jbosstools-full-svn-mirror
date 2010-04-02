package org.hibernate.mediator.stubs;

import org.hibernate.engine.Mapping;
import org.hibernate.mediator.Messages;

public class MappingStub {
	public static final String CL = "org.hibernate.engine.Mapping"; //$NON-NLS-1$

	protected Mapping mapping;

	protected MappingStub(Object mapping) {
		if (mapping == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.mapping = (Mapping)mapping;
	}
}
