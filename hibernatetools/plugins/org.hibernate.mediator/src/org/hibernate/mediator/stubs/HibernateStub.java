package org.hibernate.mediator.stubs;

import org.hibernate.Hibernate;

public class HibernateStub {
	public static final NullableTypeStub BOOLEAN = new NullableTypeStub(Hibernate.BOOLEAN);
	public static final NullableTypeStub BYTE = new NullableTypeStub(Hibernate.BYTE);
	public static final NullableTypeStub BIG_INTEGER = new NullableTypeStub(Hibernate.BIG_INTEGER);
	public static final NullableTypeStub SHORT = new NullableTypeStub(Hibernate.SHORT);
	public static final NullableTypeStub CALENDAR = new NullableTypeStub(Hibernate.CALENDAR);
	public static final NullableTypeStub CALENDAR_DATE = new NullableTypeStub(Hibernate.CALENDAR_DATE);
	public static final NullableTypeStub INTEGER = new NullableTypeStub(Hibernate.INTEGER);
	public static final NullableTypeStub BIG_DECIMAL = new NullableTypeStub(Hibernate.BIG_DECIMAL);
	public static final NullableTypeStub CHARACTER = new NullableTypeStub(Hibernate.CHARACTER);
	public static final NullableTypeStub CLASS = new NullableTypeStub(Hibernate.CLASS);
	public static final NullableTypeStub CURRENCY = new NullableTypeStub(Hibernate.CURRENCY);
	public static final NullableTypeStub DATE = new NullableTypeStub(Hibernate.DATE);
	public static final NullableTypeStub DOUBLE = new NullableTypeStub(Hibernate.DOUBLE);
	public static final NullableTypeStub FLOAT = new NullableTypeStub(Hibernate.FLOAT);
	public static final NullableTypeStub LOCALE = new NullableTypeStub(Hibernate.LOCALE);
	public static final NullableTypeStub LONG = new NullableTypeStub(Hibernate.LONG);
	public static final NullableTypeStub STRING = new NullableTypeStub(Hibernate.STRING);
	public static final NullableTypeStub TEXT = new NullableTypeStub(Hibernate.TEXT);
	public static final NullableTypeStub TIME = new NullableTypeStub(Hibernate.TIME);
	public static final NullableTypeStub TIMESTAMP = new NullableTypeStub(Hibernate.TIMESTAMP);
	public static final NullableTypeStub TIMEZONE = new NullableTypeStub(Hibernate.TIMEZONE);
	public static final NullableTypeStub TRUE_FALSE = new NullableTypeStub(Hibernate.TRUE_FALSE);
	public static final NullableTypeStub YES_NO = new NullableTypeStub(Hibernate.YES_NO);

	protected Hibernate hibernate;

	protected HibernateStub(Object hibernate) {
		this.hibernate = (Hibernate)hibernate;
	}

	public static boolean isInitialized(Object proxy) {
		return Hibernate.isInitialized(proxy);
	}
}
