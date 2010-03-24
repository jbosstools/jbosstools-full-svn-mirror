package org.hibernate.console.stubs;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mapping.Join;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

public abstract class PersistentClassStub {
	protected PersistentClass persistentClass;

	protected PersistentClassStub(Object persistentClass) {
		this.persistentClass = (PersistentClass)persistentClass;
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
		return new PropertyStub(persistentClass.getIdentifierProperty());
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<PropertyStub> getPropertyIterator() {
		Iterator<Property> it = persistentClass.getPropertyIterator();
		ArrayList<PropertyStub> al = new ArrayList<PropertyStub>();
		while (it.hasNext()) {
			al.add(new PropertyStub(it.next()));
		}
		return al.iterator();
	}

	public RootClassStub getRootClass() {
		return new RootClassStub(persistentClass.getRootClass());
	}

	public PropertyStub getVersion() {
		return new PropertyStub(persistentClass.getVersion());
	}

	public TableStub getTable() {
		return new TableStub(persistentClass.getTable());
	}

	@SuppressWarnings("unchecked")
	public Iterator<JoinStub> getJoinIterator() {
		Iterator<Join> it = (Iterator<Join>)persistentClass.getJoinIterator();
		ArrayList<JoinStub> al = new ArrayList<JoinStub>();
		while (it.hasNext()) {
			al.add(new JoinStub(it.next()));
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
		return new PropertyStub(persistentClass.getProperty(propertyName));
	}

	public String getNodeName() {
		return persistentClass.getNodeName();
	}
}
