package org.hibernate.mediator.stubs;

public abstract class CollectionStub extends ValueStub {
	public static final String CL = "org.hibernate.mapping.Collection"; //$NON-NLS-1$

	protected CollectionStub(Object collection) {
		super(collection, CL);
	}

	protected CollectionStub(Object collection, String cn) {
		super(collection, cn);
	}

	public void setCollectionTable(TableStub table) {
		invoke("setCollectionTable", table); //$NON-NLS-1$
	}

	public void setKey(KeyValueStub key) {
		invoke("setKey", key); //$NON-NLS-1$
	}

	public void setLazy(boolean lazy) {
		invoke("setLazy", lazy); //$NON-NLS-1$
	}

	public void setRole(String role) {
		invoke("setRole", role); //$NON-NLS-1$
	}

	public void setElement(ValueStub element) {
		invoke("setElement", element); //$NON-NLS-1$
	}

	public void setFetchMode(FetchModeStub fetchMode) {
		invoke("setFetchMode", fetchMode); //$NON-NLS-1$
	}

	public ValueStub getElement() {
		return ValueStubFactory.createValueStub(invoke("getElement")); //$NON-NLS-1$
	}

	public TableStub getCollectionTable() {
		Object obj = invoke("getCollectionTable"); //$NON-NLS-1$
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

	public KeyValueStub getKey() {
		return (KeyValueStub)ValueStubFactory.createValueStub(invoke("getKey")); //$NON-NLS-1$
	}

	public boolean isOneToMany() {
		return (Boolean)invoke("isOneToMany"); //$NON-NLS-1$
	}

	public boolean isInverse() {
		return (Boolean)invoke("isInverse"); //$NON-NLS-1$
	}
}
