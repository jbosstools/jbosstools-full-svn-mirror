package org.hibernate.console.stubs;

import org.hibernate.type.AbstractType;

public class AbstractTypeStub extends TypeStub {
	protected AbstractType abstractType;

	protected AbstractTypeStub(Object abstractType) {
		super(abstractType);
		this.abstractType = (AbstractType)abstractType;
	}
}
