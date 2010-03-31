package org.hibernate.mediator.stubs;

import org.hibernate.mediator.Messages;
import org.hibernate.type.IntegerType;

public class IntegerTypeStub extends PrimitiveTypeStub {
	
	protected IntegerType integerType;

	protected IntegerTypeStub(Object integerType) {
		super(integerType);
		if (integerType == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.integerType = (IntegerType)integerType;
	}
}
