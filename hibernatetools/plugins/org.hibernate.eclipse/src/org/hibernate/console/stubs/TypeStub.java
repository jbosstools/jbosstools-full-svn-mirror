package org.hibernate.console.stubs;

import org.hibernate.type.Type;

public abstract class TypeStub {
	protected Type type;

	protected TypeStub(Object type) {
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
