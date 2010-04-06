package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public abstract class TypeStub extends HObject {
	public static final String CL = "org.hibernate.type.Type"; //$NON-NLS-1$

	protected TypeStub(Object type) {
		super(type, CL);
	}

	protected TypeStub(Object type, String cn) {
		super(type, cn);
	}

	@SuppressWarnings("unchecked")
	public Class getReturnedClass() {
		return (Class)invoke(mn());
	}

	public boolean isCollectionType() {
		return (Boolean)invoke(mn());
	}

	public boolean isEntityType() {
		return (Boolean)invoke(mn());
	}

	public boolean isAnyType() {
		return (Boolean)invoke(mn());
	}

	public boolean isComponentType() {
		return (Boolean)invoke(mn());
	}

	public String getName() {
		return (String)invoke(mn());
	}
}
