package org.hibernate.mediator.x.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.mediator.base.HObject;

public class ForeignKey extends HObject {
	public static final String CL = "org.hibernate.mapping.ForeignKey"; //$NON-NLS-1$

	protected ForeignKey(Object foreignKey) {
		super(foreignKey, CL);
	}
	
	public boolean containsColumn(Column column) {
		return (Boolean)invoke(mn(), column);
	}
	
	public TableStub getReferencedTable() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
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
	public List<Column> getReferencedColumns() {
		List list = (List)invoke(mn());
		ArrayList<Column> al = new ArrayList<Column>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new Column(obj));
			}
		}
		return al;
	}

	public boolean isReferenceToPrimaryKey() {
		return (Boolean)invoke(mn());
	}

}
