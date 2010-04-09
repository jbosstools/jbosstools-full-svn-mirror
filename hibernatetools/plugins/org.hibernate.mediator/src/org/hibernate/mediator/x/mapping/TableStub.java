package org.hibernate.mediator.x.mapping;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mediator.HibernateConsoleRuntimeException;
import org.hibernate.mediator.Messages;
import org.hibernate.mediator.base.HObject;

public class TableStub extends HObject {
	public static final String CL = "org.hibernate.mapping.Table"; //$NON-NLS-1$

	public TableStub(Object table) {
		super(table, CL);
	}
	
	public static TableStub newInstance(String name) {
		return new TableStub(newInstance(CL, name));
	}
	
	public String getName() {
		return (String)invoke(mn());
	}
	
	public PrimaryKey getPrimaryKey() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new PrimaryKey(obj);
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<ForeignKey> getForeignKeyIterator() {
		Iterator it = (Iterator)invoke(mn());
		ArrayList<ForeignKey> al = new ArrayList<ForeignKey>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new ForeignKey(obj));
			}
		}
		return al.iterator();
	}

	public String getCatalog() {
		return (String)invoke(mn());
	}

	public String getSchema() {
		return (String)invoke(mn());
	}

	public KeyValue getIdentifierValue() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		Value res = ValueFactory.createValueStub(obj);
		if (res instanceof KeyValue) {
			return (KeyValue)res;
		}
		throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
	}

	@SuppressWarnings("unchecked")
	public Iterator<Column> getColumnIterator() {
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

	public String getComment() {
		return (String)invoke(mn());
	}

	public String getRowId() {
		return (String)invoke(mn());
	}

	public String getSubselect() {
		return (String)invoke(mn());
	}

	public boolean hasDenormalizedTables() {
		return (Boolean)invoke(mn());
	}

	public boolean isAbstract() {
		return (Boolean)invoke(mn());
	}

	public boolean isAbstractUnionTable() {
		return (Boolean)invoke(mn());
	}

	public boolean isPhysicalTable() {
		return (Boolean)invoke(mn());
	}
}
