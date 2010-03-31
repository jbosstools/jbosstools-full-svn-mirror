package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mapping.Join;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mediator.Messages;

public abstract class PersistentClassStub {
	protected PersistentClass persistentClass;

	protected PersistentClassStub(Object persistentClass) {
		if (persistentClass == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		if (persistentClass instanceof PersistentClassStub) {
			this.persistentClass = ((PersistentClassStub)persistentClass).persistentClass;
		} else {
			this.persistentClass = (PersistentClass)persistentClass;
		}
	}
	
	protected PersistentClass getPersistentClass() {
		return persistentClass;
	}
	
	public Boolean isAbstract() {
		return persistentClass.isAbstract();
	}
	
	public ValueStub getDiscriminator() {
		return ValueStubFactory.createValueStub(persistentClass.getDiscriminator());
	}
	
	public String getClassName() {
		return persistentClass.getClassName();
	}
	
	public String getEntityName() {
		return persistentClass.getEntityName();
	}
	
	public PropertyStub getIdentifierProperty() {
		Object obj = persistentClass.getIdentifierProperty();
		if (obj == null) {
			return null;
		}
		return new PropertyStub(obj);
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<PropertyStub> getPropertyIterator() {
		Iterator<Property> it = persistentClass.getPropertyIterator();
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
		Object obj = persistentClass.getRootClass();
		if (obj == null) {
			return null;
		}
		return new RootClassStub(obj);
	}

	public PropertyStub getVersion() {
		Object obj = persistentClass.getVersion();
		if (obj == null) {
			return null;
		}
		return new PropertyStub(obj);
	}

	public TableStub getTable() {
		Object obj = persistentClass.getTable();
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

	@SuppressWarnings("unchecked")
	public Iterator<JoinStub> getJoinIterator() {
		Iterator<Join> it = (Iterator<Join>)persistentClass.getJoinIterator();
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
		return (KeyValueStub)ValueStubFactory.createValueStub(persistentClass.getIdentifier());
	}

	public PersistentClassStub getSuperclass() {
		return PersistentClassStubFactory.createPersistentClassStub(persistentClass.getSuperclass());
	}

	public PropertyStub getProperty(String propertyName) {
		Object obj = persistentClass.getProperty(propertyName);
		if (obj == null) {
			return null;
		}
		return new PropertyStub(obj);
	}

	public String getNodeName() {
		return persistentClass.getNodeName();
	}

	@SuppressWarnings("unchecked")
	public Iterator<PropertyStub> getPropertyClosureIterator() {
		Iterator<Property> it = (Iterator<Property>)persistentClass.getPropertyClosureIterator();
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
