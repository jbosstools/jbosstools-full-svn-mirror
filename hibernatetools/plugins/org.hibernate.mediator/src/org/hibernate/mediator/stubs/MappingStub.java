package org.hibernate.mediator.stubs;

import org.hibernate.engine.Mapping;

public class MappingStub {
	protected Mapping mapping;

	protected MappingStub(Object mapping) {
		this.mapping = (Mapping)mapping;
	}
}
