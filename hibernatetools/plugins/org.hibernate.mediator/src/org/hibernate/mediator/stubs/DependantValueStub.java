package org.hibernate.mediator.stubs;

import org.hibernate.mapping.DependantValue;

public class DependantValueStub extends SimpleValueStub {
	protected DependantValue dependantValue;

	protected DependantValueStub(Object dependantValue) {
		super(dependantValue);
		this.dependantValue = (DependantValue)dependantValue;
	}

}
