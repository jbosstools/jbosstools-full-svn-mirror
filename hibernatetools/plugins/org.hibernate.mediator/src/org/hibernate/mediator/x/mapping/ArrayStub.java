package org.hibernate.mediator.x.mapping;

import org.hibernate.mediator.base.HObject;

public class ArrayStub extends ListStub {
	public static final String CL = "org.hibernate.mapping.Array"; //$NON-NLS-1$

	protected ArrayStub(Object array) {
		super(array, CL);
	}

	protected ArrayStub(Object array, String cn) {
		super(array, cn);
	}
	
	public static ArrayStub newInstance(PersistentClassStub persistentClass) {
		return new ArrayStub(HObject.newInstance(CL, persistentClass));
	}

	public void setElement(ValueStub element) {
		invoke(mn(), element);
	}

	public void setCollectionTable(TableStub table) {
		invoke(mn(), table);
	}

	public void setElementClassName(String elementClassName) {
		invoke(mn(), elementClassName);
	}

	public void setIndex(ValueStub index) {
		invoke(mn(), index);
	}

	public String getElementClassName() {
		return (String)invoke(mn());
	}
}
