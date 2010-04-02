package org.hibernate.mediator.stubs;

import org.hibernate.mapping.DependantValue;
import org.hibernate.mediator.Messages;

public class DependantValueStub extends SimpleValueStub {
	public static final String CL = "org.hibernate.mapping.DependantValue"; //$NON-NLS-1$

	protected DependantValue dependantValue;

	protected DependantValueStub(Object dependantValue) {
		super(dependantValue);
		if (dependantValue == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.dependantValue = (DependantValue)dependantValue;
	}

}
