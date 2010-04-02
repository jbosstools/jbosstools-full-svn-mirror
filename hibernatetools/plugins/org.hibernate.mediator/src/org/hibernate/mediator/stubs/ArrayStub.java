package org.hibernate.mediator.stubs;

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
		invoke("setElement", element); //$NON-NLS-1$
	}

	public void setCollectionTable(TableStub table) {
		invoke("setCollectionTable", table); //$NON-NLS-1$
	}

	public void setElementClassName(String elementClassName) {
		invoke("setElementClassName", elementClassName); //$NON-NLS-1$
	}

	public void setIndex(ValueStub index) {
		invoke("setIndex", index); //$NON-NLS-1$
	}

	public String getElementClassName() {
		return (String)invoke("getElementClassName"); //$NON-NLS-1$
	}
}
