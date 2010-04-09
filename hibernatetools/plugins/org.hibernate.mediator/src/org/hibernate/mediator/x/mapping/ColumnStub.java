package org.hibernate.mediator.x.mapping;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.dialect.DialectStub;
import org.hibernate.mediator.x.engine.MappingStub;

public class ColumnStub extends SelectableStub {
	public static final String CL = "org.hibernate.mapping.Column"; //$NON-NLS-1$

	public static final int DEFAULT_LENGTH = 255;
	public static final int DEFAULT_PRECISION = 19;
	public static final int DEFAULT_SCALE = 2;

	protected ColumnStub(Object column) {
		super(column, CL);
	}
	
	public static ColumnStub newInstance() {
		return new ColumnStub(HObject.newInstance(CL));
	}
	
	public static ColumnStub newInstance(String columnName) {
		return new ColumnStub(HObject.newInstance(CL, columnName));
	}

	public String getSqlType() {
		return (String)invoke(mn());
	}

	public void setSqlType(String sqlType) {
		invoke(mn(), sqlType);
	}

	public String getSqlType(DialectStub dialect, MappingStub mapping) {
		return (String)invoke(mn(), dialect, mapping);
	}

	public ValueStub getValue() {
		return ValueStubFactory.createValueStub(invoke(mn()));
	}

	public String getName() {
		return (String)invoke(mn());
	}

	public boolean isNullable() {
		return (Boolean)invoke(mn());
	}

	public boolean isUnique() {
		return (Boolean)invoke(mn());
	}

	public Integer getSqlTypeCode() {
		return (Integer)invoke(mn());
	}

	public int getLength() {
		return (Integer)invoke(mn());
	}

	public int getPrecision() {
		return (Integer)invoke(mn());
	}

	public int getScale() {
		return (Integer)invoke(mn());
	}

}
