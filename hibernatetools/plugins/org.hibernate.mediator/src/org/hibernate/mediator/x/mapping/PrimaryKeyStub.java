package org.hibernate.mediator.x.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.mediator.base.HObject;

public class PrimaryKeyStub extends HObject {
	public static final String CL = "org.hibernate.mapping.PrimaryKey"; //$NON-NLS-1$

	protected PrimaryKeyStub(Object primaryKey) {
		super(primaryKey, CL);
	}
	
	public boolean containsColumn(ColumnStub column) {
		return (Boolean)invoke(mn(), column);
	}

	public String getName() {
		return (String)invoke(mn());
	}

	@SuppressWarnings("unchecked")
	public Iterator<ColumnStub> columnIterator() {
		Iterator it = (Iterator)invoke(mn());
		ArrayList<ColumnStub> al = new ArrayList<ColumnStub>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new ColumnStub(obj));
			}
		}
		return al.iterator();
	}

	@SuppressWarnings("unchecked")
	public List<ColumnStub> getColumns() {
		Iterator it = (Iterator)invoke("getColumnIterator"); //$NON-NLS-1$
		ArrayList<ColumnStub> al = new ArrayList<ColumnStub>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new ColumnStub(obj));
			}
		}
		return al;
	}

	public int getColumnSpan() {
		return (Integer)invoke(mn());
	}

	public ColumnStub getColumn(int i) {
		Object obj = invoke(mn(), i);
		if (obj == null) {
			return null;
		}
		return new ColumnStub(obj);
	}

	public TableStub getTable() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

}
