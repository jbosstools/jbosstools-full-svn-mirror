package org.hibernate.mediator.x.mapping;

import org.hibernate.mediator.x.FetchModeStub;

public abstract class CollectionStub extends ValueStub {
	public static final String CL = "org.hibernate.mapping.Collection"; //$NON-NLS-1$

	protected CollectionStub(Object collection) {
		super(collection, CL);
	}

	protected CollectionStub(Object collection, String cn) {
		super(collection, cn);
	}

	public void setCollectionTable(TableStub table) {
		invoke(mn(), table);
	}

	public void setKey(KeyValueStub key) {
		invoke(mn(), key);
	}

	public void setLazy(boolean lazy) {
		invoke(mn(), lazy);
	}

	public void setRole(String role) {
		invoke(mn(), role);
	}

	public void setElement(ValueStub element) {
		invoke(mn(), element);
	}

	public void setFetchMode(FetchModeStub fetchMode) {
		invoke(mn(), fetchMode);
	}

	public ValueStub getElement() {
		return ValueStubFactory.createValueStub(invoke(mn()));
	}

	public TableStub getCollectionTable() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

	public KeyValueStub getKey() {
		return (KeyValueStub)ValueStubFactory.createValueStub(invoke(mn()));
	}

	public boolean isOneToMany() {
		return (Boolean)invoke(mn());
	}

	public boolean isInverse() {
		return (Boolean)invoke(mn());
	}
}
