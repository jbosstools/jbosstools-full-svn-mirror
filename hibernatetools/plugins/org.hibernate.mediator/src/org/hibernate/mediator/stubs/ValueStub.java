package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.Selectable;
import org.hibernate.mapping.Value;
import org.hibernate.mediator.Messages;

public abstract class ValueStub {
	protected Value value;

	protected ValueStub(Object value) {
		if (value == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.value = (Value)value;
	}
	
	public TableStub getTable() {
		Object obj = value.getTable();
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

	public boolean isSimpleValue() {
		return value.isSimpleValue();
	}

	public TypeStub getType() {
		return TypeStubFactory.createTypeStub(value.getType());
	}

	@SuppressWarnings("unchecked")
	public Iterator<SelectableStub> getColumnIterator() {
		Iterator<Selectable> it = (Iterator<Selectable>)value.getColumnIterator();
		ArrayList<SelectableStub> al = new ArrayList<SelectableStub>();
		while (it.hasNext()) {
			Selectable obj = it.next();
			if (obj != null) {
				al.add(SelectableStubFactory.createSelectableStub(obj));
			}
		}
		return al.iterator();
	}

	public abstract Object accept(ValueVisitorStub visitor);
};
