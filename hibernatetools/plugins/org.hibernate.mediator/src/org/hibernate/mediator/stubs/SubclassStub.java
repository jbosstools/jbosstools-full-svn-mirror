package org.hibernate.mediator.stubs;

public class SubclassStub extends PersistentClassStub {
	public static final String CL = "org.hibernate.mapping.Subclass"; //$NON-NLS-1$

	protected SubclassStub(Object subclass) {
		super(subclass, CL);
	}

	protected SubclassStub(Object subclass, String cn) {
		super(subclass, cn);
	}

	public void setClassName(String className) {
		invoke("setClassName", className); //$NON-NLS-1$
	}
	
	public void setEntityName(String entityName) {
		invoke("setEntityName", entityName); //$NON-NLS-1$
	}
	
	public void setDiscriminatorValue(String discriminatorValue) {
		invoke("setDiscriminatorValue", discriminatorValue); //$NON-NLS-1$
	}
	
	public void setAbstract(Boolean isAbstract) {
		invoke("setAbstract", isAbstract); //$NON-NLS-1$
	}
	
	public void addProperty(PropertyStub p) {
		invoke("addProperty", p); //$NON-NLS-1$
	}

	public TableStub getRootTable() {
		Object obj = invoke("getRootTable"); //$NON-NLS-1$
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

	public boolean isJoinedSubclass() {
		return (Boolean)invoke("isJoinedSubclass"); //$NON-NLS-1$
	}
}
