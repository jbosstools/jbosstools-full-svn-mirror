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
		invoke("setTypeName", type); //$NON-NLS-1$
	}

	public void addColumn(ColumnStub column) {
		invoke("addColumn", column.column); //$NON-NLS-1$
	}

	public void setTable(TableStub table) {
		invoke("setTable", table); //$NON-NLS-1$
	}

	public void setTypeParameters(Properties parameterMap) {
		invoke("setTypeParameters" ,parameterMap); //$NON-NLS-1$
	}

	public String getForeignKeyName() {
		return (String)invoke("getForeignKeyName"); //$NON-NLS-1$
	}

	public String getTypeName() {
		return (String)invoke("getTypeName"); //$NON-NLS-1$
	}

	public boolean isTypeSpecified() {
		return (Boolean)invoke("isTypeSpecified"); //$NON-NLS-1$
	}

	@Override
	public Object accept(ValueVisitorStub visitor) {
		return visitor.accept(this);
	}
}
