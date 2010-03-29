package org.hibernate.mediator.stubs;

import org.hibernate.type.EnumType;

public class EnumTypeStub {
	
	public static final String ENUM = EnumType.ENUM;
	public static final String TYPE = EnumType.TYPE;

	protected EnumType enumType;

	protected EnumTypeStub(Object enumType) {
		this.enumType = (EnumType)enumType;
	}
	
	public static String getClassName() {
		return EnumType.class.getName();
	}
}
