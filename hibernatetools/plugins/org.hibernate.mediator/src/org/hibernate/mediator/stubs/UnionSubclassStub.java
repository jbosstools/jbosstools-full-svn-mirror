package org.hibernate.mediator.stubs;

import org.hibernate.mapping.UnionSubclass;
import org.hibernate.mediator.Messages;

public class UnionSubclassStub extends SubclassStub {
	protected UnionSubclass unionSubclass;

	protected UnionSubclassStub(Object unionSubclass) {
		super(unionSubclass);
		if (unionSubclass == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.unionSubclass = (UnionSubclass)unionSubclass;
	}
}
