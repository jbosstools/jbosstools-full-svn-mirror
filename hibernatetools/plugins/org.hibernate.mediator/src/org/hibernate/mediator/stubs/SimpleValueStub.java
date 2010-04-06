package org.hibernate.mediator.stubs;

import java.util.Properties;

public class SimpleValueStub extends KeyValueStub {
	public static final String CL = "org.hibernate.mapping.SimpleValue"; //$NON-NLS-1$

	protected SimpleValueStub(Object simpleValue) {
		super(simpleValue, CL);
	}

	protected SimpleValueStub(Object simpleValue, String cn) {
		super(simpleValue, cn);
	}
	
	public static SimpleValueStub newInstance() {
		return new SimpleValueStub(newInstance(CL));
	}

	public void setTypeName(String type) {
		invoke(mn(), type);
	}

	public void addColumn(ColumnStub column) {
		invoke(mn(), column);
	}

	public void setTable(TableStub table) {
		invoke(mn(), table);
	}

	public void setTypeParameters(Properties parameterMap) {
		invoke(mn() ,parameterMap);
	}

	public String getForeignKeyName() {
		return (String)invoke(mn());
	}

	public String getTypeName() {
		return (String)invoke(mn());
	}

	public boolean isTypeSpecified() {
		return (Boolean)invoke(mn());
	}

	@Override
	public Object accept(ValueVisitorStub visitor) {
		return visitor.accept(this);
	}
}
