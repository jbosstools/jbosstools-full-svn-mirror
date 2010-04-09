package org.hibernate.mediator.x;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.type.NullableType;
import org.hibernate.mediator.x.type.TypeFactory;

public class Hibernate extends HObject {
	public static final String CL = "org.hibernate.Hibernate"; //$NON-NLS-1$

	public static final NullableType BOOLEAN = TypeFactory.createNTS(readStaticFieldValue(CL, "BOOLEAN")); //$NON-NLS-1$
	public static final NullableType BYTE = TypeFactory.createNTS(readStaticFieldValue(CL, "BYTE")); //$NON-NLS-1$
	public static final NullableType BIG_INTEGER = TypeFactory.createNTS(readStaticFieldValue(CL, "BIG_INTEGER")); //$NON-NLS-1$
	public static final NullableType SHORT = TypeFactory.createNTS(readStaticFieldValue(CL, "SHORT")); //$NON-NLS-1$
	public static final NullableType CALENDAR = TypeFactory.createNTS(readStaticFieldValue(CL, "CALENDAR")); //$NON-NLS-1$
	public static final NullableType CALENDAR_DATE = TypeFactory.createNTS(readStaticFieldValue(CL, "CALENDAR_DATE")); //$NON-NLS-1$
	public static final NullableType INTEGER = TypeFactory.createNTS(readStaticFieldValue(CL, "INTEGER")); //$NON-NLS-1$
	public static final NullableType BIG_DECIMAL = TypeFactory.createNTS(readStaticFieldValue(CL, "BIG_DECIMAL")); //$NON-NLS-1$
	public static final NullableType CHARACTER = TypeFactory.createNTS(readStaticFieldValue(CL, "CHARACTER")); //$NON-NLS-1$
	public static final NullableType CLASS = TypeFactory.createNTS(readStaticFieldValue(CL, "CLASS")); //$NON-NLS-1$
	public static final NullableType CURRENCY = TypeFactory.createNTS(readStaticFieldValue(CL, "CURRENCY")); //$NON-NLS-1$
	public static final NullableType DATE = TypeFactory.createNTS(readStaticFieldValue(CL, "DATE")); //$NON-NLS-1$
	public static final NullableType DOUBLE = TypeFactory.createNTS(readStaticFieldValue(CL, "DOUBLE")); //$NON-NLS-1$
	public static final NullableType FLOAT = TypeFactory.createNTS(readStaticFieldValue(CL, "FLOAT")); //$NON-NLS-1$
	public static final NullableType LOCALE = TypeFactory.createNTS(readStaticFieldValue(CL, "LOCALE")); //$NON-NLS-1$
	public static final NullableType LONG = TypeFactory.createNTS(readStaticFieldValue(CL, "LONG")); //$NON-NLS-1$
	public static final NullableType STRING = TypeFactory.createNTS(readStaticFieldValue(CL, "STRING")); //$NON-NLS-1$
	public static final NullableType TEXT = TypeFactory.createNTS(readStaticFieldValue(CL, "TEXT")); //$NON-NLS-1$
	public static final NullableType TIME = TypeFactory.createNTS(readStaticFieldValue(CL, "TIME")); //$NON-NLS-1$
	public static final NullableType TIMESTAMP = TypeFactory.createNTS(readStaticFieldValue(CL, "TIMESTAMP")); //$NON-NLS-1$
	public static final NullableType TIMEZONE = TypeFactory.createNTS(readStaticFieldValue(CL, "TIMEZONE")); //$NON-NLS-1$
	public static final NullableType TRUE_FALSE = TypeFactory.createNTS(readStaticFieldValue(CL, "TRUE_FALSE")); //$NON-NLS-1$
	public static final NullableType YES_NO = TypeFactory.createNTS(readStaticFieldValue(CL, "YES_NO")); //$NON-NLS-1$

	protected Hibernate(Object hibernate) {
		super(hibernate, CL);
	}

	public static boolean isInitialized(Object proxy) {
		Boolean isInitialized = (Boolean)invokeStaticMethod(CL, "isInitialized", proxy); //$NON-NLS-1$
		return isInitialized;
	}
}
