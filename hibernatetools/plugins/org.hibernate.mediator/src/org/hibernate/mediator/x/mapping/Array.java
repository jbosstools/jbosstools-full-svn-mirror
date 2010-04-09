package org.hibernate.mediator.x.mapping;

import org.hibernate.mediator.base.HObject;

public class Array extends ListStub {
	public static final String CL = "org.hibernate.mapping.Array"; //$NON-NLS-1$

	protected Array(Object array) {
		super(array, CL);
	}

	protected Array(Object array, String cn) {
		super(array, cn);
	}
	
	public static Array newInstance(PersistentClass persistentClass) {
		return new Array(HObject.newInstance(CL, persistentClass));
	}

	public void setElement(Value element) {
		invoke(mn(), element);
	}

	public void setCollectionTable(TableStub table) {
		invoke(mn(), table);
	}

	public void setElementClassName(String elementClassName) {
		invoke(mn(), elementClassName);
	}

	public void setIndex(Value index) {
		invoke(mn(), index);
	}

	public String getElementClassName() {
		return (String)invoke(mn());
	}
}
