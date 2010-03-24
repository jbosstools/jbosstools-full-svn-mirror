package org.hibernate.console.stubs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;

public class ForeignKeyStub {
	protected ForeignKey foreignKey;

	protected ForeignKeyStub(Object foreignKey) {
		this.foreignKey = (ForeignKey)foreignKey;
	}
	
	public boolean containsColumn(ColumnStub column) {
		return foreignKey.containsColumn(column.column);
	}
	
	public TableStub getReferencedTable() {
		return new TableStub(foreignKey.getReferencedTable());
	}

	@SuppressWarnings("unchecked")
	public Iterator<ColumnStub> columnIterator() {
		Iterator<Column> it = (Iterator<Column>)foreignKey.getColumnIterator();
		ArrayList<ColumnStub> al = new ArrayList<ColumnStub>();
		while (it.hasNext()) {
			al.add(new ColumnStub(it.next()));
		}
		return al.iterator();
	}

	@SuppressWarnings("unchecked")
	public List<ColumnStub> getReferencedColumns() {
		Iterator<Column> it = foreignKey.getReferencedColumns().iterator();
		ArrayList<ColumnStub> al = new ArrayList<ColumnStub>();
		while (it.hasNext()) {
			al.add(new ColumnStub(it.next()));
		}
		return al;
	}

	public boolean isReferenceToPrimaryKey() {
		return foreignKey.isReferenceToPrimaryKey();
	}

}
