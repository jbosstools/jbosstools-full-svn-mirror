package org.hibernate.mediator.x.mapping;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mediator.base.HObject;

public abstract class PersistentClass extends HObject {
	public static final String CL = "org.hibernate.mapping.PersistentClass"; //$NON-NLS-1$

	protected PersistentClass(Object persistentClass) {
		super(persistentClass, CL);
	}

	protected PersistentClass(Object persistentClass, String cn) {
		super(persistentClass, cn);
	}
	
	public Boolean isAbstract() {
		return (Boolean)invoke(mn());
	}
	
	public Value getDiscriminator() {
		return ValueFactory.createValueStub(invoke(mn()));
	}
	
	public String getClassName() {
		return (String)invoke(mn());
	}
	
	public String getEntityName() {
		return (String)invoke(mn());
	}
	
	public Property getIdentifierProperty() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new Property(obj);
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<Property> getPropertyIterator() {
		Iterator it = (Iterator)invoke(mn());
		ArrayList<Property> al = new ArrayList<Property>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new Property(obj));
			}
		}
		return al.iterator();
	}

	public RootClass getRootClass() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new RootClass(obj);
	}

	public Property getVersion() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new Property(obj);
	}

	public TableStub getTable() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

	@SuppressWarnings("unchecked")
	public Iterator<Join> getJoinIterator() {
		Iterator it = (Iterator)invoke(mn());
		ArrayList<Join> al = new ArrayList<Join>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new Join(obj));
			}
		}
		return al.iterator();
	}

	public KeyValue getIdentifier() {
		return (KeyValue)ValueFactory.createValueStub(invoke(mn()));
	}

	public PersistentClass getSuperclass() {
		return PersistentClassFactory.createPersistentClassStub(invoke(mn()));
	}

	public Property getProperty(String propertyName) {
		Object obj = invoke(mn(), propertyName);
		if (obj == null) {
			return null;
		}
		return new Property(obj);
	}

	public String getNodeName() {
		return (String)invoke(mn());
	}

	@SuppressWarnings("unchecked")
	public Iterator<Property> getPropertyClosureIterator() {
		Iterator it = (Iterator)invoke(mn());
		ArrayList<Property> al = new ArrayList<Property>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new Property(obj));
			}
		}
		return al.iterator();
	}
}
