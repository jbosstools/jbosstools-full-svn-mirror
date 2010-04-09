package org.hibernate.mediator.x.mapping;

import org.hibernate.mediator.x.FetchMode;

public abstract class CollectionStub extends Value {
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

	public void setKey(KeyValue key) {
		invoke(mn(), key);
	}

	public void setLazy(boolean lazy) {
		invoke(mn(), lazy);
	}

	public void setRole(String role) {
		invoke(mn(), role);
	}

	public void setElement(Value element) {
		invoke(mn(), element);
	}

	public void setFetchMode(FetchMode fetchMode) {
		invoke(mn(), fetchMode);
	}

	public Value getElement() {
		return ValueFactory.createValueStub(invoke(mn()));
	}

	public TableStub getCollectionTable() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

	public KeyValue getKey() {
		return (KeyValue)ValueFactory.createValueStub(invoke(mn()));
	}

	public boolean isOneToMany() {
		return (Boolean)invoke(mn());
	}

	public boolean isInverse() {
		return (Boolean)invoke(mn());
	}
}
