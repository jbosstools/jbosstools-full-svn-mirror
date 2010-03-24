package org.hibernate.console.stubs;

import org.hibernate.cfg.Mappings;

public class MappingsStub {
	protected Mappings mappings;

	protected MappingsStub(Object mappings) {
		this.mappings = (Mappings)mappings;
	}

	public void addClass(PersistentClassStub persistentClass) {
		mappings.addClass(persistentClass.getPersistentClass());
	}
}
