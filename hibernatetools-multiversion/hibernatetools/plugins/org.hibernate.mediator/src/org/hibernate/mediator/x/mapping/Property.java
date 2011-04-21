package org.hibernate.mediator.x.mapping;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.type.Type;
import org.hibernate.mediator.x.type.TypeFactory;

public class Property extends HObject {
	public static final String CL = "org.hibernate.mapping.Property"; //$NON-NLS-1$

	public Property(Object property) {
		super(property, CL);
	}

	public static Property newInstance() {
		return new Property(newInstance(CL));
	}

	public void setName(String name) {
		invoke(mn(), name);
	}

	public void setValue(Value value) {
		invoke(mn(), value);
	}

	public Value getValue() {
		return ValueFactory.createValueStub(invoke(mn()));
	}

	public void setCascade(String cascade) {
		invoke(mn(), cascade);
	}

	public String getName() {
		return (String)invoke(mn());
	}

	public Type getType() {
		return TypeFactory.createTypeStub(invoke(mn()));
	}

	public PersistentClass getPersistentClass() {
		return PersistentClassFactory.createPersistentClassStub(invoke(mn()));
	}

	public void setPersistentClass(PersistentClass ownerClass) {
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
