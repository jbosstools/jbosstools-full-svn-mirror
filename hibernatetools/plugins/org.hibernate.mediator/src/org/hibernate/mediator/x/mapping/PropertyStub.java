package org.hibernate.mediator.x.mapping;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.type.TypeStub;
import org.hibernate.mediator.x.type.TypeStubFactory;

public class PropertyStub extends HObject {
	public static final String CL = "org.hibernate.mapping.Property"; //$NON-NLS-1$

	public PropertyStub(Object property) {
		super(property, CL);
	}

	public static PropertyStub newInstance() {
		return new PropertyStub(newInstance(CL));
	}

	public void setName(String name) {
		invoke(mn(), name);
	}

	public void setValue(ValueStub value) {
		invoke(mn(), value);
	}

	public ValueStub getValue() {
		return ValueStubFactory.createValueStub(invoke(mn()));
	}

	public void setCascade(String cascade) {
		invoke(mn(), cascade);
	}

	public String getName() {
		return (String)invoke(mn());
	}

	public TypeStub getType() {
		return TypeStubFactory.createTypeStub(invoke(mn()));
	}

	public PersistentClassStub getPersistentClass() {
		return PersistentClassStubFactory.createPersistentClassStub(invoke(mn()));
	}

	public void setPersistentClass(PersistentClassStub ownerClass) {
		invoke(mn(), ownerClass);
	}

	public boolean isSelectable() {
		return (Boolean)invoke(mn());
	}

	public boolean isInsertable() {
		return (Boolean)invoke(mn());
	}

	public boolean isUpdateable() {
		return (Boolean)invoke(mn());
	}

	public boolean isLazy() {
		return (Boolean)invoke(mn());
	}

	public boolean isOptional() {
		return (Boolean)invoke(mn());
	}

	public boolean isNaturalIdentifier() {
		return (Boolean)invoke(mn());
	}

	public boolean isOptimisticLocked() {
		return (Boolean)invoke(mn());
	}

	public String getCascade() {
		return (String)invoke(mn());
	}

	public String getNodeName() {
		return (String)invoke(mn());
	}

	public boolean isBackRef() {
		return (Boolean)invoke(mn());
	}

	public boolean isComposite() {
		return (Boolean)invoke(mn());
	}

	public String getPropertyAccessorName() {
		return (String)invoke(mn());
	}

}
