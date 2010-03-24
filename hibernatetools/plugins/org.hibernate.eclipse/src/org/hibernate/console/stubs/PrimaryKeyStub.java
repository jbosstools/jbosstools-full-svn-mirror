package org.hibernate.console.stubs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.PrimaryKey;

public class PrimaryKeyStub {
	protected PrimaryKey primaryKey;

	protected PrimaryKeyStub(Object primaryKey) {
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
			al.add(new ColumnStub(it.next()));
		}
		return al.iterator();
	}

	@SuppressWarnings("unchecked")
	public List<ColumnStub> getColumns() {
		Iterator<Column> it = (Iterator<Column>)primaryKey.getColumnIterator();
		ArrayList<ColumnStub> al = new ArrayList<ColumnStub>();
		while (it.hasNext()) {
			al.add(new ColumnStub(it.next()));
		}
		return al;
	}

	public int getColumnSpan() {
		return primaryKey.getColumnSpan();
	}

	public ColumnStub getColumn(int i) {
		return new ColumnStub(primaryKey.getColumn(i));
	}

	public TableStub getTable() {
		return new TableStub(primaryKey.getTable());
	}

}
