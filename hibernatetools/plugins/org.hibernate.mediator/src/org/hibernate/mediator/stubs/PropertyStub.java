package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public class PropertyStub extends HObject {
	public static final String CL = "org.hibernate.mapping.Property"; //$NON-NLS-1$

	protected PropertyStub(Object property) {
		super(property, CL);
	}

	public static PropertyStub newInstance() {
		return new PropertyStub(newInstance(CL));
	}

	public void setName(String name) {
		invoke("setName", name); //$NON-NLS-1$
	}

	public void setValue(ValueStub value) {
		invoke("setValue", value); //$NON-NLS-1$
	}

	public ValueStub getValue() {
		return ValueStubFactory.createValueStub(invoke("getValue")); //$NON-NLS-1$
	}

	public void setCascade(String cascade) {
		invoke("setCascade", cascade); //$NON-NLS-1$
	}

	public String getName() {
		return (String)invoke("getName"); //$NON-NLS-1$
	}

	public TypeStub getType() {
		return TypeStubFactory.createTypeStub(invoke("getType")); //$NON-NLS-1$
	}

	public PersistentClassStub getPersistentClass() {
		return PersistentClassStubFactory.createPersistentClassStub(invoke("getPersistentClass")); //$NON-NLS-1$
	}

	public void setPersistentClass(PersistentClassStub ownerClass) {
		invoke("setPersistentClass", ownerClass); //$NON-NLS-1$
	}

	public boolean isSelectable() {
		return (Boolean)invoke("isSelectable"); //$NON-NLS-1$
	}

	public boolean isInsertable() {
		return (Boolean)invoke("isInsertable"); //$NON-NLS-1$
	}

	public boolean isUpdateable() {
		return (Boolean)invoke("isUpdateable"); //$NON-NLS-1$
	}

	public boolean isLazy() {
		return (Boolean)invoke("isLazy"); //$NON-NLS-1$
	}

	public boolean isOptional() {
		return (Boolean)invoke("isOptional"); //$NON-NLS-1$
	}

	public boolean isNaturalIdentifier() {
		return (Boolean)invoke("isNaturalIdentifier"); //$NON-NLS-1$
	}

	public boolean isOptimisticLocked() {
		return (Boolean)invoke("isOptimisticLocked"); //$NON-NLS-1$
	}

	public String getCascade() {
		return (String)invoke("getCascade"); //$NON-NLS-1$
	}

	public String getNodeName() {
		return (String)invoke("getNodeName"); //$NON-NLS-1$
	}

	public boolean isBackRef() {
		return (Boolean)invoke("isBackRef"); //$NON-NLS-1$
	}

	public boolean isComposite() {
		return (Boolean)invoke("isComposite"); //$NON-NLS-1$
	}

	public String getPropertyAccessorName() {
		return (String)invoke("getPropertyAccessorName"); //$NON-NLS-1$
	}

}
