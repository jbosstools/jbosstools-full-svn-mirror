package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.Table;
import org.hibernate.mediator.Messages;

public class TableStub {
	protected Table table;

	protected TableStub(Object table) {
		if (table == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.table = (Table)table;
	}
	
	protected Table getTable() {
		return table;
	}
	
	public static TableStub newInstance(String name) {
		return new TableStub(new Table(name));
	}
	
	public String getName() {
		return table.getName();
	}
	
	public PrimaryKeyStub getPrimaryKey() {
		Object obj = table.getPrimaryKey();
		if (obj == null) {
			return null;
		}
		return new PrimaryKeyStub(obj);
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<ForeignKeyStub> getForeignKeyIterator() {
		Iterator<ForeignKey> it = (Iterator<ForeignKey>)table.getForeignKeyIterator();
		ArrayList<ForeignKeyStub> al = new ArrayList<ForeignKeyStub>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new ForeignKeyStub(obj));
			}
		}
		return al.iterator();
	}

	public String getCatalog() {
		return table.getCatalog();
	}

	public String getSchema() {
		return table.getSchema();
	}

	public KeyValueStub getIdentifierValue() {
		Object obj = table.getIdentifierValue();
		if (obj == null) {
			return null;
		}
		ValueStub res = ValueStubFactory.createValueStub(obj);
		if (res instanceof KeyValueStub) {
			return (KeyValueStub)res;
		}
		throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
	}

	@SuppressWarnings("unchecked")
	public Iterator<ColumnStub> getColumnIterator() {
		Iterator<Column> it = (Iterator<Column>)table.getColumnIterator();
		ArrayList<ColumnStub> al = new ArrayList<ColumnStub>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new ColumnStub(obj));
			}
		}
		return al.iterator();
	}

	public String getComment() {
		return table.getComment();
	}

	public String getRowId() {
		return table.getRowId();
	}

	public String getSubselect() {
		return table.getSubselect();
	}

	public boolean hasDenormalizedTables() {
		return table.hasDenormalizedTables();
	}

	public boolean isAbstract() {
		return table.isAbstract();
	}

	public boolean isAbstractUnionTable() {
		return table.isAbstractUnionTable();
	}

	public boolean isPhysicalTable() {
		return table.isPhysicalTable();
	}
}
