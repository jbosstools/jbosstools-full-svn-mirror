package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.Table;

public class TableStub {
	protected Table table;

	protected TableStub(Object table) {
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
		return new PrimaryKeyStub(table.getPrimaryKey());
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<ForeignKeyStub> getForeignKeyIterator() {
		Iterator<ForeignKey> it = (Iterator<ForeignKey>)table.getForeignKeyIterator();
		ArrayList<ForeignKeyStub> al = new ArrayList<ForeignKeyStub>();
		while (it.hasNext()) {
			al.add(new ForeignKeyStub(it.next()));
		}
		return al.iterator();
	}

	public String getCatalog() {
		return table.getCatalog();
	}

	public String getSchema() {
		return table.getSchema();
	}

	public DependantValueStub getIdentifierValue() {
		return new DependantValueStub(table.getIdentifierValue());
	}

	@SuppressWarnings("unchecked")
	public Iterator<ColumnStub> getColumnIterator() {
		Iterator<Column> it = (Iterator<Column>)table.getColumnIterator();
		ArrayList<ColumnStub> al = new ArrayList<ColumnStub>();
		while (it.hasNext()) {
			al.add(new ColumnStub(it.next()));
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
