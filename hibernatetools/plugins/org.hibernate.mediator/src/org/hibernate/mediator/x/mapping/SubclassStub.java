package org.hibernate.mediator.x.mapping;


public class SubclassStub extends PersistentClassStub {
	public static final String CL = "org.hibernate.mapping.Subclass"; //$NON-NLS-1$

	protected SubclassStub(Object subclass) {
		super(subclass, CL);
	}

	protected SubclassStub(Object subclass, String cn) {
		super(subclass, cn);
	}

	public void setClassName(String className) {
		invoke(mn(), className);
	}
	
	public void setEntityName(String entityName) {
		invoke(mn(), entityName);
	}
	
	public void setDiscriminatorValue(String discriminatorValue) {
		invoke(mn(), discriminatorValue);
	}
	
	public void setAbstract(Boolean isAbstract) {
		invoke(mn(), isAbstract);
	}
	
	public void addProperty(PropertyStub p) {
		invoke(mn(), p);
	}

	public TableStub getRootTable() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

	public boolean isJoinedSubclass() {
		return (Boolean)invoke(mn());
	}
}
