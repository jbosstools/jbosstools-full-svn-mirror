package org.hibernate.mediator.x.mapping;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.dialect.Dialect;
import org.hibernate.mediator.x.engine.Mapping;

public class Column extends Selectable {
	public static final String CL = "org.hibernate.mapping.Column"; //$NON-NLS-1$

	public static final int DEFAULT_LENGTH = 255;
	public static final int DEFAULT_PRECISION = 19;
	public static final int DEFAULT_SCALE = 2;

	protected Column(Object column) {
		super(column, CL);
	}
	
	public static Column newInstance() {
		return new Column(HObject.newInstance(CL));
	}
	
	public static Column newInstance(String columnName) {
		return new Column(HObject.newInstance(CL, columnName));
	}

	public String getSqlType() {
		return (String)invoke(mn());
	}

	public void setSqlType(String sqlType) {
		invoke(mn(), sqlType);
	}

	public String getSqlType(Dialect dialect, Mapping mapping) {
		return (String)invoke(mn(), dialect, mapping);
	}

	public Value getValue() {
		return ValueFactory.createValueStub(invoke(mn()));
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
