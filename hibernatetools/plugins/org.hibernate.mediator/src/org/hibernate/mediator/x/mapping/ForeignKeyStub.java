package org.hibernate.mediator.x.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.mediator.base.HObject;

public class ForeignKeyStub extends HObject {
	public static final String CL = "org.hibernate.mapping.ForeignKey"; //$NON-NLS-1$

	protected ForeignKeyStub(Object foreignKey) {
		super(foreignKey, CL);
	}
	
	public boolean containsColumn(ColumnStub column) {
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
	public List<ColumnStub> getReferencedColumns() {
		List list = (List)invoke(mn());
		ArrayList<ColumnStub> al = new ArrayList<ColumnStub>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new ColumnStub(obj));
			}
		}
		return al;
	}

	public boolean isReferenceToPrimaryKey() {
		return (Boolean)invoke(mn());
	}

}
