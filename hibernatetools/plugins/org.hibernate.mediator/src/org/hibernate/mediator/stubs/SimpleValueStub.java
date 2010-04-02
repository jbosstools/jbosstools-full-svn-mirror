package org.hibernate.mediator.stubs;

import java.util.Properties;

import org.hibernate.mapping.SimpleValue;
import org.hibernate.mediator.Messages;

public class SimpleValueStub extends KeyValueStub {
	public static final String CL = "org.hibernate.mapping.SimpleValue"; //$NON-NLS-1$

	protected SimpleValue simpleValue;

	protected SimpleValueStub(Object simpleValue) {
		super(simpleValue);
		if (simpleValue == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.simpleValue = (SimpleValue)simpleValue;
	}
	
	public static SimpleValueStub newInstance() {
		return new SimpleValueStub(new SimpleValue());
	}

	public void setTypeName(String type) {
		simpleValue.setTypeName(type);
	}

	public void addColumn(ColumnStub column) {
		simpleValue.addColumn(column.column);
	}

	public void setTable(TableStub table) {
		simpleValue.setTable(table.table);
	}

	public void setTypeParameters(Properties parameterMap) {
		simpleValue.setTypeParameters(parameterMap);
	}

	public String getForeignKeyName() {
		return simpleValue.getForeignKeyName();
	}

	public String getTypeName() {
		return simpleValue.getTypeName();
	}

	public boolean isTypeSpecified() {
		return simpleValue.isTypeSpecified();
	}

	@Override
	public Object accept(ValueVisitorStub visitor) {
		return visitor.accept(this);
	}
}
