package org.hibernate.mediator.stubs;

public class ToOneStub extends SimpleValueStub {
	public static final String CL = "org.hibernate.mapping.ToOne"; //$NON-NLS-1$

	protected ToOneStub(Object toOne) {
		super(toOne, CL);
	}

	protected ToOneStub(Object toOne, String cn) {
		super(toOne, cn);
	}

	public void setReferencedEntityName(String referencedEntityName) {
		invoke("setReferencedEntityName", referencedEntityName); //$NON-NLS-1$
	}

	public void setReferencedPropertyName(String name) {
		invoke("setReferencedPropertyName", name); //$NON-NLS-1$
	}

	public void setFetchMode(FetchModeStub fetchMode) {
		invoke("setFetchMode", fetchMode); //$NON-NLS-1$
	}

	public boolean isEmbedded() {
		return (Boolean)invoke("isEmbedded"); //$NON-NLS-1$
	}
}
