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
		return (Class)invoke("getReturnedClass"); //$NON-NLS-1$
	}

	public boolean isCollectionType() {
		return (Boolean)invoke("isCollectionType"); //$NON-NLS-1$
	}

	public boolean isEntityType() {
		return (Boolean)invoke("isEntityType"); //$NON-NLS-1$
	}

	public boolean isAnyType() {
		return (Boolean)invoke("isAnyType"); //$NON-NLS-1$
	}

	public boolean isComponentType() {
		return (Boolean)invoke("isComponentType"); //$NON-NLS-1$
	}

	public String getName() {
		return (String)invoke("getName"); //$NON-NLS-1$
	}
}
