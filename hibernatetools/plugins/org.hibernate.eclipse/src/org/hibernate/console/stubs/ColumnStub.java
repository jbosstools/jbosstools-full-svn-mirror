package org.hibernate.console.stubs;

import org.hibernate.mapping.Column;

public class ColumnStub {
	public static final int DEFAULT_LENGTH = 255;
	public static final int DEFAULT_PRECISION = 19;
	public static final int DEFAULT_SCALE = 2;

	protected Column column;

	protected ColumnStub(Object column) {
		this.column = (Column)column;
	}
	
	public static ColumnStub newInstance() {
		return new ColumnStub(new Column());
	}
	
	public static ColumnStub newInstance(String columnName) {
		return new ColumnStub(new Column(columnName));
	}

	public String getSqlType() {
		return column.getSqlType();
	}

	public void setSqlType(String sqlType) {
		column.setSqlType(sqlType);
	}

	public String getSqlType(DialectStub dialect, MappingStub mapping) {
		return column.getSqlType(dialect.dialect, mapping.mapping);
	}

	public ValueStub getValue() {
		return ValueStubFactory.createValueStub(column.getValue());
	}

	public String getName() {
		return column.getName();
	}

	public boolean isNullable() {
		return column.isNullable();
	}

	public boolean isUnique() {
		return column.isUnique();
	}

	public Integer getSqlTypeCode() {
		return column.getSqlTypeCode();
	}

	public int getLength() {
		return column.getLength();
	}

	public int getPrecision() {
		return column.getPrecision();
	}

	public int getScale() {
		return column.getScale();
	}

}
