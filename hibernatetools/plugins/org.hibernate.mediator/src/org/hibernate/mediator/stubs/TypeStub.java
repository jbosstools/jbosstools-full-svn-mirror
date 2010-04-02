package org.hibernate.mediator.stubs;

import org.hibernate.mediator.Messages;
import org.hibernate.type.Type;

public abstract class TypeStub {
	public static final String CL = "org.hibernate.type.Type"; //$NON-NLS-1$

	protected Type type;

	protected TypeStub(Object type) {
		if (type == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.type = (Type)type;
	}

	@SuppressWarnings("unchecked")
	public Class getReturnedClass() {
		return type.getReturnedClass();
	}

	public boolean isCollectionType() {
		return type.isCollectionType();
	}

	public boolean isEntityType() {
		return type.isEntityType();
	}

	public boolean isAnyType() {
		return type.isAnyType();
	}

	public boolean isComponentType() {
		return type.isComponentType();
	}

	public String getName() {
		return type.getName();
	}
}
