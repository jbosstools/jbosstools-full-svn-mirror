package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mediator.Messages;

public class PrimaryKeyStub {
	protected PrimaryKey primaryKey;

	protected PrimaryKeyStub(Object primaryKey) {
		if (primaryKey == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.primaryKey = (PrimaryKey)primaryKey;
	}
	
	public boolean containsColumn(ColumnStub column) {
		return primaryKey.containsColumn(column.column);
	}

	public String getName() {
		return primaryKey.getName();
	}

	@SuppressWarnings("unchecked")
	public Iterator<ColumnStub> columnIterator() {
		Iterator<Column> it = (Iterator<Column>)primaryKey.getColumnIterator();
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
		Iterator<Column> it = (Iterator<Column>)primaryKey.getColumnIterator();
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
		return primaryKey.getColumnSpan();
	}

	public ColumnStub getColumn(int i) {
		Object obj = primaryKey.getColumn(i);
		if (obj == null) {
			return null;
		}
		return new ColumnStub(obj);
	}

	public TableStub getTable() {
		Object obj = primaryKey.getTable();
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

}
