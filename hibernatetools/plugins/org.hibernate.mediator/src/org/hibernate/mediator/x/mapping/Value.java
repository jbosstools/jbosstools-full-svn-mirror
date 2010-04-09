package org.hibernate.mediator.x.mapping;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.type.Type;
import org.hibernate.mediator.x.type.TypeFactory;

public abstract class Value extends HObject {
	public static final String CL = "org.hibernate.mapping.Value"; //$NON-NLS-1$

	protected Value(Object value) {
		super(value, CL);
	}

	protected Value(Object value, String cn) {
		super(value, cn);
	}
	
	public TableStub getTable() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

	public boolean isSimpleValue() {
		return (Boolean)invoke(mn());
	}

	public Type getType() {
		return TypeFactory.createTypeStub(invoke(mn()));
	}

	@SuppressWarnings("unchecked")
	public Iterator<Selectable> getColumnIterator() {
		Iterator it = (Iterator)invoke(mn());
		ArrayList<Selectable> al = new ArrayList<Selectable>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(SelectableFactory.createSelectableStub(obj));
			}
		}
		return al.iterator();
	}

	public abstract Object accept(ValueVisitor visitor);
};
