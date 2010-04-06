package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mediator.base.HObject;

public abstract class PersistentClassStub extends HObject {
	public static final String CL = "org.hibernate.mapping.PersistentClass"; //$NON-NLS-1$

	protected PersistentClassStub(Object persistentClass) {
		super(persistentClass, CL);
	}

	protected PersistentClassStub(Object persistentClass, String cn) {
		super(persistentClass, cn);
	}
	
	public Boolean isAbstract() {
		return (Boolean)invoke(mn());
	}
	
	public ValueStub getDiscriminator() {
		return ValueStubFactory.createValueStub(invoke(mn()));
	}
	
	public String getClassName() {
		return (String)invoke(mn());
	}
	
	public String getEntityName() {
		return (String)invoke(mn());
	}
	
	public PropertyStub getIdentifierProperty() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new PropertyStub(obj);
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<PropertyStub> getPropertyIterator() {
		Iterator it = (Iterator)invoke(mn());
		ArrayList<PropertyStub> al = new ArrayList<PropertyStub>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new PropertyStub(obj));
			}
		}
		return al.iterator();
	}

	public RootClassStub getRootClass() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new RootClassStub(obj);
	}

	public PropertyStub getVersion() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new PropertyStub(obj);
	}

	public TableStub getTable() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

	@SuppressWarnings("unchecked")
	public Iterator<JoinStub> getJoinIterator() {
		Iterator it = (Iterator)invoke(mn());
		ArrayList<JoinStub> al = new ArrayList<JoinStub>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new JoinStub(obj));
			}
		}
		return al.iterator();
	}

	public KeyValueStub getIdentifier() {
		return (KeyValueStub)ValueStubFactory.createValueStub(invoke(mn()));
	}

	public PersistentClassStub getSuperclass() {
		return PersistentClassStubFactory.createPersistentClassStub(invoke(mn()));
	}

	public PropertyStub getProperty(String propertyName) {
		Object obj = invoke(mn(), propertyName);
		if (obj == null) {
			return null;
		}
		return new PropertyStub(obj);
	}

	public String getNodeName() {
		return (String)invoke(mn());
	}

	@SuppressWarnings("unchecked")
	public Iterator<PropertyStub> getPropertyClosureIterator() {
		Iterator it = (Iterator)invoke(mn());
		ArrayList<PropertyStub> al = new ArrayList<PropertyStub>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new PropertyStub(obj));
			}
		}
		return al.iterator();
	}
}
