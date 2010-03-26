package org.hibernate.console.stubs;

import org.hibernate.mapping.Property;

public class PropertyStub {
	protected Property property;

	protected PropertyStub(Object property) {
		this.property = (Property)property;
	}

	public static PropertyStub newInstance() {
		return new PropertyStub(new Property());
	}

	public void setName(String name) {
		property.setName(name);
	}

	public void setValue(ValueStub value) {
		property.setValue(value.value);
	}

	public ValueStub getValue() {
		return ValueStubFactory.createValueStub(property.getValue());
	}

	public void setCascade(String cascade) {
		property.setCascade(cascade);
	}

	public String getName() {
		return property.getName();
	}

	public TypeStub getType() {
		return TypeStubFactory.createTypeStub(property.getType());
	}

	public PersistentClassStub getPersistentClass() {
		return PersistentClassStubFactory.createPersistentClassStub(property.getPersistentClass());
	}

	public void setPersistentClass(PersistentClassStub ownerClass) {
		property.setPersistentClass(ownerClass.persistentClass);
	}

	public boolean isSelectable() {
		return property.isSelectable();
	}

	public boolean isInsertable() {
		return property.isInsertable();
	}

	public boolean isUpdateable() {
		return property.isUpdateable();
	}

	public boolean isLazy() {
		return property.isLazy();
	}

	public boolean isOptional() {
		return property.isOptional();
	}

	public boolean isNaturalIdentifier() {
		return property.isNaturalIdentifier();
	}

	public boolean isOptimisticLocked() {
		return property.isOptimisticLocked();
	}

	public String getCascade() {
		return property.getCascade();
	}

	public String getNodeName() {
		return property.getNodeName();
	}

	public boolean isBackRef() {
		return property.isBackRef();
	}

	public boolean isComposite() {
		return property.isComposite();
	}

	public String getPropertyAccessorName() {
		return property.getPropertyAccessorName();
	}

}
