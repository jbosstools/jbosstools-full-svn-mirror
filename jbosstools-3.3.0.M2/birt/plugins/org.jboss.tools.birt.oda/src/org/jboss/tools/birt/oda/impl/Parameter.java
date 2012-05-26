package org.jboss.tools.birt.oda.impl;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.type.Type;

public class Parameter {
	public static int StringType = 1;
	public static int IntegerType = 4;
	public static int DoubleType = 8;
	public static int BigDecimalType = 3;
	public static int DateType = 91;
	public static int TimeType = 92;
	public static int TimestampType = 93;
	public static int BooleanType = 16;
	
	private String name;
	private int type;
	private String typeName;
	private Object value;
	private static Map<Integer,Type>hibernateTypes = new HashMap<Integer,Type>();
	
	static {
		hibernateTypes.put(StringType, Hibernate.STRING);
		hibernateTypes.put(IntegerType, Hibernate.INTEGER);
		hibernateTypes.put(DoubleType, Hibernate.DOUBLE);
		hibernateTypes.put(BigDecimalType, Hibernate.BIG_DECIMAL);
		hibernateTypes.put(DateType, Hibernate.DATE);
		hibernateTypes.put(TimeType, Hibernate.TIME);
		hibernateTypes.put(TimestampType, Hibernate.TIMESTAMP);
		hibernateTypes.put(BooleanType, Hibernate.BOOLEAN);
	}
	
	public Parameter(int type, String name, Object value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Parameter other = (Parameter) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public Type getHibernateType() {
		return hibernateTypes.get(type);
	}
}
