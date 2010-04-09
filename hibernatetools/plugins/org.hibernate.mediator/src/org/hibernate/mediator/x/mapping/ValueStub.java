package org.hibernate.mediator.x.mapping;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.type.TypeStub;
import org.hibernate.mediator.x.type.TypeStubFactory;

public abstract class ValueStub extends HObject {
	public static final String CL = "org.hibernate.mapping.Value"; //$NON-NLS-1$

	protected ValueStub(Object value) {
		super(value, CL);
	}

	protected ValueStub(Object value, String cn) {
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

	public TypeStub getType() {
		return TypeStubFactory.createTypeStub(invoke(mn()));
	}

	@SuppressWarnings("unchecked")
	public Iterator<SelectableStub> getColumnIterator() {
		Iterator it = (Iterator)invoke(mn());
		ArrayList<SelectableStub> al = new ArrayList<SelectableStub>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(SelectableStubFactory.createSelectableStub(obj));
			}
		}
		return al.iterator();
	}

	public abstract Object accept(ValueVisitorStub visitor);
};
