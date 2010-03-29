package org.hibernate.mediator.stubs;

import org.hibernate.FetchMode;
import org.hibernate.mapping.Collection;

public abstract class CollectionStub extends ValueStub {
	
	protected Collection collection;

	protected CollectionStub(Object collection) {
		super(collection);
		this.collection = (Collection)collection;
	}

	public void setCollectionTable(TableStub table) {
		collection.setCollectionTable(table.table);
	}

	public void setKey(KeyValueStub key) {
		collection.setKey(key.keyValue);
	}

	public void setLazy(boolean lazy) {
		collection.setLazy(lazy);
	}

	public void setRole(String role) {
		collection.setRole(role);
	}

	public void setElement(ValueStub element) {
		collection.setElement(element.value);
	}

	public void setFetchMode(FetchModeStub fetchMode) {
		if (FetchModeStub.DEFAULT.equals(fetchMode)) {
			collection.setFetchMode(FetchMode.DEFAULT);
		} else if (FetchModeStub.SELECT.equals(fetchMode)) {
			collection.setFetchMode(FetchMode.SELECT);
		} else if (FetchModeStub.JOIN.equals(fetchMode)) {
			collection.setFetchMode(FetchMode.JOIN);
		} else if (FetchModeStub.SUBSELECT.equals(fetchMode)) {
		}
	}

	public ValueStub getElement() {
		return ValueStubFactory.createValueStub(collection.getElement());
	}

	public TableStub getCollectionTable() {
		return new TableStub(collection.getCollectionTable());
	}

	public KeyValueStub getKey() {
		return (KeyValueStub)ValueStubFactory.createValueStub(collection.getKey());
	}

	public boolean isOneToMany() {
		return collection.isOneToMany();
	}

	public boolean isInverse() {
		return collection.isInverse();
	}
}
