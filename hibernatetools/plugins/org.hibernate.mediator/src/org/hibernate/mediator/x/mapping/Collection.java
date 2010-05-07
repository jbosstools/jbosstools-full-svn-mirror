package org.hibernate.mediator.x.mapping;

import org.hibernate.mediator.x.FetchMode;

public abstract class Collection extends Value {
	public static final String CL = "org.hibernate.mapping.Collection"; //$NON-NLS-1$

	protected Collection(Object collection) {
		super(collection, CL);
	}

	protected Collection(Object collection, String cn) {
		super(collection, cn);
	}

	public void setCollectionTable(Table table) {
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

	public Table getCollectionTable() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new Table(obj);
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
