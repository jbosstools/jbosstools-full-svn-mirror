package org.hibernate.mediator.stubs;

import org.hibernate.mapping.PrimitiveArray;

public class PrimitiveArrayStub extends ArrayStub {
	protected PrimitiveArray primitiveArray;

	protected PrimitiveArrayStub(Object primitiveArray) {
		super(primitiveArray);
		this.primitiveArray = (PrimitiveArray)primitiveArray;
	}
	
	public static PrimitiveArrayStub newInstance(PersistentClassStub owner) {
		return new PrimitiveArrayStub(new PrimitiveArray(owner.persistentClass));
	}

	public void setElement(ValueStub element) {
		primitiveArray.setElement(element.value);
	}

}
