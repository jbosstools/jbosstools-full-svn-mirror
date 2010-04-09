package org.hibernate.mediator.x;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.type.NullableTypeStub;
import org.hibernate.mediator.x.type.TypeStubFactory;

public class HibernateStub extends HObject {
	public static final String CL = "org.hibernate.Hibernate"; //$NON-NLS-1$

	public static final NullableTypeStub BOOLEAN = TypeStubFactory.createNTS(readStaticFieldValue(CL, "BOOLEAN")); //$NON-NLS-1$
	public static final NullableTypeStub BYTE = TypeStubFactory.createNTS(readStaticFieldValue(CL, "BYTE")); //$NON-NLS-1$
	public static final NullableTypeStub BIG_INTEGER = TypeStubFactory.createNTS(readStaticFieldValue(CL, "BIG_INTEGER")); //$NON-NLS-1$
	public static final NullableTypeStub SHORT = TypeStubFactory.createNTS(readStaticFieldValue(CL, "SHORT")); //$NON-NLS-1$
	public static final NullableTypeStub CALENDAR = TypeStubFactory.createNTS(readStaticFieldValue(CL, "CALENDAR")); //$NON-NLS-1$
	public static final NullableTypeStub CALENDAR_DATE = TypeStubFactory.createNTS(readStaticFieldValue(CL, "CALENDAR_DATE")); //$NON-NLS-1$
	public static final NullableTypeStub INTEGER = TypeStubFactory.createNTS(readStaticFieldValue(CL, "INTEGER")); //$NON-NLS-1$
	public static final NullableTypeStub BIG_DECIMAL = TypeStubFactory.createNTS(readStaticFieldValue(CL, "BIG_DECIMAL")); //$NON-NLS-1$
	public static final NullableTypeStub CHARACTER = TypeStubFactory.createNTS(readStaticFieldValue(CL, "CHARACTER")); //$NON-NLS-1$
	public static final NullableTypeStub CLASS = TypeStubFactory.createNTS(readStaticFieldValue(CL, "CLASS")); //$NON-NLS-1$
	public static final NullableTypeStub CURRENCY = TypeStubFactory.createNTS(readStaticFieldValue(CL, "CURRENCY")); //$NON-NLS-1$
	public static final NullableTypeStub DATE = TypeStubFactory.createNTS(readStaticFieldValue(CL, "DATE")); //$NON-NLS-1$
	public static final NullableTypeStub DOUBLE = TypeStubFactory.createNTS(readStaticFieldValue(CL, "DOUBLE")); //$NON-NLS-1$
	public static final NullableTypeStub FLOAT = TypeStubFactory.createNTS(readStaticFieldValue(CL, "FLOAT")); //$NON-NLS-1$
	public static final NullableTypeStub LOCALE = TypeStubFactory.createNTS(readStaticFieldValue(CL, "LOCALE")); //$NON-NLS-1$
	public static final NullableTypeStub LONG = TypeStubFactory.createNTS(readStaticFieldValue(CL, "LONG")); //$NON-NLS-1$
	public static final NullableTypeStub STRING = TypeStubFactory.createNTS(readStaticFieldValue(CL, "STRING")); //$NON-NLS-1$
	public static final NullableTypeStub TEXT = TypeStubFactory.createNTS(readStaticFieldValue(CL, "TEXT")); //$NON-NLS-1$
	public static final NullableTypeStub TIME = TypeStubFactory.createNTS(readStaticFieldValue(CL, "TIME")); //$NON-NLS-1$
	public static final NullableTypeStub TIMESTAMP = TypeStubFactory.createNTS(readStaticFieldValue(CL, "TIMESTAMP")); //$NON-NLS-1$
	public static final NullableTypeStub TIMEZONE = TypeStubFactory.createNTS(readStaticFieldValue(CL, "TIMEZONE")); //$NON-NLS-1$
	public static final NullableTypeStub TRUE_FALSE = TypeStubFactory.createNTS(readStaticFieldValue(CL, "TRUE_FALSE")); //$NON-NLS-1$
	public static final NullableTypeStub YES_NO = TypeStubFactory.createNTS(readStaticFieldValue(CL, "YES_NO")); //$NON-NLS-1$

	protected HibernateStub(Object hibernate) {
		super(hibernate, CL);
	}

	public static boolean isInitialized(Object proxy) {
		Boolean isInitialized = (Boolean)invokeStaticMethod(CL, "isInitialized", proxy); //$NON-NLS-1$
		return isInitialized;
	}
}
