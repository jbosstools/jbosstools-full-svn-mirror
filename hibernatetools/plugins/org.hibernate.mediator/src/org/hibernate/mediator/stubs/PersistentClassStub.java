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
		return (Boolean)invoke("isAbstract"); //$NON-NLS-1$
	}
	
	public ValueStub getDiscriminator() {
		return ValueStubFactory.createValueStub(invoke("getDiscriminator")); //$NON-NLS-1$
	}
	
	public String getClassName() {
		return (String)invoke("getClassName"); //$NON-NLS-1$
	}
	
	public String getEntityName() {
		return (String)invoke("getEntityName"); //$NON-NLS-1$
	}
	
	public PropertyStub getIdentifierProperty() {
		Object obj = invoke("getIdentifierProperty"); //$NON-NLS-1$
		if (obj == null) {
			return null;
		}
		return new PropertyStub(obj);
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<PropertyStub> getPropertyIterator() {
		Iterator it = (Iterator)invoke("getPropertyIterator"); //$NON-NLS-1$
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
		Object obj = invoke("getRootClass"); //$NON-NLS-1$
		if (obj == null) {
			return null;
		}
		return new RootClassStub(obj);
	}

	public PropertyStub getVersion() {
		Object obj = invoke("getVersion"); //$NON-NLS-1$
		if (obj == null) {
			return null;
		}
		return new PropertyStub(obj);
	}

	public TableStub getTable() {
		Object obj = invoke("getTable"); //$NON-NLS-1$
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

	@SuppressWarnings("unchecked")
	public Iterator<JoinStub> getJoinIterator() {
		Iterator it = (Iterator)invoke("getJoinIterator"); //$NON-NLS-1$
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
		return (KeyValueStub)ValueStubFactory.createValueStub(invoke("getIdentifier")); //$NON-NLS-1$
	}

	public PersistentClassStub getSuperclass() {
		return PersistentClassStubFactory.createPersistentClassStub(invoke("getSuperclass")); //$NON-NLS-1$
	}

	public PropertyStub getProperty(String propertyName) {
		Object obj = invoke("getProperty", propertyName); //$NON-NLS-1$
		if (obj == null) {
			return null;
		}
		return new PropertyStub(obj);
	}

	public String getNodeName() {
		return (String)invoke("getNodeName"); //$NON-NLS-1$
	}

	@SuppressWarnings("unchecked")
	public Iterator<PropertyStub> getPropertyClosureIterator() {
		Iterator it = (Iterator)invoke("getPropertyClosureIterator"); //$NON-NLS-1$
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
