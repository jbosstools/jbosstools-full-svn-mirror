package org.hibernate.mediator.stubs;

import org.hibernate.mapping.Selectable;
import org.hibernate.mediator.Messages;

public class SelectableStub {
	
	protected Selectable selectable;

	protected SelectableStub(Object selectable) {
		if (selectable == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.selectable = (Selectable)selectable;
	}
}
