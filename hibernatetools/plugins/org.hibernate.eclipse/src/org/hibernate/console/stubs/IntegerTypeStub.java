package org.hibernate.console.stubs;

import org.hibernate.type.IntegerType;

public class IntegerTypeStub extends PrimitiveTypeStub {
	
	protected IntegerType integerType;

	protected IntegerTypeStub(Object integerType) {
		super(integerType);
		this.integerType = (IntegerType)integerType;
	}
}
