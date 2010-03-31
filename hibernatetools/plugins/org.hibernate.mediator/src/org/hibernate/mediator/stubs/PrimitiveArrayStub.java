package org.hibernate.mediator.stubs;

import org.hibernate.mapping.PrimitiveArray;
import org.hibernate.mediator.Messages;

public class PrimitiveArrayStub extends ArrayStub {
	protected PrimitiveArray primitiveArray;

	protected PrimitiveArrayStub(Object primitiveArray) {
		super(primitiveArray);
		if (primitiveArray == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.primitiveArray = (PrimitiveArray)primitiveArray;
	}
	
	public static PrimitiveArrayStub newInstance(PersistentClassStub owner) {
		return new PrimitiveArrayStub(new PrimitiveArray(owner.persistentClass));
	}

	public void setElement(ValueStub element) {
		primitiveArray.setElement(element.value);
	}

}
