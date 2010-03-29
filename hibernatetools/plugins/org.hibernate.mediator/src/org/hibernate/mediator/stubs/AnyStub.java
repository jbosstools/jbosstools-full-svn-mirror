package org.hibernate.mediator.stubs;

import org.hibernate.mapping.Any;

public class AnyStub extends SimpleValueStub {
	protected Any any;

	protected AnyStub(Object any) {
		super(any);
		this.any = (Any)any;
	}

}
