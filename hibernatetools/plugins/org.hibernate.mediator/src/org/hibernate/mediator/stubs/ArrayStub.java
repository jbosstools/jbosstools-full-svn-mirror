package org.hibernate.mediator.stubs;

import org.hibernate.mapping.Array;
import org.hibernate.mediator.Messages;

public class ArrayStub extends ListStub {
	public static final String CL = "org.hibernate.mapping.Array"; //$NON-NLS-1$

	protected Array array;

	protected ArrayStub(Object array) {
		super(array);
		if (array == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.array = (Array)array;
	}
	
	public static ArrayStub newInstance(PersistentClassStub owner) {
		return new ArrayStub(new Array(owner.persistentClass));
	}

	public void setElement(ValueStub element) {
		array.setElement(element.value);
	}

	public void setCollectionTable(TableStub table) {
		array.setCollectionTable(table.table);
	}

	public void setElementClassName(String elementClassName) {
		array.setElementClassName(elementClassName);
	}

	public void setIndex(ValueStub index) {
		array.setIndex(index.value);
	}

	public String getElementClassName() {
		return array.getElementClassName();
	}
}
