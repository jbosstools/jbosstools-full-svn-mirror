/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.console;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.hibernate.console.stubs.HibernateStub;
import org.hibernate.console.stubs.NullableTypeStub;
import org.hibernate.console.stubs.TableStub;
import org.hibernate.console.stubs.TypeStub;


public class ConsoleQueryParameter {

	static private final Object NULL_MARKER = null; //new Object() { public String toString() { return "[null]"; } };
	
	static final Map<TypeStub, String> typeFormats = new HashMap<TypeStub, String>();
	static {
		addTypeFormat(HibernateStub.BOOLEAN, Boolean.TRUE );
		addTypeFormat(HibernateStub.BYTE, Byte.valueOf((byte) 42));
		addTypeFormat(HibernateStub.BIG_INTEGER, BigInteger.valueOf(42));
		addTypeFormat(HibernateStub.SHORT, Short.valueOf((short) 42));
		addTypeFormat(HibernateStub.CALENDAR, new GregorianCalendar());
		addTypeFormat(HibernateStub.CALENDAR_DATE, new GregorianCalendar());
		addTypeFormat(HibernateStub.INTEGER, Integer.valueOf(42));
		addTypeFormat(HibernateStub.INTEGER, Integer.valueOf(42));
		addTypeFormat(HibernateStub.BIG_DECIMAL, new BigDecimal(42.0));
		addTypeFormat(HibernateStub.CHARACTER, Character.valueOf('h'));
		addTypeFormat(HibernateStub.CLASS, TableStub.class);
		addTypeFormat(HibernateStub.CURRENCY, Currency.getInstance(Locale.getDefault()));
		addTypeFormat(HibernateStub.DATE, new Date());
		addTypeFormat(HibernateStub.DOUBLE, Double.valueOf(42.42));
		addTypeFormat(HibernateStub.FLOAT, Float.valueOf((float)42.42));
		addTypeFormat(HibernateStub.LOCALE, Locale.getDefault());
		addTypeFormat(HibernateStub.LONG, Long.valueOf(42));
		addTypeFormat(HibernateStub.STRING, "a string"); //$NON-NLS-1$
		addTypeFormat(HibernateStub.TEXT, "a text"); //$NON-NLS-1$
		addTypeFormat(HibernateStub.TIME, new Date());
		addTypeFormat(HibernateStub.TIMESTAMP, new Date());
		addTypeFormat(HibernateStub.TIMEZONE, TimeZone.getDefault());
		addTypeFormat(HibernateStub.TRUE_FALSE, Boolean.TRUE);
		addTypeFormat(HibernateStub.YES_NO, Boolean.TRUE);
	}


	private static void addTypeFormat(NullableTypeStub nullableType, Object value) {
		typeFormats.put(nullableType, nullableType.toString(value));
	}
	String name;
	NullableTypeStub type;
	Object value;
	
	public ConsoleQueryParameter(ConsoleQueryParameter cqp) {
		name = cqp.name;
		type = cqp.type;
		value = cqp.value;
	}

	public ConsoleQueryParameter() {
		
	}

	public ConsoleQueryParameter(String name, NullableTypeStub type, Object value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public NullableTypeStub getType() {
		return type;
	}
	
	public void setType(NullableTypeStub type) {
		this.type = type;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		//if(value == null) { throw new IllegalArgumentException("Value must not be set to null"); }
		this.value = value;
	}
	
	public String getValueAsString() {
		if(isNull()) return ""; //$NON-NLS-1$
		return type.toString(getValue());
	}
	
	public void setValueFromString(String value) {
		try {
			Object object = type.fromStringValue(value);
			setValue(object);
		} catch(Exception he) {
			setNull();
		}
	}

	
	public String getDefaultFormat() {
		if(type!=null) {
			Object object = typeFormats.get(type);
			if(object!=null) {
				return object.toString();
			}
		}
		return "<unknown>";				 //$NON-NLS-1$
	}

	public static Set<TypeStub> getPossibleTypes() {
		return typeFormats.keySet();
	}

	public void setNull() {
		setValue( NULL_MARKER );
	}
	
	public boolean isNull() {
		return getValue()==NULL_MARKER;
	}

	public Object getValueForQuery() {
		if(isNull()) {
			return null;
		} else {
			return getValue();
		}	
	}
}
