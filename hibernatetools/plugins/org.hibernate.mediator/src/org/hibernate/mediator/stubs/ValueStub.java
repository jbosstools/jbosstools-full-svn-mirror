package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mapping.Selectable;
import org.hibernate.mediator.base.HObject;

public abstract class ValueStub extends HObject {
	public static final String CL = "org.hibernate.mapping.Value"; //$NON-NLS-1$

	protected ValueStub(Object value) {
		super(value, CL);
	}

	protected ValueStub(Object value, String cn) {
		super(value, cn);
	}
	
	public TableStub getTable() {
		Object obj = invoke("getTable"); //$NON-NLS-1$
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

	public boolean isSimpleValue() {
		return (Boolean)invoke("isSimpleValue"); //$NON-NLS-1$
	}

	public TypeStub getType() {
		return TypeStubFactory.createTypeStub(invoke("getType")); //$NON-NLS-1$
	}

	@SuppressWarnings("unchecked")
	public Iterator<SelectableStub> getColumnIterator() {
		Iterator<Selectable> it = (Iterator<Selectable>)invoke("getColumnIterator"); //$NON-NLS-1$
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
