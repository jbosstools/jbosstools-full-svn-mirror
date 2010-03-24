package org.hibernate.console.stubs;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.Value;

public abstract class ValueStub {
	protected Value value;

	protected ValueStub(Object value) {
		this.value = (Value)value;
	}
	
	public TableStub getTable() {
		return new TableStub(value.getTable());
	}

	public boolean isSimpleValue() {
		return value.isSimpleValue();
	}

	public TypeStub getType() {
		return TypeStubFactory.createTypeStub(value.getType());
	}

	@SuppressWarnings("unchecked")
	public Iterator<ColumnStub> getColumnIterator() {
		Iterator<Column> it = (Iterator<Column>)value.getColumnIterator();
		ArrayList<ColumnStub> al = new ArrayList<ColumnStub>();
		while (it.hasNext()) {
			al.add(new ColumnStub(it.next()));
		}
		return al.iterator();
	}

	public abstract Object accept(ValueVisitorStub visitor);
};
