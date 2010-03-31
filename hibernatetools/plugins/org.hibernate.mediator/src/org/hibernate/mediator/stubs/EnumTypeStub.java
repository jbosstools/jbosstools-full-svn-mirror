package org.hibernate.mediator.stubs;

import org.hibernate.mediator.Messages;
import org.hibernate.type.EnumType;

public class EnumTypeStub {
	
	public static final String ENUM = EnumType.ENUM;
	public static final String TYPE = EnumType.TYPE;

	protected EnumType enumType;

	protected EnumTypeStub(Object enumType) {
		if (enumType == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.enumType = (EnumType)enumType;
	}
	
	public static String getClassName() {
		return EnumType.class.getName();
	}
}
