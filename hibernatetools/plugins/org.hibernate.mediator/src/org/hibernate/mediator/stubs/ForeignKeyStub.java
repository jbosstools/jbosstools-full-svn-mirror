package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mediator.Messages;

public class ForeignKeyStub {
	protected ForeignKey foreignKey;

	protected ForeignKeyStub(Object foreignKey) {
		if (foreignKey == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.foreignKey = (ForeignKey)foreignKey;
	}
	
	public boolean containsColumn(ColumnStub column) {
		return foreignKey.containsColumn(column.column);
	}
	
	public TableStub getReferencedTable() {
		Object obj = foreignKey.getReferencedTable();
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

	@SuppressWarnings("unchecked")
	public Iterator<ColumnStub> columnIterator() {
		Iterator<Column> it = (Iterator<Column>)foreignKey.getColumnIterator();
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
		Iterator<Column> it = foreignKey.getReferencedColumns().iterator();
		ArrayList<ColumnStub> al = new ArrayList<ColumnStub>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new ColumnStub(obj));
			}
		}
		return al;
	}

	public boolean isReferenceToPrimaryKey() {
		return foreignKey.isReferenceToPrimaryKey();
	}

}
