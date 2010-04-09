package org.hibernate.mediator.x.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.mediator.base.HObject;

public class PrimaryKey extends HObject {
	public static final String CL = "org.hibernate.mapping.PrimaryKey"; //$NON-NLS-1$

	protected PrimaryKey(Object primaryKey) {
		super(primaryKey, CL);
	}
	
	public boolean containsColumn(Column column) {
		return (Boolean)invoke(mn(), column);
	}

	public String getName() {
		return (String)invoke(mn());
	}

	@SuppressWarnings("unchecked")
	public Iterator<Column> columnIterator() {
		Iterator it = (Iterator)invoke(mn());
		ArrayList<Column> al = new ArrayList<Column>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new Column(obj));
			}
		}
		return al.iterator();
	}

	@SuppressWarnings("unchecked")
	public List<Column> getColumns() {
		Iterator it = (Iterator)invoke("getColumnIterator"); //$NON-NLS-1$
		ArrayList<Column> al = new ArrayList<Column>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new Column(obj));
			}
		}
		return al;
	}

	public int getColumnSpan() {
		return (Integer)invoke(mn());
	}

	public Column getColumn(int i) {
		Object obj = invoke(mn(), i);
		if (obj == null) {
			return null;
		}
		return new Column(obj);
	}

	public TableStub getTable() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

}
