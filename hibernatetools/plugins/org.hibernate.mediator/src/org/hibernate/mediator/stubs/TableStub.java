package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mediator.Messages;
import org.hibernate.mediator.base.HObject;

public class TableStub extends HObject {
	public static final String CL = "org.hibernate.mapping.Table"; //$NON-NLS-1$

	protected TableStub(Object table) {
		super(table, CL);
	}
	
	public static TableStub newInstance(String name) {
		return new TableStub(newInstance(CL, name));
	}
	
	public String getName() {
		return (String)invoke("getName"); //$NON-NLS-1$
	}
	
	public PrimaryKeyStub getPrimaryKey() {
		Object obj = invoke("getPrimaryKey"); //$NON-NLS-1$
		if (obj == null) {
			return null;
		}
		return new PrimaryKeyStub(obj);
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<ForeignKeyStub> getForeignKeyIterator() {
		Iterator<ForeignKey> it = (Iterator<ForeignKey>)invoke("getForeignKeyIterator"); //$NON-NLS-1$
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
		return (String)invoke("getCatalog"); //$NON-NLS-1$
	}

	public String getSchema() {
		return (String)invoke("getSchema"); //$NON-NLS-1$
	}

	public KeyValueStub getIdentifierValue() {
		Object obj = invoke("getIdentifierValue"); //$NON-NLS-1$
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
		Iterator<Column> it = (Iterator<Column>)invoke("getColumnIterator"); //$NON-NLS-1$
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
		return (String)invoke("getComment"); //$NON-NLS-1$
	}

	public String getRowId() {
		return (String)invoke("getRowId"); //$NON-NLS-1$
	}

	public String getSubselect() {
		return (String)invoke("getSubselect"); //$NON-NLS-1$
	}

	public boolean hasDenormalizedTables() {
		return (Boolean)invoke("hasDenormalizedTables"); //$NON-NLS-1$
	}

	public boolean isAbstract() {
		return (Boolean)invoke("isAbstract"); //$NON-NLS-1$
	}

	public boolean isAbstractUnionTable() {
		return (Boolean)invoke("isAbstractUnionTable"); //$NON-NLS-1$
	}

	public boolean isPhysicalTable() {
		return (Boolean)invoke("isPhysicalTable"); //$NON-NLS-1$
	}
}
