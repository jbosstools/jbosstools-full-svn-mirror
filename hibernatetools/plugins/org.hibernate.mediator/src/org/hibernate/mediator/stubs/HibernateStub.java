package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public class HibernateStub extends HObject {
	public static final String CL = "org.hibernate.Hibernate"; //$NON-NLS-1$

	public static final NullableTypeStub BOOLEAN = new NullableTypeStub(readStaticFieldValue(CL, "BOOLEAN")); //$NON-NLS-1$
	public static final NullableTypeStub BYTE = new NullableTypeStub(readStaticFieldValue(CL, "BYTE")); //$NON-NLS-1$
	public static final NullableTypeStub BIG_INTEGER = new NullableTypeStub(readStaticFieldValue(CL, "BIG_INTEGER")); //$NON-NLS-1$
	public static final NullableTypeStub SHORT = new NullableTypeStub(readStaticFieldValue(CL, "SHORT")); //$NON-NLS-1$
	public static final NullableTypeStub CALENDAR = new NullableTypeStub(readStaticFieldValue(CL, "CALENDAR")); //$NON-NLS-1$
	public static final NullableTypeStub CALENDAR_DATE = new NullableTypeStub(readStaticFieldValue(CL, "CALENDAR_DATE")); //$NON-NLS-1$
	public static final NullableTypeStub INTEGER = new NullableTypeStub(readStaticFieldValue(CL, "INTEGER")); //$NON-NLS-1$
	public static final NullableTypeStub BIG_DECIMAL = new NullableTypeStub(readStaticFieldValue(CL, "BIG_DECIMAL")); //$NON-NLS-1$
	public static final NullableTypeStub CHARACTER = new NullableTypeStub(readStaticFieldValue(CL, "CHARACTER")); //$NON-NLS-1$
	public static final NullableTypeStub CLASS = new NullableTypeStub(readStaticFieldValue(CL, "CLASS")); //$NON-NLS-1$
	public static final NullableTypeStub CURRENCY = new NullableTypeStub(readStaticFieldValue(CL, "CURRENCY")); //$NON-NLS-1$
	public static final NullableTypeStub DATE = new NullableTypeStub(readStaticFieldValue(CL, "DATE")); //$NON-NLS-1$
	public static final NullableTypeStub DOUBLE = new NullableTypeStub(readStaticFieldValue(CL, "DOUBLE")); //$NON-NLS-1$
	public static final NullableTypeStub FLOAT = new NullableTypeStub(readStaticFieldValue(CL, "FLOAT")); //$NON-NLS-1$
	public static final NullableTypeStub LOCALE = new NullableTypeStub(readStaticFieldValue(CL, "LOCALE")); //$NON-NLS-1$
	public static final NullableTypeStub LONG = new NullableTypeStub(readStaticFieldValue(CL, "LONG")); //$NON-NLS-1$
	public static final NullableTypeStub STRING = new NullableTypeStub(readStaticFieldValue(CL, "STRING")); //$NON-NLS-1$
	public static final NullableTypeStub TEXT = new NullableTypeStub(readStaticFieldValue(CL, "TEXT")); //$NON-NLS-1$
	public static final NullableTypeStub TIME = new NullableTypeStub(readStaticFieldValue(CL, "TIME")); //$NON-NLS-1$
	public static final NullableTypeStub TIMESTAMP = new NullableTypeStub(readStaticFieldValue(CL, "TIMESTAMP")); //$NON-NLS-1$
	public static final NullableTypeStub TIMEZONE = new NullableTypeStub(readStaticFieldValue(CL, "TIMEZONE")); //$NON-NLS-1$
	public static final NullableTypeStub TRUE_FALSE = new NullableTypeStub(readStaticFieldValue(CL, "TRUE_FALSE")); //$NON-NLS-1$
	public static final NullableTypeStub YES_NO = new NullableTypeStub(readStaticFieldValue(CL, "YES_NO")); //$NON-NLS-1$

	protected HibernateStub(Object hibernate) {
		super(hibernate, CL);
	}

	public static boolean isInitialized(Object proxy) {
		Boolean isInitialized = (Boolean)invokeStaticMethod(CL, "isInitialized", proxy); //$NON-NLS-1$
		return isInitialized;
	}
}
