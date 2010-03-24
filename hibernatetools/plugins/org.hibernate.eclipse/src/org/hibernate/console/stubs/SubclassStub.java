package org.hibernate.console.stubs;

import org.hibernate.mapping.Subclass;

public class SubclassStub extends PersistentClassStub {
	
	protected Subclass subclass;

	protected SubclassStub(Object subclass) {
		super(subclass);
		this.subclass = (Subclass)subclass;
	}

	public void setClassName(String className) {
		subclass.setClassName(className);
	}
	
	public void setEntityName(String entityName) {
		subclass.setEntityName(entityName);
	}
	
	public void setDiscriminatorValue(String discriminatorValue) {
		subclass.setDiscriminatorValue(discriminatorValue);
	}
	
	public void setAbstract(Boolean isAbstract) {
		subclass.setAbstract(isAbstract);
	}
	
	public void addProperty(PropertyStub p) {
		subclass.addProperty(p.property);
	}

	public TableStub getRootTable() {
		return new TableStub(subclass.getRootTable());
	}

	public boolean isJoinedSubclass() {
		return subclass.isJoinedSubclass();
	}
}
