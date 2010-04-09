package org.hibernate.mediator.x.type;

import org.hibernate.mediator.base.HObject;

public abstract class Type extends HObject {
	public static final String CL = "org.hibernate.type.Type"; //$NON-NLS-1$

	protected Type(Object type) {
		super(type, CL);
	}

	protected Type(Object type, String cn) {
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
