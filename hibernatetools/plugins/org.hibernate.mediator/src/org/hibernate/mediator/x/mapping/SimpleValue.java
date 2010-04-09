package org.hibernate.mediator.x.mapping;

import java.util.Properties;


public class SimpleValue extends KeyValue {
	public static final String CL = "org.hibernate.mapping.SimpleValue"; //$NON-NLS-1$

	protected SimpleValue(Object simpleValue) {
		super(simpleValue, CL);
	}

	protected SimpleValue(Object simpleValue, String cn) {
		super(simpleValue, cn);
	}
	
	public static SimpleValue newInstance() {
		return new SimpleValue(newInstance(CL));
	}

	public void setTypeName(String type) {
		invoke(mn(), type);
	}

	public void addColumn(Column column) {
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
	public Object accept(ValueVisitor visitor) {
		return visitor.accept(this);
	}
}
