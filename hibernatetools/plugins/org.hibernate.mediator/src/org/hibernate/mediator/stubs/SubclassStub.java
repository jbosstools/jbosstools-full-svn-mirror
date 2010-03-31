package org.hibernate.mediator.stubs;

import org.hibernate.mapping.Subclass;
import org.hibernate.mediator.Messages;

public class SubclassStub extends PersistentClassStub {
	
	protected Subclass subclass;

	protected SubclassStub(Object subclass) {
		super(subclass);
		if (subclass == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
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
		Object obj = subclass.getRootTable();
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

	public boolean isJoinedSubclass() {
		return subclass.isJoinedSubclass();
	}
}
