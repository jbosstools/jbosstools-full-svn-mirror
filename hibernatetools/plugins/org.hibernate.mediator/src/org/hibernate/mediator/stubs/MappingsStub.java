package org.hibernate.mediator.stubs;

import org.hibernate.cfg.Mappings;
import org.hibernate.mediator.Messages;

public class MappingsStub {
	protected Mappings mappings;

	protected MappingsStub(Object mappings) {
		if (mappings == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.mappings = (Mappings)mappings;
	}

	public void addClass(PersistentClassStub persistentClass) {
		mappings.addClass(persistentClass.getPersistentClass());
	}
}
