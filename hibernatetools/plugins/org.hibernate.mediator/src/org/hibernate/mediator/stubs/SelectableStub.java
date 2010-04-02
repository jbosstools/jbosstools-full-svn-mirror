package org.hibernate.mediator.stubs;

import org.hibernate.mapping.Selectable;
import org.hibernate.mediator.Messages;

public class SelectableStub {
	public static final String CL = "org.hibernate.mapping.Selectable"; //$NON-NLS-1$

	protected Selectable selectable;

	protected SelectableStub(Object selectable) {
		if (selectable == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.selectable = (Selectable)selectable;
	}
}
